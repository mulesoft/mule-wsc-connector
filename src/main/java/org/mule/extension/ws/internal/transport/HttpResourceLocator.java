/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.transport;

import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.soap.api.transport.locator.TransportResourceLocator;

import java.io.InputStream;

import static java.util.Collections.emptyMap;

/**
 * {@link TransportResourceLocator} implementation that uses a custom HTTP connector requester configuration to
 * execute and fetch the resources.
 *
 * @since 1.2
 */
public class HttpResourceLocator implements TransportResourceLocator {

  private final ExtensionsClientHttpRequestExecutor executor;

  public HttpResourceLocator(String requesterConfig, ExtensionsClient client) {
    this.executor = new ExtensionsClientHttpRequestExecutor(requesterConfig, client);
  }

  @Override
  public boolean handles(String url) {
    return url.startsWith("http");
  }

  @Override
  public InputStream getResource(String url) {
    return executor.get(url, emptyMap()).getFirst();
  }
}
