/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.transport;

import org.mule.extension.ws.internal.transport.ExtensionsClientHttpRequestExecutor;
import org.mule.extension.ws.internal.transport.HttpResourceLocator;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.reference.ConfigReference;
import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.soap.api.transport.DispatcherException;
import org.mule.soap.api.transport.TransportDispatcher;
import org.mule.soap.api.transport.TransportResponse;
import org.mule.soap.api.transport.locator.TransportResourceLocator;

import java.util.Objects;

/**
 * Provides the capability to send soap messages using a custom HTTP Requester configuration.
 *
 * @since 1.1
 */
@Alias("http-transport-configuration")
public class CustomHttpTransportConfiguration implements CustomTransportConfiguration {

  public CustomHttpTransportConfiguration() {
  }

  @ConfigReference(namespace = "HTTP", name = "REQUEST_CONFIG")
  @Parameter
  private String requesterConfig;

  @Override
  public TransportDispatcher buildDispatcher(ExtensionsClient client) {
    return req -> {
      try {
        ExtensionsClientHttpRequestExecutor executor = new ExtensionsClientHttpRequestExecutor(requesterConfig, client);
        HttpRequestResponse postResult = executor.post(req.getAddress(), req.getHeaders(), req.getContent());
        return new TransportResponse(postResult.getContent(), postResult.getHttpHeaders(), postResult.getStatusLine());
      } catch (Exception e) {
        throw new DispatcherException("Error dispatching message with config [" + requesterConfig + "]:" + e.getMessage(), e);
      }
    };
  }

  @Override
  public TransportResourceLocator resourceLocator(ExtensionsClient client) {
    return new HttpResourceLocator(requesterConfig, client);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CustomHttpTransportConfiguration that = (CustomHttpTransportConfiguration) o;
    return Objects.equals(requesterConfig, that.requesterConfig);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requesterConfig);
  }

  public String getRequesterConfig() {
    return requesterConfig;
  }

  public void setRequesterConfig(String requesterConfig) {
    this.requesterConfig = requesterConfig;
  }
}
