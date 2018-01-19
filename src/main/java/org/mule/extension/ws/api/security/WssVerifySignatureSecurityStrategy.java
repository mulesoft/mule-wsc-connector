/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.security;


import org.mule.extension.ws.api.security.config.WssTrustStoreConfigurationAdapter;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.soap.security.SecurityStrategy;
import org.mule.runtime.extension.api.soap.security.VerifySignatureSecurityStrategy;
import org.mule.runtime.extension.api.soap.security.config.WssTrustStoreConfiguration;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;


/**
 * Verifies the signature of a SOAP response, using certificates of the trust-store in the provided TLS context.
 *
 * @since 1.0
 */
public class WssVerifySignatureSecurityStrategy implements SecurityStrategyAdapter {

  /**
   * The truststore to use to verify the signature.
   */
  @Parameter
  @Optional
  @Expression(NOT_SUPPORTED)
  private WssTrustStoreConfigurationAdapter trustStoreConfiguration;

  @Override
  public SecurityStrategy getSecurityStrategy() {
    WssTrustStoreConfiguration trustStoreConfig =
        new WssTrustStoreConfiguration(trustStoreConfiguration.getStorePath(), trustStoreConfiguration.getPassword(),
                                       trustStoreConfiguration.getType());
    return new VerifySignatureSecurityStrategy(trustStoreConfig);
  }
}
