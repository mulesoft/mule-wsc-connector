/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.addressing;

import org.mule.extension.ws.internal.addressing.value.AddressingActionValueProvider;
import org.mule.extension.ws.internal.addressing.value.AddressingToValueProvider;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.ConfigOverride;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.annotation.param.reference.ConfigReference;
import org.mule.runtime.extension.api.annotation.values.OfValues;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.ExpressionSupport.REQUIRED;
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.extension.api.annotation.param.display.Placement.ADVANCED_TAB;

/**
 * Web Service Addressing Settings
 *
 * @since 2.0
 */
public class AddressingSettings {

  /**
   * Whether mustUnderstand attribute in {@code wsa:To} header is true or false.
   */
  @ConfigOverride
  @Parameter
  @Placement(tab = ADVANCED_TAB, order = 1)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("Must understand")
  @Summary("Value of the mustUnderstand attribute in WS-Addressing To header.")
  private boolean wsaMustUnderstand;

  /**
   * WS-Addressing version.
   */
  @ConfigOverride
  @Parameter
  @Placement(tab = ADVANCED_TAB, order = 2)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("Version")
  private AddressingVersion wsaVersion;

  /**
   * Reference of the endpoint where the message originated from.
   */
  @ConfigOverride
  @Parameter
  @Placement(tab = ADVANCED_TAB, order = 3)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("From")
  private String wsaFrom;

  /**
   * An identifier that uniquely identifies the semantics implied by this message.
   */
  @Parameter
  @Placement(tab = ADVANCED_TAB, order = 4)
  @Optional
  @Expression(SUPPORTED)
  @DisplayName("Action")
  @OfValues(AddressingActionValueProvider.class)
  private String wsaAction;

  /**
   * The address of the intended receiver of this message.
   */
  @Parameter
  @Placement(tab = ADVANCED_TAB, order = 5)
  @Optional
  @Expression(SUPPORTED)
  @DisplayName("To")
  @OfValues(AddressingToValueProvider.class)
  private String wsaTo;

  /**
   * An identifier that uniquely identifies this message in time and space.
   */
  @Parameter
  @Placement(tab = ADVANCED_TAB, order = 6)
  @Optional
  @Expression(REQUIRED)
  @DisplayName("Message ID")
  private String wsaMessageID;

  /**
   * A message ID that this message is related to.
   */
  @Parameter
  @Placement(tab = ADVANCED_TAB, order = 7)
  @Optional
  @Expression(SUPPORTED)
  @DisplayName("Relates to")
  private String wsaRelatesTo;

  /**
   * The type of relationship between this message and the one related to.
   */
  @Parameter
  @Placement(tab = ADVANCED_TAB, order = 8)
  @Optional
  @Expression(SUPPORTED)
  @DisplayName("Relationsship type")
  private String wsaRelationshipType;

  /**
   * The address of the intended receiver for faults related to this message.
   */
  @Parameter
  @Placement(tab = ADVANCED_TAB, order = 9)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("Fault to")
  private String wsaFaultTo;

  @ConfigOverride
  @ConfigReference(namespace = "HTTP", name = "LISTENER_CONFIG")
  @Parameter
  @Placement(tab = ADVANCED_TAB, order = 10)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("HTTP Listener")
  private String wsaHttpListenerConfig;

  public boolean isMustUnderstand() {
    return wsaMustUnderstand;
  }

  public AddressingVersion getVersion() {
    return wsaVersion;
  }

  public String getFrom() {
    return wsaFrom;
  }

  public String getHttpListenerConfig() {
    return wsaHttpListenerConfig;
  }

  public String getFaultTo() {
    return wsaFaultTo;
  }

  public String getAction() {
    return wsaAction;
  }

  public String getTo() {
    return wsaTo;
  }

  public String getMessageID() {
    return wsaMessageID;
  }

  public String getRelatesTo() {
    return wsaRelatesTo;
  }

  public String getRelationshipType() {
    return wsaRelationshipType;
  }
}
