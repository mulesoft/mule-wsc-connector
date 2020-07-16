/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api;

import static java.util.Collections.EMPTY_MAP;
import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.joining;

import org.mule.extension.ws.api.addressing.AddressingAttributes;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;

import java.util.Map;

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

  /**
   * Addressing data
   */
  @Parameter
  @Optional
  private final AddressingAttributes addressing;

  public SoapAttributes(Map<String, String> protocolHeaders, Map<String, String> additionalTransportData) {
    this(protocolHeaders, additionalTransportData, null);
  }

  public SoapAttributes(Map<String, String> protocolHeaders, Map<String, String> additionalTransportData,
                        AddressingAttributes addressing) {
    this.protocolHeaders = unmodifiableMap(protocolHeaders != null ? protocolHeaders : EMPTY_MAP);
    this.additionalTransportData = unmodifiableMap(additionalTransportData != null ? additionalTransportData : EMPTY_MAP);
    this.addressing = addressing;
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

  /**
   * @return a set of additional addressing data
   *
   * @since 2.0
   */
  public AddressingAttributes getAddressing() {
    return addressing;
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

    String addressingDataAsString = "";
    if (addressing != null) {
      addressingDataAsString =
          "  addressing = {\n" +
              "    messageId:" + addressing.getMessageId() + "\n" +
              "  }\n";
    }

    return "{\n" +
        "  additionalTransportData = [\n" +
        "    " + transportDataAsString + "\n" +
        "  ]\n" +
        "  protocolHeaders = [\n" +
        "    " + headersAsString + "\n" +
        "  ]\n" +
        addressingDataAsString +
        "}\n";
  }
}
