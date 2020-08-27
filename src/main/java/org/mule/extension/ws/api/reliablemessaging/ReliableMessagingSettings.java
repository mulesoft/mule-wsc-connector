/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.reliablemessaging;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;

import org.mule.runtime.api.store.ObjectStore;
import org.mule.runtime.api.store.ObjectStoreManager;
import org.mule.runtime.api.store.ObjectStoreSettings;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;

import java.util.Objects;

/**
 * This class serves as {@link ParameterGroup} for configuring Web Service Reliable Messaging connection.
 *
 * @since 2.0
 */
public class ReliableMessagingSettings {

  private static final String RELIABLE_MESSAGING_TAB = "Reliable Messaging";
  private static final String DEFAULT_OS_NAME = "DEFAULT_WSRM_OS_NAME";
  private static final long DEFAULT_OS_TTL = MINUTES.toMillis(5);
  private static final ObjectStoreSettings DEFAULT_OS_SETTINGS =
      ObjectStoreSettings.builder().persistent(false).entryTtl(DEFAULT_OS_TTL).build();

  /**
   * The store of the WSRM sequence's data
   */
  @Parameter
  @Placement(tab = RELIABLE_MESSAGING_TAB, order = 6)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("Store")
  private ObjectStore wsrmStore;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    ReliableMessagingSettings that = (ReliableMessagingSettings) o;
    return Objects.equals(wsrmStore, that.wsrmStore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wsrmStore);
  }

  public ObjectStore getObjectStore() {
    return wsrmStore;
  }
}
