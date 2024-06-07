/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.security.Constants;

/**
 * Lists the order that can be applied to outgoing security strategies.
 * The XML security tags will appear in the reverse order to the list described here,
 * since the underlying library applies the actions and places the corresponding tag above those that already exist
 * **/
public enum SecurityHeadersOrderConstants {
  Timestamp_At_End("Timestamp UsernameToken Signature Encrypt "), Timestamp_At_Start(
      "Encrypt Signature UsernameToken Timestamp"),;

  private String value;

  SecurityHeadersOrderConstants(String value) {
    this.value = value;
  }

  public String toString() {
    return this.value;
  }
}
