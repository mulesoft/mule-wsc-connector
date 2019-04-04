/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.transport;

import org.mule.extension.ws.internal.transport.DefaultHttpTransportConfigurationImpl;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.soap.api.transport.TransportDispatcher;
import org.mule.soap.api.transport.locator.TransportResourceLocator;

import java.util.Objects;

/**
 * DTO subtype used to instantiate {@link DefaultHttpTransportConfigurationImpl} objects.
 *
 * @since 1.1
 */
public class DefaultHttpTransportConfiguration implements CustomTransportConfiguration {

  @Parameter
  @Optional(defaultValue = "5000")
  private int timeout;

  @Override
  public TransportDispatcher buildDispatcher(ExtensionsClient client) {
    // Just using this impl as DTO
    return null;
  }

  @Override
  public TransportResourceLocator resourceLocator(ExtensionsClient client) {
    // Just using this impl as DTO
    return null;
  }

  public int getTimeout() {
    return timeout;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DefaultHttpTransportConfiguration that = (DefaultHttpTransportConfiguration) o;
    return timeout == that.timeout;
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeout);
  }
}
