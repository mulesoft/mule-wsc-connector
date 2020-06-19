/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.addressing.properties;

import org.mule.runtime.core.api.util.UUID;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.base.Strings;

public class AddressingPropertiesBuilder {

  private boolean mustUnderstand;
  private String namespaceURI;
  private URIType to, action, messageId;
  private EndpointReferenceType from, replyTo, faultTo;
  private RelatesToType relatesTo;

  public AddressingPropertiesBuilder mustUnderstand(boolean mustUnderstand) {
    this.mustUnderstand = mustUnderstand;
    return this;
  }

  public AddressingPropertiesBuilder namespaceURI(String namespaceURI) {
    this.namespaceURI = namespaceURI;
    return this;
  }

  public AddressingPropertiesBuilder to(String to) {
    this.to = getURIType(to);
    return this;
  }

  public AddressingPropertiesBuilder action(String action) {
    this.action = getURIType(action);
    return this;
  }

  public AddressingPropertiesBuilder relatesTo(String relatesTo, String relationshipType) {
    this.relatesTo = getRelatesTo(relatesTo, relationshipType);
    return this;
  }

  public AddressingPropertiesBuilder messageID(String messageID) {
    this.messageId = getURIType(messageID);
    return this;
  }

  public AddressingPropertiesBuilder from(String from) {
    this.from = getEndpointReferenceType(from);
    return this;
  }

  public AddressingPropertiesBuilder replyTo(String base, String replyTo, String faultTo) {
    this.replyTo = getEndpointReferenceType(buildPath(base, replyTo));
    this.faultTo = getEndpointReferenceType(buildPath(base, faultTo));
    return this;
  }

  public AddressingProperties build() {
    if (Objects.isNull(to) && Objects.isNull(action) && Objects.isNull(from) && Objects.isNull(messageId)
        && Objects.isNull(replyTo) && Objects.isNull(faultTo) && Objects.isNull(relatesTo)) {
      return AddressingProperties.disabled();
    }
    checkNotNull(namespaceURI, "Namespace URI cannot be null");
    checkNotNull(to, "'To' cannot be null");
    checkNotNull(action, "'Action' cannot be null");

    if (Objects.isNull(messageId)) {
      messageId = new URIType(UUID.getUUID());
    }

    return new AddressingProperties(namespaceURI, mustUnderstand, to, action, from, messageId, replyTo, faultTo, relatesTo);
  }

  private URIType getURIType(String value) {
    return !Strings.isNullOrEmpty(value) ? new URIType(value) : null;
  }

  private EndpointReferenceType getEndpointReferenceType(String value) {
    return !Strings.isNullOrEmpty(value) ? new EndpointReferenceType(new URIType(value)) : null;
  }

  private RelatesToType getRelatesTo(String value, String type) {
    return !Strings.isNullOrEmpty(value) ? new RelatesToType(value, type) : null;
  }

  private String buildPath(String base, String path) {
    if (path == null) {
      return null;
    }
    return base + (path.startsWith("/") ? path : "/" + path);
  }
}
