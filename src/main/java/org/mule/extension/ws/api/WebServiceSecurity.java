/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.util.collection.Collectors.toImmutableList;

import org.mule.extension.ws.api.security.SecurityStrategyAdapter;
import org.mule.extension.ws.api.security.WssDecryptSecurityStrategyAdapter;
import org.mule.extension.ws.api.security.WssEncryptSecurityStrategyAdapter;
import org.mule.extension.ws.api.security.WssSignSecurityStrategyAdapter;
import org.mule.extension.ws.api.security.WssTimestampSecurityStrategyAdapter;
import org.mule.extension.ws.api.security.WssIncomingTimestampSecurityStrategyAdapter;
import org.mule.extension.ws.api.security.WssUsernameTokenSecurityStrategyAdapter;
import org.mule.extension.ws.api.security.WssVerifySignatureSecurityStrategyAdapter;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.soap.api.security.SecurityStrategy;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * This class serves as {@link ParameterGroup} for configuring Web Service Security.
 *
 * @since 1.0
 */
public class WebServiceSecurity {

  private static final String SECURITY_TAB = "Security";

  /**
   * a sign WSS configuration
   */
  @Parameter
  @Expression(NOT_SUPPORTED)
  @Placement(tab = SECURITY_TAB, order = 0)
  @Optional
  private WssSignSecurityStrategyAdapter signSecurityStrategy;

  /**
   * a verify signature WSS configuration
   */
  @Parameter
  @Expression(NOT_SUPPORTED)
  @Placement(tab = SECURITY_TAB, order = 4)
  @Optional
  private WssVerifySignatureSecurityStrategyAdapter verifySignatureSecurityStrategy;

  /**
   * a usernameToken WSS configuration
   */
  @Parameter
  @Expression(NOT_SUPPORTED)
  @Placement(tab = SECURITY_TAB, order = 1)
  @Optional
  private WssUsernameTokenSecurityStrategyAdapter usernameTokenSecurityStrategy;

  /**
   * a timestamp WSS configuration
   */
  @Parameter
  @Expression(NOT_SUPPORTED)
  @Placement(tab = SECURITY_TAB, order = 3)
  @Optional
  @DisplayName("Outgoing Timestamp Security Strategy")
  private WssTimestampSecurityStrategyAdapter timestampSecurityStrategy;

  /**
   * a decrypt WSS configuration
   */
  @Parameter
  @Expression(NOT_SUPPORTED)
  @Placement(tab = SECURITY_TAB, order = 5)
  @Optional
  private WssDecryptSecurityStrategyAdapter decryptSecurityStrategy;

  /**
   * an encrypt WSS configuration
   */
  @Parameter
  @Expression(NOT_SUPPORTED)
  @Placement(tab = SECURITY_TAB, order = 2)
  @Optional
  private WssEncryptSecurityStrategyAdapter encryptSecurityStrategy;

  /**
   * a timestamp verification WSS configuration
   */
  @Parameter
  @Expression(NOT_SUPPORTED)
  @Optional
  @Placement(tab = SECURITY_TAB, order = 6)
  @DisplayName("Incoming Timestamp Security Strategy")
  private WssIncomingTimestampSecurityStrategyAdapter incomingTimestampSecurityStrategy;


  public List<SecurityStrategy> strategiesList() {
    // Default order: Timestamp UsernameToken Signature Encryption
    // Timestamp and UsernameToken actions need to come first because they are needed in case
    // signing/encryption use them.
    return Stream.of(timestampSecurityStrategy,
                     usernameTokenSecurityStrategy,
                     signSecurityStrategy,
                     encryptSecurityStrategy,
                     decryptSecurityStrategy,
                     verifySignatureSecurityStrategy,
                     incomingTimestampSecurityStrategy)
        .filter(Objects::nonNull)
        .map(SecurityStrategyAdapter::getSecurityStrategy)
        .collect(toImmutableList());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    WebServiceSecurity that = (WebServiceSecurity) o;
    return Objects.equals(signSecurityStrategy, that.signSecurityStrategy) &&
        Objects.equals(verifySignatureSecurityStrategy, that.verifySignatureSecurityStrategy) &&
        Objects.equals(usernameTokenSecurityStrategy, that.usernameTokenSecurityStrategy) &&
        Objects.equals(timestampSecurityStrategy, that.timestampSecurityStrategy) &&
        Objects.equals(incomingTimestampSecurityStrategy, that.incomingTimestampSecurityStrategy) &&
        Objects.equals(decryptSecurityStrategy, that.decryptSecurityStrategy) &&
        Objects.equals(encryptSecurityStrategy, that.encryptSecurityStrategy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(signSecurityStrategy, verifySignatureSecurityStrategy, usernameTokenSecurityStrategy,
                        timestampSecurityStrategy, incomingTimestampSecurityStrategy, decryptSecurityStrategy,
                        encryptSecurityStrategy);
  }
}
