/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.extension.ws.api.security.Constants;

/**
 * List of the algorithm listed on the <a href="https://www.w3.org/TR/xmlenc-core1/</a> specification and implemented by
 * Apache WSS4J to be used for the digest algorithm to be used with the key transport algorithm for encryption.
 *
 * @since 1.3.0
 */
public enum EncryptionDigestAlgorithmConstants {

  SHA1("http://www.w3.org/2000/09/xmldsig#sha1"), SHA256("http://www.w3.org/2001/04/xmlenc#sha256"), SHA384(
      "http://www.w3.org/2001/04/xmldsig-more#sha384"), SHA512("http://www.w3.org/2001/04/xmlenc#sha512");

  private String value;

  EncryptionDigestAlgorithmConstants(String value) {
    this.value = value;
  }

  public String toString() {
    return this.value;
  }
}
