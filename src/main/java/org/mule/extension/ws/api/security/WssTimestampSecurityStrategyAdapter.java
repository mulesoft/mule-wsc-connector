/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.security;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.soap.api.security.SecurityStrategy;
import org.mule.soap.api.security.WssTimestampSecurityStrategy;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Bundles the outgoing SOAP message that it's being built with a timestamp that carries the creation.
 *
 * @since 1.0
 */
public class WssTimestampSecurityStrategyAdapter implements SecurityStrategyAdapter {

  /**
   * The time difference between creation and expiry time in the time unit specified in {@code timeToLiveUnit}.
   * After this time the message is invalid.
   * <p>
   * This parameter values must be greater or equal to 1 second (or similar in other unit).
   * Values lower than 1 second will end up in 1 second timestamp.
   */
  @Parameter
  @Summary("The expiration time in the time unit specified. This value converted to seconds must be greater or equal to 1 second")
  @Optional(defaultValue = "60")
  private long timeToLive;

  /**
   * A {@link TimeUnit} which qualifies the {@link #timeToLive} parameter.
   * <p>
   * Defaults to {@code SECONDS}
   */
  @Parameter
  @Optional(defaultValue = "SECONDS")
  @Summary("Time unit to be used in the timeToLive parameter")
  private TimeUnit timeToLiveUnit;

  @Override
  public SecurityStrategy getSecurityStrategy() {
    long seconds = timeToLiveUnit.toSeconds(timeToLive);
    return new WssTimestampSecurityStrategy(seconds > 0 ? seconds : 1);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    WssTimestampSecurityStrategyAdapter that = (WssTimestampSecurityStrategyAdapter) o;
    return timeToLive == that.timeToLive &&
        timeToLiveUnit == that.timeToLiveUnit;
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeToLive, timeToLiveUnit);
  }
}
