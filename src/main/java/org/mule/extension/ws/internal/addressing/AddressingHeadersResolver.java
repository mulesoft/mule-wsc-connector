/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.addressing;

import org.mule.extension.ws.internal.addressing.properties.AddressingProperties;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

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
    ImmutableMap.Builder<String, String> headers = ImmutableMap.builder();
    if (properties.isRequired()) {
      properties.getAction().ifPresent(x -> headers.put(encoder.encodeActionHeader(x, false)));
      properties.getTo().ifPresent(x -> headers.put(encoder.encodeToHeader(x, properties.isMustUnderstand())));
      properties.getMessageID().ifPresent(x -> headers.put(encoder.encodeMessageIDHeader(x, false)));
      properties.getFrom().ifPresent(x -> headers.put(encoder.encodeFromHeader(x, false)));
      properties.getReplyTo().ifPresent(x -> headers.put(encoder.encodeReplyToHeader(x, false)));
      properties.getFaultTo().ifPresent(x -> headers.put(encoder.encodeFaultToHeader(x, false)));
      properties.getRelatesTo().ifPresent(x -> headers.put(encoder.encodeRelatesToHeader(x, false)));
    }
    return headers.build();
  }
}
