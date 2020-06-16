/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.addressing.properties;

import org.mule.extension.ws.internal.connection.WsdlConnectionInfo;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.base.Strings;

public class AddressingPropertiesBuilder {

  private final WsdlConnectionInfo info;
  private final String operation;

  private String messageId;
  private boolean mustUnderstand;
  private String namespaceURI;
  private String to;
  private String action;
  private String relatesTo;
  private String from;
  private String replyTo;
  private String faultTo;
  private String relationshipType;

  public AddressingPropertiesBuilder(WsdlConnectionInfo info, String operation) {
    this.info = info;
    this.operation = operation;
  }

  public AddressingPropertiesBuilder mustUnderstand(boolean mustUnderstand) {
    this.mustUnderstand = mustUnderstand;
    return this;
  }

  public AddressingPropertiesBuilder withNamespace(String namespaceURI) {
    this.namespaceURI = namespaceURI;
    return this;
  }

  public AddressingPropertiesBuilder withTo(String to) {
    this.to = to;
    return this;
  }

  public AddressingPropertiesBuilder withAction(String action) {
    this.action = action;
    return this;
  }

  public AddressingPropertiesBuilder withRelatesTo(String relatesTo, String relationshipType) {
    this.relatesTo = relatesTo;
    this.relationshipType = relationshipType;
    return this;
  }

  public AddressingPropertiesBuilder withMessageID(String messageID) {
    this.messageId = messageID;
    return this;
  }

  public AddressingPropertiesBuilder withFrom(String from) {
    this.from = from;
    return this;
  }

  public AddressingPropertiesBuilder withReplyTo(String replyTo, String faultTo) {
    this.replyTo = replyTo;
    this.faultTo = faultTo;
    return this;
  }

  public AddressingProperties build() {
    checkNotNull(namespaceURI, "Namespace URI cannot be null");

    if (Strings.isNullOrEmpty(to)) {
      to = info.getAddress();
    }
    checkNotNull(to, "'To' cannot be null");

    if (Strings.isNullOrEmpty(action)) {
      action = info.getAddress() + "/" + info.getPort() + "/" + operation + "Request";
    }
    checkNotNull(action, "'Action' cannot be null");

    if (Strings.isNullOrEmpty(messageId)) {
      messageId = UUID.randomUUID().toString();
    }

    return new AddressingProperties(namespaceURI, mustUnderstand, new URIType(to), new URIType(action),
                                    !Strings.isNullOrEmpty(from) ? new EndpointReferenceType(new URIType(from)) : null,
                                    !Strings.isNullOrEmpty(messageId) ? new URIType(messageId) : null,
                                    !Strings.isNullOrEmpty(replyTo) ? new EndpointReferenceType(new URIType(replyTo)) : null,
                                    !Strings.isNullOrEmpty(faultTo) ? new EndpointReferenceType(new URIType(faultTo)) : null,
                                    !Strings.isNullOrEmpty(relatesTo) ? new RelatesToType(relatesTo, relationshipType)
                                        : null);
  }
}
