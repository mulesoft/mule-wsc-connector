/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.addressing;

import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.ConfigOverride;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.annotation.param.reference.ConfigReference;

import static org.mule.extension.ws.api.addressing.AddressingConfiguration.ADDRESSING_TAB;
import static org.mule.runtime.api.meta.ExpressionSupport.*;
import static org.mule.runtime.extension.api.annotation.param.display.Placement.ADVANCED_TAB;

public class AddressingSettings {

  /**
   * Indicates if it wants to use WSA on every consume operation.
   */
  @ConfigOverride
  @Parameter
  @Placement(order = 0)
  @Optional
  @Expression(NOT_SUPPORTED)
  @Summary("Indicates if it wants to use WSA on every consume operation.")
  private boolean useWsa;

  /**
   * Whether mustUnderstand attribute in {@code wsa:To} header is true or false.
   */
  @ConfigOverride
  @Parameter
  @Placement(order = 1)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("Must understand")
  @Summary("Value of the mustUnderstand attribute in WS-Addressing To header.")
  private boolean wsaMustUnderstand;

  @ConfigOverride
  @Parameter
  @Placement(order = 2)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("Version")
  private AddressingVersion wsaVersion;

  @ConfigOverride
  @Parameter
  @Placement(order = 6)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("From")
  private String wsaFrom;

  @ConfigOverride
  @ConfigReference(namespace = "HTTP", name = "LISTENER_CONFIG")
  @Parameter
  @Placement(order = 7)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("HTTP Listener")
  private String wsaHttpListenerConfig;

  @ConfigOverride
  @Parameter
  @Placement(order = 9)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("Fault to")
  private String wsaFaultTo;

  @Parameter
  @Placement(order = 10)
  @Optional
  @Expression(SUPPORTED)
  @DisplayName("Action")
  private String wsaAction;

  @Parameter
  @Placement(order = 11)
  @Optional
  @Expression(SUPPORTED)
  @DisplayName("To")
  private String wsaTo;

  @Parameter
  @Placement(order = 12)
  @Optional
  @Expression(REQUIRED)
  @DisplayName("Message ID")
  private String wsaMessageID;

  @Parameter
  @Placement(order = 12)
  @Optional
  @Expression(SUPPORTED)
  @DisplayName("Relates to")
  private String wsaRelatesTo;

  @Parameter
  @Placement(order = 14)
  @Optional
  @Expression(SUPPORTED)
  @DisplayName("Relates to")
  private String wsaRelationshipType;

  public boolean isUseWsa() {
    return useWsa;
  }

  public boolean isWsaMustUnderstand() {
    return wsaMustUnderstand;
  }

  public AddressingVersion getWsaVersion() {
    return wsaVersion;
  }

  public String getWsaFrom() {
    return wsaFrom;
  }

  public String getWsaHttpListenerConfig() {
    return wsaHttpListenerConfig;
  }

  public String getWsaFaultTo() {
    return wsaFaultTo;
  }

  public String getWsaAction() {
    return wsaAction;
  }

  public String getWsaTo() {
    return wsaTo;
  }

  public String getWsaMessageID() {
    return wsaMessageID;
  }

  public String getWsaRelatesTo() {
    return wsaRelatesTo;
  }

  public String getWsaRelationshipType() {
    return wsaRelationshipType;
  }
}
