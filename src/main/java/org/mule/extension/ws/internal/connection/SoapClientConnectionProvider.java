/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.connection;

import static java.lang.Thread.currentThread;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;

import org.mule.extension.ws.api.SoapVersionAdapter;
import org.mule.extension.ws.api.WebServiceSecurity;
import org.mule.extension.ws.api.transport.CustomTransportConfiguration;
import org.mule.extension.ws.api.transport.DefaultHttpTransportConfiguration;
import org.mule.extension.ws.internal.transport.DefaultHttpTransportConfigurationImpl;
import org.mule.extension.ws.internal.value.WsdlValueProvider;
import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.lifecycle.Lifecycle;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.DefaultEncoding;
import org.mule.runtime.extension.api.annotation.param.NullSafe;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.values.OfValues;
import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.runtime.extension.api.connectivity.NoConnectivityTest;
import org.mule.runtime.http.api.HttpService;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.client.HttpClientConfiguration;
import org.mule.soap.api.SoapWebServiceConfiguration;
import org.mule.soap.api.client.SoapClientFactory;
import org.mule.soap.api.transport.locator.DefaultTransportResourceLocator;
import org.mule.soap.api.transport.locator.TransportResourceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URL;
import java.util.Objects;

/**
 * {@link ConnectionProvider} that returns instances of {@link WscSoapClient}.
 *
 * @since 1.0
 */
public class SoapClientConnectionProvider implements CachedConnectionProvider<WscSoapClient>, NoConnectivityTest, Lifecycle {

  private final Logger LOGGER = LoggerFactory.getLogger(SoapClientConnectionProvider.class);
  private final SoapClientFactory SOAP_CLIENT_FACTORY = SoapClientFactory.getDefault();

  @Inject
  private HttpService httpService;

  @Inject
  private ExtensionsClient extensionsClient;

  private HttpClient client;

  @ParameterGroup(name = "Connection")
  @OfValues(WsdlValueProvider.class)
  private WsdlConnectionInfo info;

  @ParameterGroup(name = "Web Service Security", showInDsl = true)
  private WebServiceSecurity wsSecurity;

  /**
   * The soap version of the WSDL.
   */
  @Parameter
  @Placement(order = 5)
  @Optional(defaultValue = "SOAP11")
  private SoapVersionAdapter soapVersion;

  /**
   * If should use the MTOM protocol to manage the attachments or not.
   */
  @Parameter
  @Placement(order = 6)
  @Optional(defaultValue = "false")
  private boolean mtomEnabled;

  /**
   * Default character encoding to be used in all the messages. If not specified, the default charset in the mule configuration
   * will be used
   */
  @Parameter
  @Placement(order = 7)
  @Expression(NOT_SUPPORTED)
  @DefaultEncoding
  private String encoding;

  /**
   * The transport configuration used to dispatch the SOAP messages.
   */
  @Placement(tab = "Transport")
  @Parameter
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("Transport Configuration")
  @NullSafe(defaultImplementingType = DefaultHttpTransportConfiguration.class)
  private CustomTransportConfiguration customTransportConfiguration;

  /**
   * {@inheritDoc}
   */
  @Override
  public WscSoapClient connect() throws ConnectionException {
    try {
      // TODO(MULE-15847): remove instance of and move HTTP Client lifecycle to the subtype
      if (customTransportConfiguration instanceof DefaultHttpTransportConfiguration) {
        DefaultHttpTransportConfiguration transport = (DefaultHttpTransportConfiguration) customTransportConfiguration;
        this.customTransportConfiguration = new DefaultHttpTransportConfigurationImpl(client, transport.getTimeout());
      }
      return new WscSoapClient(info, () -> SOAP_CLIENT_FACTORY.create(getConfiguration()), customTransportConfiguration);
    } catch (Exception e) {
      throw new ConnectionException(e.getMessage(), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void disconnect(WscSoapClient client) {
    try {
      client.destroy();
    } catch (Exception e) {
      LOGGER.error("Error disconnecting soap client [" + client.toString() + "]: " + e.getMessage(), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ConnectionValidationResult validate(WscSoapClient client) {
    // TODO MULE-12036
    return ConnectionValidationResult.success();
  }

  private SoapWebServiceConfiguration getConfiguration() {
    TransportResourceLocator locator;
    if (customTransportConfiguration != null) {
      locator = customTransportConfiguration.resourceLocator(extensionsClient);
    } else {
      locator = new DefaultTransportResourceLocator();
    }

    return SoapWebServiceConfiguration.builder()
        .withService(info.getService())
        .withPort(info.getPort())
        .withWsdlLocation(getWsdlLocation(info.getWsdlLocation()))
        .withAddress(info.getAddress())
        .withEncoding(encoding)
        .enableMtom(mtomEnabled)
        .withSecurities(wsSecurity.strategiesList())
        .withResourceLocator(locator)
        .withVersion(soapVersion.getVersion())
        .build();
  }

  private String getWsdlLocation(String wsdlLocation) {
    URL resource = currentThread().getContextClassLoader().getResource(wsdlLocation);
    return resource != null ? resource.toString() : wsdlLocation;
  }

  @Override
  public void dispose() {
    // Do nothing
  }

  @Override
  public void initialise() {
    client = httpService.getClientFactory().create(new HttpClientConfiguration.Builder().setName("wsc-dispatcher").build());
  }

  @Override
  public void stop() {
    client.stop();
  }

  @Override
  public void start() {
    client.start();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SoapClientConnectionProvider that = (SoapClientConnectionProvider) o;
    return mtomEnabled == that.mtomEnabled &&
        Objects.equals(info, that.info) &&
        Objects.equals(wsSecurity, that.wsSecurity) &&
        soapVersion == that.soapVersion &&
        Objects.equals(encoding, that.encoding) &&
        Objects.equals(customTransportConfiguration, that.customTransportConfiguration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(httpService, extensionsClient, client, info, wsSecurity, soapVersion,
                        mtomEnabled, encoding, customTransportConfiguration);
  }
}
