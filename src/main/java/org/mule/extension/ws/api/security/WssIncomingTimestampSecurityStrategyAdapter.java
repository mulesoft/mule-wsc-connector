/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.security;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.soap.api.security.SecurityStrategy;
import org.mule.soap.api.security.WssIncomingTimestampSecurityStrategy;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Verifies the timestamp of an incoming SOAP message.
 * <p>
 * This should be used when an incoming soap message is encrypted, signed, etc, and also includes a timestamp that express the
 * creation and expiration of the security semantic of the message.
 *
 * @see <a href="http://docs.oasis-open.org/wss/v1.1/wss-v1.1-spec-errata-os-SOAPMessageSecurity.htm#_Toc118717167">Security
 *      Timestamps</a>
 *
 * @since 1.4.0
 */
public class WssIncomingTimestampSecurityStrategyAdapter implements SecurityStrategyAdapter {

  /**
   * Specifies the time in the future within which the Created time of an incoming Timestamp is valid. The default value is "60",
   * to avoid problems where clocks are slightly askew. To reject all future-created Timestamps, set this value to "0".
   */
  @Parameter
  @Placement(order = 0)
  @Summary("Time in the future within which the Created time of an incoming Timestamp is valid")
  @Optional(defaultValue = "60")
  private long futureTimeToLive;

  /**
   * A {@link TimeUnit} which qualifies the {@link #futureTimeToLive} parameter.
   * <p>
   * Defaults to {@code SECONDS}
   */
  @Parameter
  @Placement(order = 1)
  @Optional(defaultValue = "SECONDS")
  @Summary("Time unit to be used in the futureTimeToLive parameter")
  private TimeUnit futureTimeToLiveUnit;

  public WssIncomingTimestampSecurityStrategyAdapter() {}

  @Override
  public SecurityStrategy getSecurityStrategy() {
    long futureTimeToLiveInSeconds = futureTimeToLiveUnit.toSeconds(futureTimeToLive);
    return new WssIncomingTimestampSecurityStrategy(futureTimeToLiveInSeconds);
  }

  public long getFutureTimeToLive() {
    return futureTimeToLive;
  }

  public void setFutureTimeToLive(long futureTimeToLive) {
    this.futureTimeToLive = futureTimeToLive;
  }

  public TimeUnit getFutureTimeToLiveUnit() {
    return futureTimeToLiveUnit;
  }

  public void setFutureTimeToLiveUnit(TimeUnit futureTimeToLiveUnit) {
    this.futureTimeToLiveUnit = futureTimeToLiveUnit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    WssIncomingTimestampSecurityStrategyAdapter that = (WssIncomingTimestampSecurityStrategyAdapter) o;
    return futureTimeToLive == that.futureTimeToLive &&
        futureTimeToLiveUnit == that.futureTimeToLiveUnit;
  }

  @Override
  public int hashCode() {
    return Objects.hash(futureTimeToLive, futureTimeToLiveUnit);
  }
}

