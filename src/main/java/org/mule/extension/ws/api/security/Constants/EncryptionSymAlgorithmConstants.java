/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.security.Constants;

/**
 * List of the algorithm listed on the <a href="https://www.w3.org/TR/xmlenc-core1/</a> specification and implemented by Apache
 * WSS4J to be used for symmetric encryption.
 *
 * @since 1.3.0
 */
public enum EncryptionSymAlgorithmConstants {

  TRIPLE_DES("http://www.w3.org/2001/04/xmlenc#tripledes-cbc"), AES_128("http://www.w3.org/2001/04/xmlenc#aes128-cbc"), AES_256(
      "http://www.w3.org/2001/04/xmlenc#aes256-cbc"), AES_192("http://www.w3.org/2001/04/xmlenc#aes192-cbc"), AES_128_GCM(
          "http://www.w3.org/2009/xmlenc11#aes128-gcm"), AES_192_GCM(
              "http://www.w3.org/2009/xmlenc11#aes192-gcm"), AES_256_GCM("http://www.w3.org/2009/xmlenc11#aes256-gcm");

  private String value;

  EncryptionSymAlgorithmConstants(String value) {
    this.value = value;
  }

  public String toString() {
    return this.value;
  }
}
