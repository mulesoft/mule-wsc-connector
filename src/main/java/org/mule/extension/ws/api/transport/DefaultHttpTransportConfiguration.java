/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.transport;

import org.mule.runtime.extension.api.annotation.param.*;
import org.mule.runtime.extension.api.client.*;
import org.mule.soap.api.transport.*;
import org.mule.soap.api.transport.locator.*;

public class DefaultHttpTransportConfiguration implements CustomTransportConfiguration {

  @Parameter
  @Optional(defaultValue = "5000")
  private int timeout;

  @Override
  public TransportDispatcher buildDispatcher(ExtensionsClient client) {
    return null;
  }

  @Override
  public TransportResourceLocator resourceLocator(ExtensionsClient client) {
    return null;
  }

  public int getTimeout() {
    return timeout;
  }
}
