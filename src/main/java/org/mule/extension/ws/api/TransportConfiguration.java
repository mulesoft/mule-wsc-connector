/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api;

import org.mule.runtime.extension.api.annotation.param.NullSafe;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

import java.util.Map;

/**
 * Component that specifies how to configure the transport used to send the request.
 *
 * @since 1.0
 */
public class TransportConfiguration {

  public TransportConfiguration(Map<String, String> transportHeaders) {
    this.transportHeaders = transportHeaders;
  }

  /**
   * A group of transport headers that will be bounded with the transport request.
   */
  @Parameter
  @Optional
  @DisplayName("Headers")
  @NullSafe
  private Map<String, String> transportHeaders;

  public Map<String, String> getTransportHeaders() {
    return transportHeaders;
  }

  public void setTransportHeaders(Map<String, String> transportHeaders) {
    this.transportHeaders = transportHeaders;
  }
}
