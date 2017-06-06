/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api;

import static org.mule.runtime.core.api.util.collection.Collectors.toImmutableList;
import org.mule.extension.ws.internal.security.SecurityStrategyAdapter;
import org.mule.extension.ws.internal.security.WssDecryptSecurityStrategy;
import org.mule.extension.ws.internal.security.WssEncryptSecurityStrategy;
import org.mule.extension.ws.internal.security.WssSignSecurityStrategy;
import org.mule.extension.ws.internal.security.WssTimestampSecurityStrategy;
import org.mule.extension.ws.internal.security.WssUsernameTokenSecurityStrategy;
import org.mule.extension.ws.internal.security.WssVerifySignatureSecurityStrategy;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.soap.security.SecurityStrategy;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * This class serves as {@link ParameterGroup} for configuring Web Service Security.
 *
 * @since 1.0
 */
public class WebServiceSecurity {

  /**
   * a sign WSS configuration
   */
  @Parameter
  @Optional
  private WssSignSecurityStrategy signSecurityStrategy;

  /**
   * a verify signature WSS configuration
   */
  @Parameter
  @Optional
  private WssVerifySignatureSecurityStrategy verifySignatureSecurityStrategy;

  /**
   * a usernameToken WSS configuration
   */
  @Parameter
  @Optional
  private WssUsernameTokenSecurityStrategy usernameTokenSecurityStrategy;

  /**
   * a timestamp WSS configuration
   */
  @Parameter
  @Optional
  private WssTimestampSecurityStrategy timestampSecurityStrategy;

  /**
   * a decrypt WSS configuration
   */
  @Parameter
  @Optional
  private WssDecryptSecurityStrategy decryptSecurityStrategy;

  /**
   * an encrypt WSS configuration
   */
  @Parameter
  @Optional
  private WssEncryptSecurityStrategy encryptSecurityStrategy;

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
