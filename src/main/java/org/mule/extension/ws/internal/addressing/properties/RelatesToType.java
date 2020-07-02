/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.addressing.properties;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

/**
 * Model of Relates To
 *
 * @since 2.0
 */
public class RelatesToType {

  private final String value;
  private final String relationship;

  public RelatesToType(String value, String relationship) {
    this.value = requireNonNull(value, "Value cannot be null");
    this.relationship = relationship;
  }

  public String getValue() {
    return value;
  }

  public Optional<String> getRelationship() {
    return ofNullable(relationship);
  }
}
