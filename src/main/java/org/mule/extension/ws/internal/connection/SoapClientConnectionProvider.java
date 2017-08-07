/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.connection;

import org.apache.log4j.Logger;
import org.mule.extension.ws.api.WebServiceSecurity;
import org.mule.extension.ws.api.message.CustomTransportConfiguration;
import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.lifecycle.InitialisationException;
import org.mule.runtime.api.lifecycle.Lifecycle;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.DefaultEncoding;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.param.display.Path;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.values.OfValues;
import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.runtime.extension.api.soap.message.MessageDispatcher;
import org.mule.runtime.http.api.HttpService;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.client.HttpClientConfiguration;
import org.mule.runtime.soap.api.SoapService;
import org.mule.runtime.soap.api.SoapVersion;
import org.mule.runtime.soap.api.client.SoapClient;
import org.mule.runtime.soap.api.client.SoapClientConfiguration;
import org.mule.runtime.soap.api.message.dispatcher.DefaultHttpMessageDispatcher;
import org.mule.runtime.soap.api.transport.NullTransportResourceLocator;
import org.mule.runtime.soap.api.transport.TransportResourceLocator;

import javax.inject.Inject;
import java.net.URL;

import static java.lang.Thread.currentThread;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.model.display.PathModel.Type.FILE;

/**
 * {@link ConnectionProvider} that returns instances of {@link SoapClient}.
 *
 * @since 1.0
 */
public class SoapClientConnectionProvider implements CachedConnectionProvider<SoapClient>, Lifecycle {

  private final Logger LOGGER = Logger.getLogger(SoapClientConnectionProvider.class);

  @Inject
  private SoapService soapService;

  @Inject
  private HttpService httpService;

  @Inject
  private ExtensionsClient extensionsClient;

  private HttpClient client;

  /**
   * The WSDL file URL remote or local.
   */
  @Placement(order = 1)
  @Parameter
  @Example("http://www.somehost.com/location?wsdl")
  @Path(type = FILE, acceptedFileExtensions = "wsdl", acceptsUrls = true)
  private String wsdlLocation;


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
  private SoapVersion soapVersion;

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
  public SoapClient connect() throws ConnectionException {
    return soapService.getClientFactory().create(getConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void disconnect(SoapClient client) {
    try {
      client.stop();
    } catch (Exception e) {
      LOGGER.error("Error disconnecting soap client [" + client.toString() + "]: " + e.getMessage(), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ConnectionValidationResult validate(SoapClient client) {
    // TODO MULE-12036
    return ConnectionValidationResult.success();
  }

  private SoapClientConfiguration getConfiguration() {
    MessageDispatcher dispatcher;
    TransportResourceLocator locator;
    if (customTransportConfiguration != null) {
      dispatcher = customTransportConfiguration.buildDispatcher(extensionsClient);
      locator = customTransportConfiguration.resourceLocator(extensionsClient);
    } else {
      dispatcher = new DefaultHttpMessageDispatcher(client);
      locator = new NullTransportResourceLocator();
    }

    return SoapClientConfiguration.builder()
        .withService(info.getService())
        .withPort(info.getPort())
        .withWsdlLocation(getWsdlLocation(wsdlLocation))
        .withAddress(info.getAddress())
        .withEncoding(encoding)
        .enableMtom(mtomEnabled)
        .withSecurities(wsSecurity.strategiesList())
        .withDispatcher(dispatcher)
        .withResourceLocator(locator)
        .withVersion(soapVersion)
        .build();
  }

  private String getWsdlLocation(String wsdlLocation) {
    URL resource = currentThread().getContextClassLoader().getResource(wsdlLocation);
    return resource != null ? resource.getPath() : wsdlLocation;
  }

  @Override
  public void dispose() {
    // Do nothing
  }

  @Override
  public void initialise() throws InitialisationException {
    client = httpService.getClientFactory().create(new HttpClientConfiguration.Builder().setName("wsc-dispatcher").build());
  }

  @Override
  public void stop() throws MuleException {
    client.stop();
  }

  @Override
  public void start() throws MuleException {
    client.start();
  }
}
