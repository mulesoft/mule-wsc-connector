/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.connection;

import org.mule.extension.ws.api.SoapVersionAdapter;
import org.mule.extension.ws.api.WebServiceSecurity;
import org.mule.extension.ws.api.transport.CustomTransportConfiguration;
import org.mule.extension.ws.api.transport.DefaultHttpTransportConfiguration;
import org.mule.extension.ws.internal.value.WsdlValueProvider;
import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.lifecycle.Lifecycle;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.DefaultEncoding;
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
import org.mule.soap.api.client.SoapClient;
import org.mule.soap.api.client.SoapClientFactory;
import org.mule.soap.api.transport.locator.NullTransportResourceLocator;
import org.mule.soap.api.transport.locator.TransportResourceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URL;

import static java.lang.Thread.currentThread;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;

/**
 * {@link ConnectionProvider} that returns instances of {@link WscSoapClient}.
 *
 * @since 1.0
 */
public class SoapClientConnectionProvider implements CachedConnectionProvider<WscSoapClient>, Lifecycle, NoConnectivityTest {

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
  private CustomTransportConfiguration customTransportConfiguration;

  /**
   * {@inheritDoc}
   */
  @Override
  public WscSoapClient connect() throws ConnectionException {
    try {
      if (customTransportConfiguration == null) {
        customTransportConfiguration = new DefaultHttpTransportConfiguration(client);
      }
      return new WscSoapClient(info.getWsdlLocation(),
                               SOAP_CLIENT_FACTORY.create(getConfiguration()),
                               customTransportConfiguration);
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
      locator = new NullTransportResourceLocator();
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
}
