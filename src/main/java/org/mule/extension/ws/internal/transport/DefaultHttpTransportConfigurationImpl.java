/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.transport;

import org.mule.extension.ws.api.transport.*;
import org.mule.extension.ws.internal.connection.*;
import org.mule.runtime.extension.api.client.*;
import org.mule.runtime.http.api.client.*;
import org.mule.soap.api.transport.*;
import org.mule.soap.api.transport.locator.*;

/**
 * Default transport configuration, sends SOAP messages through HTTP with a default configuration.
 *
 * @since 1.1
 */
public class DefaultHttpTransportConfigurationImpl implements CustomTransportConfiguration {


  private final HttpClient httpClient;
  private final int timeout;

  public DefaultHttpTransportConfigurationImpl(HttpClient client, int timeout) {
    this.httpClient = client;
    this.timeout = timeout;
  }

  @Override
  public TransportDispatcher buildDispatcher(ExtensionsClient client) {
    return new DefaultHttpMessageDispatcher(httpClient, timeout);
  }

  @Override
  public TransportResourceLocator resourceLocator(ExtensionsClient client) {
    return new NullTransportResourceLocator();
  }
}
