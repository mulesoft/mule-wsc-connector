/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api;

import static com.google.common.collect.ImmutableMap.copyOf;
import static com.google.common.collect.ImmutableMap.of;
import static java.util.stream.Collectors.joining;

import org.mule.runtime.extension.api.annotation.param.Parameter;

import java.util.Map;

import org.mulesoft.common.ext.Diff;

/**
 * The attributes returned by the consume operation, it carries the protocol specific headers (i.e. HTTP headers) and
 * additional transport data (for example HTTP status line) bounded to the response.
 *
 * @since 1.1.2
 */
public class SoapAttributes {

  private static final long serialVersionUID = 4591210489306615571L;

  /**
   * The protocol headers bundled in the response.
   */
  @Parameter
  private final Map<String, String> protocolHeaders;

  /**
   * The additional transport data bundled in the response.
   */
  @Parameter
  private final Map<String, String> additionalTransportData;

  public SoapAttributes(Map<String, String> protocolHeaders, Map<String, String> additionalTransportData) {
    this.protocolHeaders = protocolHeaders != null ? copyOf(protocolHeaders) : of();
    this.additionalTransportData = additionalTransportData != null ? copyOf(additionalTransportData) : of();
  }

  /**
   * @return a set of protocol headers bounded to the service response. i.e. HTTP Headers.
   */
  public Map<String, String> getProtocolHeaders() {
    return protocolHeaders;
  }

  /**
   * @return a set of additional transport data bounded to the service response (for example HTTP status line).
   */
  public Map<String, String> getAdditionalTransportData() {
    return additionalTransportData;
  }

  @Override
  public String toString() {
    String headersAsString = protocolHeaders.entrySet()
        .stream()
        .map(e -> e.getKey() + ":" + e.getValue())
        .collect(joining(",\n    "));

    String transportDataAsString = additionalTransportData.entrySet()
        .stream()
        .map(e -> e.getKey() + ":" + e.getValue())
        .collect(joining(",\n    "));

    return "{\n" +
        "  additionalTransportData = [\n" +
        "    " + transportDataAsString + "\n" +
        "  ]\n" +
        "  protocolHeaders = [\n" +
        "    " + headersAsString + "\n" +
        "  ]\n" +
        "}";
  }
}
