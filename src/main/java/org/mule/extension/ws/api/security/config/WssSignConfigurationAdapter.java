/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.security.config;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;

import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.soap.api.security.configuration.SignatureAlgorithmConstants;
import org.mule.soap.api.security.configuration.SignatureC14nAlgorithmConstants;
import org.mule.soap.api.security.configuration.SignatureDigestAlgorithmConstants;

/**
 * Group which holds the configuration regarding signing algorithms used on sign security strategy.
 *
 * @since 1.3.0
 */
public class WssSignConfigurationAdapter {

  @Parameter
  @Optional
  @Expression(NOT_SUPPORTED)
  @Summary("The signature algorithm to use. The default is set by the data in the certificate.")
  private SignatureAlgorithmConstants signatureAlgorithm;

  @Parameter
  @Optional(defaultValue = "SHA1")
  @Expression(NOT_SUPPORTED)
  @Summary("The signature digest algorithm to use.")
  private SignatureDigestAlgorithmConstants signatureDigestAlgorithm;

  @Parameter
  @Optional(defaultValue = "ExclusiveXMLCanonicalization_1_0")
  @DisplayName("Signature c14n algorithm")
  @Expression(NOT_SUPPORTED)
  @Summary("Defines which signature c14n (canonicalization) algorithm to use.")
  private SignatureC14nAlgorithmConstants signatureC14nAlgorithm;

  /**
   * @return The signature algorithm.
   */
  public SignatureAlgorithmConstants getSignatureAlgorithm() {
    return signatureAlgorithm;
  }

  /**
   * @return The signature digest algorithm.
   */
  public SignatureDigestAlgorithmConstants getSignatureDigestAlgorithm() {
    return signatureDigestAlgorithm;
  }

  /**
   * @return The signature c14n (canonicalization) algorithm.
   */
  public SignatureC14nAlgorithmConstants getSignatureC14nAlgorithm() {
    return signatureC14nAlgorithm;
  }

}
