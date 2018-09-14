/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.copyOf;
import static com.google.common.collect.ImmutableMap.of;

public class SoapAttributes {

  private static final long serialVersionUID = 4591210489306615571L;

  private final Map<String, String> protocolHeaders;

  public SoapAttributes(Map<String, String> protocolHeaders) {
    this.protocolHeaders = protocolHeaders != null ? copyOf(protocolHeaders) : of();
  }

  /**
   * @return a set of protocol headers bounded to the service response. i.e. HTTP Headers.
   */
  public Map<String, String> getProtocolHeaders() {
    return protocolHeaders;
  }
}
