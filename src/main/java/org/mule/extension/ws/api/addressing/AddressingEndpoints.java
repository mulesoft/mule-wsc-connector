/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.addressing;

import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.dsl.xml.TypeDsl;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.reference.ConfigReference;

import java.util.Objects;

import static org.mule.extension.ws.api.addressing.AddressingConfiguration.ADDRESSING_TAB;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;

public class AddressingEndpoints {

  @ConfigReference(namespace = "HTTP", name = "LISTENER_CONFIG")
  @Parameter
  @Placement(tab = ADDRESSING_TAB, order = 8)
  @Expression(NOT_SUPPORTED)
  @DisplayName("HTTP Listener")
  private String wsaHttpListenerConfig;

  @Parameter
  @Placement(tab = ADDRESSING_TAB, order = 9)
  @Expression(NOT_SUPPORTED)
  @DisplayName("Reply to path")
  private String wsaReplyTo;

  @Parameter
  @Placement(tab = ADDRESSING_TAB, order = 10)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("Fault to path")
  private String wsaFaultTo;

  public AddressingEndpoints() {}

  public String getWsaReplyTo() {
    return wsaReplyTo;
  }

  public void setWsaReplyTo(String wsaReplyTo) {
    this.wsaReplyTo = wsaReplyTo;
  }

  public String getWsaFaultTo() {
    return wsaFaultTo;
  }

  public void setWsaFaultTo(String wsaFaultTo) {
    this.wsaFaultTo = wsaFaultTo;
  }

  public String getWsaHttpListenerConfig() {
    return wsaHttpListenerConfig;
  }

  public void setWsaHttpListenerConfig(String wsaHttpListenerConfig) {
    this.wsaHttpListenerConfig = wsaHttpListenerConfig;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AddressingEndpoints that = (AddressingEndpoints) o;
    return Objects.equals(wsaHttpListenerConfig, that.wsaHttpListenerConfig) &&
        Objects.equals(wsaReplyTo, that.wsaReplyTo) &&
        Objects.equals(wsaFaultTo, that.wsaFaultTo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wsaHttpListenerConfig, wsaReplyTo, wsaFaultTo);
  }
}
