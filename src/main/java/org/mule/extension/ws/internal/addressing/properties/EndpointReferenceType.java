/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.addressing.properties;

import static java.util.Objects.requireNonNull;

/**
 * Model of Endpoint Reference
 *
 * @since 2.0
 */
public class EndpointReferenceType {

  private final URIType address;

  public EndpointReferenceType(URIType address) {
    requireNonNull(address, "Address cannot be null");
    this.address = address;
  }

  public URIType getAddress() {
    return address;
  }
}
