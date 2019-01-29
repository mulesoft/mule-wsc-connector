/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.security.Constants;

/**
 * Constant that indicates whether to sign an Element or a Content.
 * <p>
 * For more information regarding the difference between Element and Content refer to the
 * <a href="https://www.w3.org/TR/xmldsig-core1/">W3C XML Encryption specification</a>
 *
 * @since 1.3.0
 */
public enum SignEncodeConstants {

  ELEMENT("Element"), CONTENT("Content");

  private String value;

  SignEncodeConstants(String value) {
    this.value = value;
  }

  public String toString() {
    return this.value;
  }
}
