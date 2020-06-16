/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.addressing.properties;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public class AddressingProperties {

  private final boolean required;
  private final boolean mustUnderstand;
  private final String namespaceURI;
  private final URIType to;
  private final URIType action;
  private final EndpointReferenceType from;
  private final URIType messageID;
  private final EndpointReferenceType replyTo;
  private final EndpointReferenceType faultTo;
  private final RelatesToType relatesTo;

  private AddressingProperties() {
    this.required = false;
    this.mustUnderstand = false;
    this.namespaceURI = null;
    this.to = null;
    this.action = null;
    this.from = null;
    this.messageID = null;
    this.replyTo = null;
    this.faultTo = null;
    this.relatesTo = null;
  }

  public AddressingProperties(String namespaceURI, boolean mustUnderstand, URIType to, URIType action,
                              EndpointReferenceType from, URIType messageID, EndpointReferenceType replyTo,
                              EndpointReferenceType faultTo, RelatesToType relatesTo) {
    this.required = true;
    this.mustUnderstand = mustUnderstand;
    this.namespaceURI = namespaceURI;
    this.to = to;
    this.action = action;
    this.from = from;
    this.messageID = messageID;
    this.replyTo = replyTo;
    this.faultTo = faultTo;
    this.relatesTo = relatesTo;
  }

  public static AddressingProperties disabled() {
    return new AddressingProperties();
  }

  public boolean isRequired() {
    return required;
  }

  public boolean isMustUnderstand() {
    return mustUnderstand;
  }

  public String getNamespaceURI() {
    return namespaceURI;
  }

  public URIType getTo() {
    return to;
  }

  public URIType getAction() {
    return action;
  }

  public Optional<EndpointReferenceType> getFrom() {
    return ofNullable(from);
  }

  public Optional<URIType> getMessageID() {
    return ofNullable(messageID);
  }

  public Optional<EndpointReferenceType> getReplyTo() {
    return ofNullable(replyTo);
  }

  public Optional<EndpointReferenceType> getFaultTo() {
    return ofNullable(faultTo);
  }

  public Optional<RelatesToType> getRelatesTo() {
    return ofNullable(relatesTo);
  }

}
