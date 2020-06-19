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
 * This class serves as {@link ParameterGroup} for configuring Web Service Security.
 *
 * @since 1.0
 */
public class AddressingConfiguration {

  public static final String ADDRESSING_TAB = "Addressing";

  /**
   * Indicates if it wants to use WSA on every consume operation.
   * <p>
   * Defaults to {@code false}
   */
  @Parameter
  @Placement(tab = ADDRESSING_TAB, order = 0)
  @Optional(defaultValue = "false")
  @Expression(NOT_SUPPORTED)
  @Summary("Indicates if it wants to use WSA on every consume operation.")
  private boolean useWsa;

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

  @Parameter
  @Placement(tab = ADDRESSING_TAB, order = 2)
  @Optional(defaultValue = "WSA200508")
  @Expression(NOT_SUPPORTED)
  @DisplayName("Version")
  private AddressingVersion wsaVersion;

  @Parameter
  @Placement(tab = ADDRESSING_TAB, order = 6)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("From")
  private String wsaFrom;

  @ConfigReference(namespace = "HTTP", name = "LISTENER_CONFIG")
  @Parameter
  @Placement(tab = ADDRESSING_TAB, order = 7)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("HTTP Listener")
  private String wsaHttpListenerConfig;

  @Parameter
  @Placement(tab = ADDRESSING_TAB, order = 8)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("Reply to")
  private String wsaReplyTo;

  @Parameter
  @Placement(tab = ADDRESSING_TAB, order = 9)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("Fault to")
  private String wsaFaultTo;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AddressingConfiguration that = (AddressingConfiguration) o;
    return Objects.equals(useWsa, that.useWsa)
        && Objects.equals(wsaMustUnderstand, that.wsaMustUnderstand)
        && Objects.equals(wsaVersion, that.wsaVersion)
        && Objects.equals(wsaFrom, that.wsaFrom)
        && Objects.equals(wsaHttpListenerConfig, that.wsaHttpListenerConfig)
        && Objects.equals(wsaReplyTo, that.wsaReplyTo)
        && Objects.equals(wsaFaultTo, that.wsaFaultTo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(useWsa, wsaMustUnderstand, wsaVersion, wsaFrom, wsaHttpListenerConfig, wsaReplyTo, wsaFaultTo);
  }
}
