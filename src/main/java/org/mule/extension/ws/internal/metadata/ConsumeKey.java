/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.metadata;

import org.mule.extension.ws.internal.ConsumeOperation;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.metadata.MetadataKeyId;
import org.mule.runtime.extension.api.annotation.metadata.MetadataKeyPart;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.extension.api.annotation.param.display.Placement.ADVANCED_TAB;

/**
 * The {@link MetadataKeyId} for the {@link ConsumeOperation}
 *
 * @since 2.0
 */
public class ConsumeKey {

  /**
   * The name of the web service operation that aims to invoke
   */
  @Parameter
  @MetadataKeyPart(order = 1)
  private String operation;

  /**
   * The address of the intended receiver for replies to this message.
   */
  @Parameter
  @MetadataKeyPart(order = 2, providedByKeyResolver = false)
  @Placement(tab = ADVANCED_TAB, order = 1)
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName("Reply to")
  @Summary("The address of the intended receiver for replies to this message.")
  private String wsaReplyTo;

  public String getOperation() {
    return operation;
  }

  public String getReplyTo() {
    return wsaReplyTo;
  }
}
