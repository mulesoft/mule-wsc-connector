/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.security.Constants;

/**
 * List of the algorithm listed on the <a href="https://www.w3.org/TR/xmldsig-core1/</a> specification and implemented by Apache
 * WSS4J to be used to sign.
 *
 * @since 1.3.0
 */
public enum SignatureAlgorithmConstants {

  RSAwithSHA256("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256"), ECDSAwithSHA256(
      "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha256"), DSAwithSHA1("http://www.w3.org/2000/09/xmldsig#dsa-sha1"),

  RSAwithSHA1("http://www.w3.org/2000/09/xmldsig#rsa-sha1"),

  RSAwithSHA224("http://www.w3.org/2001/04/xmldsig-more#rsa-sha224"), RSAwithSHA384(
      "http://www.w3.org/2001/04/xmldsig-more#rsa-sha384"), RSAwithSHA512(
          "http://www.w3.org/2001/04/xmldsig-more#rsa-sha512"), ECDSAwithSHA1(
              "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha1"), ECDSAwithSHA224(
                  "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha224"), ECDSAwithSHA384(
                      "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha384"), ECDSAwithSHA512(
                          "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha512"), DSAwithSHA256(
                              "http://www.w3.org/2009/xmldsig11#dsa-sha256");

  private String value;

  SignatureAlgorithmConstants(String value) {
    this.value = value;
  }

  public String toString() {
    return this.value;
  }

}
