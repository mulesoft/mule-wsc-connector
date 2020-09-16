/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.reliablemessaging;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.extension.api.annotation.param.ParameterGroup.ADVANCED;

import org.mule.runtime.api.store.ObjectStore;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.annotation.param.reference.ObjectStoreReference;

import java.util.Objects;

/**
 * This class serves as {@link ParameterGroup} for configuring Web Service Reliable Messaging connection.
 *
 * @since 1.7
 */
public class ReliableMessagingConnectionSettings {

  /**
   * The store of the WSRM sequence's data
   */
  @Parameter
  @Placement(tab = ADVANCED, order = 6)
  @Optional
  @Expression(NOT_SUPPORTED)
  @ObjectStoreReference
  @DisplayName("Store")
  @Summary("The WS reliable messaging store used to persists sequence's data.")
  private ObjectStore wsrmStore;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    ReliableMessagingConnectionSettings that = (ReliableMessagingConnectionSettings) o;
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
