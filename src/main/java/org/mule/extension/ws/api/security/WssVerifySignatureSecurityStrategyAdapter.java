/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.security;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;

import org.mule.extension.ws.api.security.config.WssTrustStoreConfigurationAdapter;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.soap.api.security.SecurityStrategy;
import org.mule.soap.api.security.WssVerifySignatureSecurityStrategy;
import org.mule.soap.api.security.stores.WssTrustStoreConfiguration;

import java.util.Objects;

/**
 * Verifies the signature of a SOAP response, using certificates of the trust-store in the provided TLS context.
 *
 * @since 1.0
 */
public class WssVerifySignatureSecurityStrategyAdapter implements SecurityStrategyAdapter {

  /**
   * The truststore to use to verify the signature.
   */
  @Parameter
  @Optional
  @Expression(NOT_SUPPORTED)
  private WssTrustStoreConfigurationAdapter trustStoreConfiguration;

  public WssVerifySignatureSecurityStrategyAdapter() {}

  @Override
  public SecurityStrategy getSecurityStrategy() {
    return new WssVerifySignatureSecurityStrategy(new WssTrustStoreConfiguration(trustStoreConfiguration.getStorePath(),
                                                                                 trustStoreConfiguration.getPassword(),
                                                                                 trustStoreConfiguration.getType()));
  }

  public WssTrustStoreConfigurationAdapter getTrustStoreConfiguration() {
    return trustStoreConfiguration;
  }

  public void setTrustStoreConfiguration(WssTrustStoreConfigurationAdapter trustStoreConfiguration) {
    this.trustStoreConfiguration = trustStoreConfiguration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    WssVerifySignatureSecurityStrategyAdapter that = (WssVerifySignatureSecurityStrategyAdapter) o;
    return Objects.equals(trustStoreConfiguration, that.trustStoreConfiguration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(trustStoreConfiguration);
  }
}
