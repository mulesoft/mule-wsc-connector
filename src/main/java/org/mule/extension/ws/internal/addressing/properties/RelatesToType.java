/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.addressing.properties;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;

public class RelatesToType {

  private final String value;
  private final String relationShip;

  public RelatesToType(String value, String relationShip) {
    checkNotNull(value, "Value cannot be null");
    this.value = value;
    this.relationShip = relationShip;
  }

  public String getValue() {
    return value;
  }

  public Optional<String> getRelationShip() {
    return ofNullable(relationShip);
  }
}
