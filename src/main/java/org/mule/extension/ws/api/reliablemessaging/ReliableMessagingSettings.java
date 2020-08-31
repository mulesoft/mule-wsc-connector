/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.reliablemessaging;

import org.mule.extension.ws.internal.ConsumeOperation;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;

import static org.mule.runtime.api.meta.ExpressionSupport.REQUIRED;
import static org.mule.runtime.extension.api.annotation.param.display.Placement.ADVANCED_TAB;

/**
 * This class serves as {@link ParameterGroup} for using Web Service Reliable Messaging at {@link ConsumeOperation}.
 *
 * @since 2.0
 */
public class ReliableMessagingSettings {

  @Parameter
  @Placement(tab = ADVANCED_TAB)
  @Optional
  @Expression(REQUIRED)
  @DisplayName("Sequence identifier")
  private String wsrmSequence;

  public String getSequence() {
    return wsrmSequence;
  }
}
