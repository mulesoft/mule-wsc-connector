/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.addressing.properties;

import static com.google.common.base.Preconditions.checkNotNull;

public class URIType {

  private final String value;

  public URIType(String value) {
    checkNotNull(value, "Value cannot be null.");
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
