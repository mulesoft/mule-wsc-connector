/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal;

import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.extension.api.annotation.param.display.Placement.ADVANCED_TAB;

/**
 * Component that customizes the SOAP request's message.
 *
 * @since 1.6
 */
public class SoapMessageCustomizations {

  /**
   * If true, the XML Prolog statement will be appended to the request's body.
   */
  @Parameter
  @Placement(tab = ADVANCED_TAB)
  @Optional(defaultValue = "false")
  @Expression(NOT_SUPPORTED)
  @DisplayName("Force XML Prolog into body")
  @Summary("The XML Prolog statement will be appended to the request's body")
  private boolean forceXMLProlog;

  public boolean isForceXMLProlog() {
    return forceXMLProlog;
  }

}
