/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.reliablemessaging;

import org.mule.extension.ws.api.addressing.AddressingVersion;
import org.mule.extension.ws.internal.reliablemessaging.value.ReliableMessagingVersionValueProvider;
import org.mule.runtime.api.lifecycle.Initialisable;
import org.mule.runtime.api.lifecycle.InitialisationException;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.values.OfValues;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.mule.runtime.api.i18n.I18nMessageFactory.createStaticMessage;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;

/**
 * This class serves as {@link ParameterGroup} for configuring Web Service Reliable Messaging.
 *
 * @since 1.7
 */
public class ReliableMessagingConfiguration {

  private static final String RELIABLE_MESSAGING_TAB = "Reliable Messaging";

  /**
   * WS-ReliableMessaging version.
   */
  @Parameter
  @Placement(tab = RELIABLE_MESSAGING_TAB, order = 1)
  @OfValues(value = ReliableMessagingVersionValueProvider.class, open = false)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("Version")
  private String wsrmVersion;

  /**
   * The time before the sequence expires.
   */
  @Parameter
  @Placement(tab = RELIABLE_MESSAGING_TAB, order = 2)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("Sequence TTL")
  private int wsrmSequenceTtl;

  /**
   * The time unit for the sequence ttl.
   */
  @Parameter
  @Placement(tab = RELIABLE_MESSAGING_TAB, order = 3)
  @Optional(defaultValue = "SECONDS")
  @Expression(NOT_SUPPORTED)
  @DisplayName("Sequence TTL Time Unit")
  private TimeUnit wsrmSequenceTtlTimeUnit;

  private ReliableMessagingVersion version;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    ReliableMessagingConfiguration that = (ReliableMessagingConfiguration) o;
    return wsrmSequenceTtl == that.wsrmSequenceTtl &&
        Objects.equals(wsrmVersion, that.wsrmVersion) &&
        wsrmSequenceTtlTimeUnit == that.wsrmSequenceTtlTimeUnit;
  }

  @Override
  public int hashCode() {
    return Objects.hash(wsrmVersion, wsrmSequenceTtl, wsrmSequenceTtlTimeUnit);
  }

  public void doInitialise(AddressingVersion wsaVersion, Initialisable initialisable) throws InitialisationException {
    if (wsaVersion == null) {
      throw new InitialisationException(createStaticMessage("WSA version cannot be null."), initialisable);
    }

    if (wsrmVersion == null) {
      version = Arrays.stream(ReliableMessagingVersion.values()).filter(v -> v.getAddressingVersion().equals(wsaVersion))
          .findFirst().orElseThrow(() -> new InitialisationException(
                                                                     createStaticMessage("There is no WSRM version related to the selected WSA version [%s].",
                                                                                         wsaVersion.name()),
                                                                     initialisable));
    } else {
      ReliableMessagingVersion rmVersion = Arrays.stream(ReliableMessagingVersion.values())
          .filter(v -> v.name().equals(wsrmVersion)).findFirst().orElseThrow(() -> new InitialisationException(
                                                                                                               createStaticMessage("Invalid WSRM version configured [%s].",
                                                                                                                                   wsrmVersion),
                                                                                                               initialisable));
      if (rmVersion.getAddressingVersion() != wsaVersion) {
        throw new InitialisationException(
                                          createStaticMessage("Invalid WSRM version configured [%s] for the selected WSA version [%s].",
                                                              wsrmVersion, wsaVersion.name()),
                                          initialisable);
      }

      version = rmVersion;
    }
  }

  public ReliableMessagingVersion getVersion() {
    return version;
  }

  public Long getSequenceTtl() {
    return this.wsrmSequenceTtlTimeUnit.toMillis(wsrmSequenceTtl);
  }
}
