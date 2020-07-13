/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.addressing;

import org.mule.extension.ws.internal.addressing.properties.AddressingProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles how to resolve {@link AddressingProperties} into a {@link Map<String, String>} of xml headers.
 *
 * @since 2.0
 */
public class AddressingHeadersResolver {

  private final HeadersEncoder encoder;

  public AddressingHeadersResolver(HeadersEncoder encoder) {
    this.encoder = encoder;
  }

  public Map<String, String> resolve(AddressingProperties properties) {
    Map<String, String> headers = new HashMap<>();
    if (properties.isRequired()) {
      properties.getAction().ifPresent(x -> addEntryTo(headers, encoder.encodeActionHeader(x, false)));
      properties.getTo().ifPresent(x -> addEntryTo(headers, encoder.encodeToHeader(x, properties.isMustUnderstand())));
      properties.getMessageID().ifPresent(x -> addEntryTo(headers, encoder.encodeMessageIDHeader(x, false)));
      properties.getFrom().ifPresent(x -> addEntryTo(headers, encoder.encodeFromHeader(x, false)));
      properties.getReplyTo().ifPresent(x -> addEntryTo(headers, encoder.encodeReplyToHeader(x, false)));
      properties.getFaultTo().ifPresent(x -> addEntryTo(headers, encoder.encodeFaultToHeader(x, false)));
      properties.getRelatesTo().ifPresent(x -> addEntryTo(headers, encoder.encodeRelatesToHeader(x, false)));
    }
    return headers;
  }

  private static void addEntryTo(Map<String, String> map, Map.Entry<String, String> entry) {
    map.put(entry.getKey(), entry.getValue());
  }
}
