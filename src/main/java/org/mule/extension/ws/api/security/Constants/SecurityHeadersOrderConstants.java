/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.security.Constants;

/**
 * Lists the order to apply to outgoing security strategies.
 * The XML security tags will appear in the reverse order of the list described here.
 * This is because the underlying library executes the actions and places the corresponding tag above the previously existing ones.
 * **/
public enum SecurityHeadersOrderConstants {
  TimestampAtEnd("Timestamp UsernameToken Signature Encrypt "), TimestampAtStart(
      "Encrypt Signature UsernameToken Timestamp"),;

  private String value;

  SecurityHeadersOrderConstants(String value) {
    this.value = value;
  }

  public String toString() {
    return this.value;
  }
}
