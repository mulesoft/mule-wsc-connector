/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api;

import org.mule.extension.ws.api.security.SecurityStrategyAdapter;
import org.mule.extension.ws.api.security.WssDecryptSecurityStrategyAdapter;
import org.mule.extension.ws.api.security.WssEncryptSecurityStrategyAdapter;
import org.mule.extension.ws.api.security.WssSignSecurityStrategyAdapter;
import org.mule.extension.ws.api.security.WssTimestampSecurityStrategyAdapter;
import org.mule.extension.ws.api.security.WssUsernameTokenSecurityStrategyAdapter;
import org.mule.extension.ws.api.security.WssVerifySignatureSecurityStrategyAdapter;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.soap.api.security.SecurityStrategy;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.util.collection.Collectors.toImmutableList;

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
  @Placement(tab = SECURITY_TAB)
  @Optional
  private WssSignSecurityStrategyAdapter signSecurityStrategy;

  /**
   * a verify signature WSS configuration
   */
  @Parameter
  @Expression(NOT_SUPPORTED)
  @Placement(tab = SECURITY_TAB)
  @Optional
  private WssVerifySignatureSecurityStrategyAdapter verifySignatureSecurityStrategy;

  /**
   * a usernameToken WSS configuration
   */
  @Parameter
  @Expression(NOT_SUPPORTED)
  @Placement(tab = SECURITY_TAB)
  @Optional
  private WssUsernameTokenSecurityStrategyAdapter usernameTokenSecurityStrategy;

  /**
   * a timestamp WSS configuration
   */
  @Parameter
  @Expression(NOT_SUPPORTED)
  @Placement(tab = SECURITY_TAB)
  @Optional
  private WssTimestampSecurityStrategyAdapter timestampSecurityStrategy;

  /**
   * a decrypt WSS configuration
   */
  @Parameter
  @Expression(NOT_SUPPORTED)
  @Placement(tab = SECURITY_TAB)
  @Optional
  private WssDecryptSecurityStrategyAdapter decryptSecurityStrategy;

  /**
   * an encrypt WSS configuration
   */
  @Parameter
  @Expression(NOT_SUPPORTED)
  @Placement(tab = SECURITY_TAB)
  @Optional
  private WssEncryptSecurityStrategyAdapter encryptSecurityStrategy;

  public List<SecurityStrategy> strategiesList() {
    return Stream.of(signSecurityStrategy,
                     verifySignatureSecurityStrategy,
                     usernameTokenSecurityStrategy,
                     timestampSecurityStrategy,
                     decryptSecurityStrategy,
                     encryptSecurityStrategy)
        .filter(Objects::nonNull)
        .map(SecurityStrategyAdapter::getSecurityStrategy)
        .collect(toImmutableList());
  }
}
