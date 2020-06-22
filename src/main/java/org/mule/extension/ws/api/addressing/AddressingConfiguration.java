/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.addressing;

import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.annotation.param.reference.ConfigReference;

import java.util.Objects;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;

/**
 * This class serves as {@link ParameterGroup} for configuring Web Service Addressing.
 *
 * @since 2.0
 */
public class AddressingConfiguration {

  private static final String ADDRESSING_TAB = "Addressing";

  /**
   * Whether mustUnderstand attribute in {@code wsa:To} header is true or false.
   * <p>
   * Defaults to {@code false}
   */
  @Parameter
  @Placement(tab = ADDRESSING_TAB, order = 1)
  @Optional(defaultValue = "false")
  @Expression(NOT_SUPPORTED)
  @DisplayName("Must understand")
  @Summary("Value of the mustUnderstand attribute in WS-Addressing header.")
  private boolean wsaMustUnderstand;

  /**
   * WS-Addressing version.
   * <p>
   * Defaults to {@code WSA 2005-08}
   */
  @Parameter
  @Placement(tab = ADDRESSING_TAB, order = 2)
  @Optional(defaultValue = "WSA200508")
  @Expression(NOT_SUPPORTED)
  @DisplayName("Version")
  private AddressingVersion wsaVersion;

  /**
   * Reference of the endpoint where the message originated from.
   */
  @Parameter
  @Placement(tab = ADDRESSING_TAB, order = 3)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("From")
  private String wsaFrom;

  @ConfigReference(namespace = "HTTP", name = "LISTENER_CONFIG")
  @Parameter
  @Placement(tab = ADDRESSING_TAB, order = 4)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("HTTP Listener")
  private String wsaHttpListenerConfig;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AddressingConfiguration that = (AddressingConfiguration) o;
    return Objects.equals(wsaMustUnderstand, that.wsaMustUnderstand)
        && Objects.equals(wsaVersion, that.wsaVersion)
        && Objects.equals(wsaFrom, that.wsaFrom)
        && Objects.equals(wsaHttpListenerConfig, that.wsaHttpListenerConfig);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wsaMustUnderstand, wsaVersion, wsaFrom, wsaHttpListenerConfig);
  }
}
