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

import java.util.Objects;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;

/**
 * This class serves as {@link ParameterGroup} for configuring Web Service Addressing.
 *
 * @since 1.7
 */
public class AddressingConfiguration {

  private static final String ADDRESSING_TAB = "Addressing";

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AddressingConfiguration that = (AddressingConfiguration) o;
    return Objects.equals(wsaVersion, that.wsaVersion)
        && Objects.equals(wsaFrom, that.wsaFrom);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wsaVersion, wsaFrom);
  }

  public AddressingVersion getWsaVersion() {
    return wsaVersion;
  }
}
