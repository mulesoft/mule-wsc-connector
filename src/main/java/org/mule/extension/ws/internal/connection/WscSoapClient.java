/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.connection;

import org.mule.extension.ws.api.transport.CustomTransportConfiguration;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.util.LazyValue;
import org.mule.runtime.core.api.util.func.CheckedSupplier;
import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.soap.api.client.SoapClient;
import org.mule.soap.api.message.SoapRequest;
import org.mule.soap.api.message.SoapResponse;
import org.mule.soap.api.rm.CreateSequenceRequest;
import org.mule.soap.api.rm.TerminateSequenceRequest;
import org.mule.soap.api.transport.TransportResponse;

/**
 * Connection object that wraps a {@link SoapClient} with additional information required to resolve metadata.
 *
 * @since 1.1
 */
public class WscSoapClient {

  private final CustomTransportConfiguration transportConfiguration;
  private final WsdlConnectionInfo info;
  private final LazySoapClient lazyClient;

  private ExtensionsClient extensionsClient;

  public WscSoapClient(WsdlConnectionInfo info,
                       CheckedSupplier<SoapClient> clientSupplier,
                       CustomTransportConfiguration transportConfiguration,
                       ExtensionsClient extensionsClient) {
    this.info = info;
    this.lazyClient = new LazySoapClient(clientSupplier);
    this.transportConfiguration = transportConfiguration;
    this.extensionsClient = extensionsClient;
  }

  public SoapResponse consume(SoapRequest request, ExtensionsClient client) throws ConnectionException {
    return lazyClient.get().consume(request, transportConfiguration.buildDispatcher(client));
  }

  public SoapResponse parseResponse(String operation, TransportResponse response) throws ConnectionException {
    return lazyClient.get().parseResponse(operation, response);
  }

  public String createSequence(CreateSequenceRequest request, ExtensionsClient client) throws ConnectionException {
    return lazyClient.get().createSequence(request, transportConfiguration.buildDispatcher(client));
  }

  public void terminateSequence(TerminateSequenceRequest request, ExtensionsClient client) throws ConnectionException {
    lazyClient.get().terminateSequence(request, transportConfiguration.buildDispatcher(client));
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
    lazyClient.destroy();
  }

  private class LazySoapClient {

    private LazyValue<SoapClient> lazyClient;

    public LazySoapClient(CheckedSupplier<SoapClient> supplier) {
      this.lazyClient = new LazyValue<>(supplier);
    }

    public SoapClient get() throws ConnectionException {
      try {
        return lazyClient.get();
      } catch (ModuleException e) {
        throw e;
      } catch (Exception e) {
        // Throws connection exception in any other case for backward compatibility
        throw new ConnectionException("Error trying to acquire a new connection:" + e.getMessage(), e);
      }
    }

    public void destroy() {
      if (lazyClient.isComputed()) {
        lazyClient.get().destroy();
      }
    }
  }
}
