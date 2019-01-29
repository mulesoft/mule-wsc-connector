/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.security.config;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;

import org.mule.extension.ws.api.security.Constants.SignEncodeConstants;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;

/**
 * Group which holds the data that identifies a part of a SOAP message to be signed or encrypted.
 *
 * @since 1.3.0
 */
@Alias("wss-part")
public class WssPartAdapter {

  @Parameter
  @Optional(defaultValue = "CONTENT")
  @Expression(NOT_SUPPORTED)
  private SignEncodeConstants encode;

  @Parameter
  @Expression(NOT_SUPPORTED)
  private String namespace;

  @Parameter
  @Expression(NOT_SUPPORTED)
  private String localname;

  /**
   * @return The encode.
   */
  public SignEncodeConstants getEncode() {
    return encode;
  }

  /**
   * @return The namespace.
   */
  public String getNamespace() {
    return namespace;
  }

  /**
   * @return The localname.
   */
  public String getLocalname() {
    return localname;
  }

}
