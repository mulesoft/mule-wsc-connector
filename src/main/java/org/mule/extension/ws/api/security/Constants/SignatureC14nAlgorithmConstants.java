/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.security.Constants;

/**
 * List of the algorithm listed on the <a href="https://www.w3.org/TR/xmldsig-core1/</a> specification and implemented by Apache
 * WSS4J to be used for the signature c14n (canonicalization).
 *
 * @since 1.3.0
 */
public enum SignatureC14nAlgorithmConstants {

  CanonicalXML_1_0("http://www.w3.org/TR/2001/REC-xml-c14n-20010315"), CanonicalXML_1_1(
      "http://www.w3.org/2006/12/xml-c14n11"), ExclusiveXMLCanonicalization_1_0("http://www.w3.org/2001/10/xml-exc-c14n#");


  private String value;

  SignatureC14nAlgorithmConstants(String value) {
    this.value = value;
  }

  public String toString() {
    return this.value;
  }
}
