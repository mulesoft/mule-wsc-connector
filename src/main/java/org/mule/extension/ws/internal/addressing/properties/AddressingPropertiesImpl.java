/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.addressing.properties;

import org.mule.runtime.core.api.util.UUID;
import org.mule.soap.api.message.AddressingProperties;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

/**
 * Model of Message Addressing Properties
 *
 * @since 1.7
 */
public class AddressingPropertiesImpl implements AddressingProperties {

  private final boolean required;
  private final String namespace;
  private final String to;
  private final String action;
  private final String from;
  private final String messageID;
  private final String relatesTo;

  private AddressingPropertiesImpl() {
    this.required = false;
    this.namespace = null;
    this.to = null;
    this.action = null;
    this.from = null;
    this.messageID = null;
    this.relatesTo = null;
  }

  public AddressingPropertiesImpl(String namespace, String to, String action,
                                  String from, String messageID, String relatesTo) {
    this.required = true;
    this.namespace = namespace;
    this.to = to;
    this.action = action;
    this.from = from;
    this.messageID = messageID;
    this.relatesTo = relatesTo;
  }

  public static AddressingPropertiesImpl disabled() {
    return new AddressingPropertiesImpl();
  }

  public static AddressingPropertiesBuilder builder() {
    return new AddressingPropertiesBuilder();
  }

  public boolean isRequired() {
    return required;
  }

  @Override
  public String getNamespace() {
    return namespace;
  }

  @Override
  public String getAction() {
    return action;
  }

  @Override
  public String getTo() {
    return to;
  }

  @Override
  public Optional<String> getFrom() {
    return ofNullable(from);
  }

  @Override
  public Optional<String> getMessageId() {
    return ofNullable(messageID);
  }

  @Override
  public Optional<String> getRelatesTo() {
    return ofNullable(relatesTo);
  }

  public static class AddressingPropertiesBuilder {

    private String namespace;
    private String to;
    private String action;
    private String messageId;
    private String from;
    private String relatesTo;

    public AddressingPropertiesBuilder namespace(String namespace) {
      this.namespace = namespace;
      return this;
    }

    public AddressingPropertiesBuilder to(String to) {
      this.to = to;
      return this;
    }

    public AddressingPropertiesBuilder action(String action) {
      this.action = action;
      return this;
    }

    public AddressingPropertiesBuilder relatesTo(String relatesTo) {
      this.relatesTo = relatesTo;
      return this;
    }

    public AddressingPropertiesBuilder messageId(String messageId) {
      this.messageId = messageId;
      return this;
    }

    public AddressingPropertiesBuilder from(String from) {
      this.from = from;
      return this;
    }

    public AddressingPropertiesImpl build() {
      if (Objects.isNull(to) && Objects.isNull(action) && Objects.isNull(from) && Objects.isNull(messageId)
          && Objects.isNull(relatesTo)) {
        return AddressingPropertiesImpl.disabled();
      }
      requireNonNull(namespace, "Namespace URI cannot be null");
      requireNonNull(to, "'To' cannot be null");
      requireNonNull(action, "'Action' cannot be null");

      if (Objects.isNull(messageId)) {
        messageId = UUID.getUUID();
      }

      return new AddressingPropertiesImpl(namespace, to, action, from, messageId, relatesTo);
    }
  }
}
