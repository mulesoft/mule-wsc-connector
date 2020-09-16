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
import org.mule.runtime.extension.api.annotation.values.OfValues;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.ExpressionSupport.REQUIRED;
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.extension.api.annotation.param.display.Placement.ADVANCED_TAB;

/**
 * Web Service Addressing Settings
 *
 * @since 1.7
 */
public class AddressingSettings {

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

  public AddressingVersion getVersion() {
    return wsaVersion;
  }

  public String getFrom() {
    return wsaFrom;
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
}
