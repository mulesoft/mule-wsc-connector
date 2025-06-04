/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.security;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;

import org.mule.extension.ws.api.security.config.WssKeyStoreConfigurationAdapter;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.soap.api.security.SecurityStrategy;
import org.mule.soap.api.security.WssDecryptSecurityStrategy;
import org.mule.soap.api.security.stores.WssKeyStoreConfiguration;

import java.util.Objects;

/**
 * Decrypts an encrypted SOAP response, using the private key of the key-store in the provided TLS context.
 *
 * @since 1.0
 */
@Alias("wss-decrypt-security-strategy")
public class WssDecryptSecurityStrategyAdapter implements SecurityStrategyAdapter {

  public WssDecryptSecurityStrategyAdapter() {}

  /**
   * The keystore to use when decrypting the message.
   */
  @Parameter
  @Expression(NOT_SUPPORTED)
  private WssKeyStoreConfigurationAdapter keyStoreConfiguration;

  @Override
  public SecurityStrategy getSecurityStrategy() {
    return new WssDecryptSecurityStrategy(new WssKeyStoreConfiguration(keyStoreConfiguration.getAlias(),
                                                                       keyStoreConfiguration.getPassword(),
                                                                       keyStoreConfiguration.getStorePath(),
                                                                       keyStoreConfiguration.getKeyPassword(),
                                                                       keyStoreConfiguration.getType()));
  }

  public WssKeyStoreConfigurationAdapter getKeyStoreConfiguration() {
    return keyStoreConfiguration;
  }

  public void setKeyStoreConfiguration(WssKeyStoreConfigurationAdapter keyStoreConfiguration) {
    this.keyStoreConfiguration = keyStoreConfiguration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    WssDecryptSecurityStrategyAdapter that = (WssDecryptSecurityStrategyAdapter) o;
    return Objects.equals(keyStoreConfiguration, that.keyStoreConfiguration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(keyStoreConfiguration);
  }
}
