/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.extension.ws.internal.connection;

import org.mule.extension.ws.api.message.CustomTransportConfiguration;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.extension.api.soap.message.MessageDispatcher;
import org.mule.runtime.soap.api.client.SoapClient;
import org.mule.runtime.soap.api.client.metadata.SoapMetadataResolver;
import org.mule.runtime.soap.api.message.SoapRequest;
import org.mule.runtime.soap.api.message.SoapResponse;

import java.util.Optional;

public class SoapClientWrapper implements SoapClient {

  private final SoapClient client;
  private final CustomTransportConfiguration transportConfiguration;

  public SoapClientWrapper(SoapClient client, CustomTransportConfiguration transportConfiguration) {
    this.client = client;
    this.transportConfiguration = transportConfiguration;
  }

  @Override
  public void stop() throws MuleException {
    client.stop();
  }

  @Override
  public void start() throws MuleException {
    client.start();
  }

  @Override
  public SoapResponse consume(SoapRequest request, MessageDispatcher dispatcher) {
    return client.consume(request, dispatcher);
  }

  @Override
  public SoapResponse consume(SoapRequest request) {
    return client.consume(request);
  }

  @Override
  public SoapMetadataResolver getMetadataResolver() {
    return client.getMetadataResolver();
  }

  public Optional<CustomTransportConfiguration> getTransportConfiguration() {
    return Optional.ofNullable(transportConfiguration);
  }
}
