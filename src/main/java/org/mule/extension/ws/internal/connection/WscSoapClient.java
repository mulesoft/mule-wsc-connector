/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.connection;

import org.mule.extension.ws.api.transport.CustomTransportConfiguration;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.soap.api.client.SoapClient;
import org.mule.soap.api.message.SoapRequest;
import org.mule.soap.api.message.SoapResponse;
import org.mule.soap.api.exception.InvalidWsdlException;
import org.mule.wsdl.parser.exception.WsdlGettingException;
import org.mule.wsdl.parser.exception.WsdlParsingException;

import java.util.function.Supplier;

/**
 * Connection object that wraps a {@link SoapClient} with additional information required to resolve metadata.
 *
 * @since 1.1
 */
public class WscSoapClient {

  private final CustomTransportConfiguration transportConfiguration;
  private final WsdlConnectionInfo info;
  private final Supplier<SoapClient> clientSupplier;
  private SoapClient delegate;
  private ExtensionsClient extensionsClient;

  public WscSoapClient(WsdlConnectionInfo info,
                       Supplier<SoapClient> clientSupplier,
                       CustomTransportConfiguration transportConfiguration,
                       ExtensionsClient extensionsClient) {
    this.info = info;
    this.clientSupplier = clientSupplier;
    this.transportConfiguration = transportConfiguration;
    this.extensionsClient = extensionsClient;
  }

  public SoapResponse consume(SoapRequest request, ExtensionsClient client) throws ConnectionException {
    if (delegate == null) {
      try {
        delegate = clientSupplier.get();
      } catch (Exception e) {
        if (e instanceof WsdlGettingException) {
          // can not get any wsdl
          throw new ConnectionException("Error getting a wsdl:" + e.getMessage(), e);
        } else if (e instanceof WsdlParsingException) {
          // cant parse the wsdl
          throw new InvalidWsdlException("Error parsing wsdl:" + e.getMessage(), e);
        }
        // Throws connection exception in any other case for backward compatibility
        throw new ConnectionException("Error trying to acquire a new connection:" + e.getMessage(), e);
      }
    }
    return delegate.consume(request, transportConfiguration.buildDispatcher(client));
  }

  public CustomTransportConfiguration getTransportConfiguration() {
    return transportConfiguration;
  }

  public WsdlConnectionInfo getInfo() {
    return info;
  }

  public ExtensionsClient getExtensionsClient() {
    return extensionsClient;
  }

  public void destroy() {
    if (delegate != null) {
      delegate.destroy();
    }
  }
}
