/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.security;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.soap.security.SecurityStrategy;
import org.mule.runtime.extension.api.soap.security.TimestampSecurityStrategy;

/**
 * Bundles the outgoing SOAP message that it's being built with a timestamp that carries the creation.
 *
 * @since 1.0
 */
public class WssTimestampSecurityStrategy implements SecurityStrategyAdapter {

  /**
   * The time difference between creation and expiry time in seconds. After this time the message is invalid.
   */
  @Parameter
  @Optional(defaultValue = "1")
  private long timeToLive;

  @Override
  public SecurityStrategy getSecurityStrategy() {
    return new TimestampSecurityStrategy(timeToLive);
  }
}
