/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.security.Constants;

/**
 * key identifier type to use for signature.
 *
 * @since 1.3.0
 */
public enum SignatureKeyIdentifierConstants {

  ISSUER_SERIAL("IssuerSerial"), DIRECT_REFERENCE("DirectReference"), X509_KEY_IDENTIFIER("X509KeyIdentifier"), THUMBPRINT(
      "Thumbprint"), SKI_KEY_IDENTIFIER("SKIKeyIdentifier"), KEY_VALUE("KeyValue");

  private String value;

  SignatureKeyIdentifierConstants(String value) {
    this.value = value;
  }

  public String toString() {
    return this.value;
  }
}
