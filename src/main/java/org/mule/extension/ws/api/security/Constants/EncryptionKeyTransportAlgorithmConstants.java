/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.extension.ws.api.security.Constants;

/**
 * List of the algorithm listed on the <a href="https://www.w3.org/TR/xmlenc-core1/</a> specification and implemented by Apache
 * WSS4J to be used to encrypt the symmetric key
 */
public enum EncryptionKeyTransportAlgorithmConstants {

  KEYTRANSPORT_RSA15("http://www.w3.org/2001/04/xmlenc#rsa-1_5"), KEYTRANSPORT_RSAOAEP(
      "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p"), KEYTRANSPORT_RSAOAEP_XENC11(
          "http://www.w3.org/2009/xmlenc11#rsa-oaep");

  private String value;

  EncryptionKeyTransportAlgorithmConstants(String value) {
    this.value = value;
  }

  public String toString() {
    return this.value;
  }
}
