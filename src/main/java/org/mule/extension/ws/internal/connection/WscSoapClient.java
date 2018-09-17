/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.connection;

import org.mule.extension.ws.api.transport.CustomTransportConfiguration;
import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.soap.api.client.SoapClient;
import org.mule.soap.api.message.SoapRequest;
import org.mule.soap.api.message.SoapResponse;

/**
 * Connection object that wraps a {@link SoapClient} with additional information required to resolve metadata.
 *
 * @since 1.1
 */
public class WscSoapClient {

  private final CustomTransportConfiguration transportConfiguration;
  private final String wsdlLocation;
  private final SoapClient delegate;

  public WscSoapClient(String wsdlLocation,
                       SoapClient soapClient,
                       CustomTransportConfiguration transportConfiguration) {

    this.wsdlLocation = wsdlLocation;
    this.delegate = soapClient;
    this.transportConfiguration = transportConfiguration;
  }

  public SoapResponse consume(SoapRequest request, ExtensionsClient client) {
    return delegate.consume(request, transportConfiguration.buildDispatcher(client));
  }

  public void destroy() {
    delegate.destroy();
  }

  public String getWsdlLocation() {
    return wsdlLocation;
  }
}
