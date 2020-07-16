/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.addressing;

import org.mule.extension.ws.internal.addressing.properties.AddressingProperties;
import org.mule.extension.ws.internal.addressing.properties.EndpointReferenceType;
import org.mule.extension.ws.internal.addressing.properties.RelatesToType;
import org.mule.extension.ws.internal.addressing.properties.URIType;

import static java.util.Map.Entry;

/**
 * Encodes {@link AddressingProperties} model's types into a pair of
 * key-value of type {@link Entry<String, String>}
 *
 * @since 2.0
 */
public interface HeadersEncoder {

  Entry<String, String> encodeMessageIDHeader(URIType messageID, boolean mustUnderstand);

  Entry<String, String> encodeToHeader(URIType to, boolean mustUnderstand);

  Entry<String, String> encodeActionHeader(URIType action, boolean mustUnderstand);

  Entry<String, String> encodeFromHeader(EndpointReferenceType from, boolean mustUnderstand);

  Entry<String, String> encodeReplyToHeader(EndpointReferenceType replyTo, boolean mustUnderstand);

  Entry<String, String> encodeFaultToHeader(EndpointReferenceType to, boolean mustUnderstand);

  Entry<String, String> encodeRelatesToHeader(RelatesToType relatesTo, boolean mustUnderstand);
}
