/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.addressing;

/**
 * The possible WSA versions of a SOAP service.
 *
 * @since 2.0
 */
public enum AddressingVersion {

  WSA200508("http://www.w3.org/2005/08/addressing"), WSA200408("http://schemas.xmlsoap.org/ws/2004/08/addressing");

  private final String namespaceUri;

  AddressingVersion(String namespaceUri) {
    this.namespaceUri = namespaceUri;
  }

  public String getNamespaceUri() {
    return namespaceUri;
  }
}
