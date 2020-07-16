/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.reliablemessaging;

import org.mule.extension.ws.api.addressing.AddressingVersion;
import org.mule.extension.ws.internal.WebServiceConsumer;
import org.mule.extension.ws.internal.reliablemessaging.value.ReliableMessagingVersionValueProvider;
import org.mule.runtime.api.lifecycle.Initialisable;
import org.mule.runtime.api.lifecycle.InitialisationException;
import org.mule.runtime.api.store.ObjectStore;
import org.mule.runtime.api.store.ObjectStoreManager;
import org.mule.runtime.api.store.ObjectStoreSettings;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.reference.ConfigReference;
import org.mule.runtime.extension.api.annotation.values.OfValues;
import org.mule.runtime.http.api.server.HttpServer;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.mule.extension.ws.internal.util.PathUtils.getFullPath;
import static org.mule.extension.ws.internal.util.PathUtils.sanitizePathWithStartSlash;
import static org.mule.runtime.api.i18n.I18nMessageFactory.createStaticMessage;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;

/**
 * This class serves as {@link ParameterGroup} for configuring Web Service Reliable Messaging.
 *
 * @since 2.0
 */
public class ReliableMessagingConfiguration {

  private static final String RELIABLE_MESSAGING_TAB = "Reliable Messaging";
  private static final String DEFAULT_OS_NAME = "DEFAULT_WSRM_OS_NAME";
  private static final long DEFAULT_OS_TTL = MINUTES.toMillis(5);
  private static final ObjectStoreSettings DEFAULT_OS_SETTINGS =
      ObjectStoreSettings.builder().persistent(false).entryTtl(DEFAULT_OS_TTL).build();

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

  /**
   * Endpoint reference where it's wanted to receive ack messages.
   */
  @Parameter
  @Placement(tab = RELIABLE_MESSAGING_TAB, order = 4)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("Acks To")
  private String wsrmAcksTo;

  @ConfigReference(namespace = "HTTP", name = "LISTENER_CONFIG")
  @Parameter
  @Placement(tab = RELIABLE_MESSAGING_TAB, order = 5)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("HTTP Listener")
  private String wsrmHttpListenerConfig;

  /**
   * The store of the WSRM sequence's data
   */
  @Parameter
  @Placement(tab = RELIABLE_MESSAGING_TAB, order = 6)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("Store")
  private ObjectStore wsrmStore;

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
        Objects.equals(wsrmHttpListenerConfig, that.wsrmHttpListenerConfig) &&
        Objects.equals(wsrmAcksTo, that.wsrmAcksTo) &&
        wsrmSequenceTtlTimeUnit == that.wsrmSequenceTtlTimeUnit &&
        Objects.equals(wsrmStore, that.wsrmStore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wsrmVersion, wsrmHttpListenerConfig, wsrmAcksTo, wsrmSequenceTtl, wsrmSequenceTtlTimeUnit, wsrmStore);
  }

  public void doInitialise(WebServiceConsumer connector) throws InitialisationException {
    AddressingVersion wsaVersion = connector.getAddressing().getWsaVersion();
    if (wsaVersion == null) {
      throw new InitialisationException(createStaticMessage("WSA version cannot be null."), connector);
    }

    if (wsrmVersion == null) {
      version = Arrays.stream(ReliableMessagingVersion.values()).filter(v -> v.getAddressingVersion().equals(wsaVersion))
          .findFirst().orElseThrow(() -> new InitialisationException(
                                                                     createStaticMessage("There is no WSRM version related to the selected WSA version [%s].",
                                                                                         wsaVersion.name()),
                                                                     connector));
    } else {
      ReliableMessagingVersion rmVersion = Arrays.stream(ReliableMessagingVersion.values())
          .filter(v -> v.name().equals(wsrmVersion)).findFirst().orElseThrow(() -> new InitialisationException(
                                                                                                               createStaticMessage("Invalid WSRM version configured [%s].",
                                                                                                                                   wsrmVersion),
                                                                                                               connector));
      if (rmVersion.getAddressingVersion() != wsaVersion) {
        throw new InitialisationException(
                                          createStaticMessage("Invalid WSRM version configured [%s] for the selected WSA version [%s].",
                                                              wsrmVersion, wsaVersion.name()),
                                          connector);
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

  public java.util.Optional<String> getAckTo(HttpServer server) {
    if (wsrmAcksTo == null) {
      return empty();
    }
    return of(getFullPath(sanitizePathWithStartSlash(wsrmAcksTo), "/").getAbsolutePath(server));
  }

  public ObjectStore getObjectStore(ObjectStoreManager manager) {
    if (wsrmStore == null) {
      return manager.getOrCreateObjectStore(DEFAULT_OS_NAME, DEFAULT_OS_SETTINGS);
    }
    return wsrmStore;
  }
}
