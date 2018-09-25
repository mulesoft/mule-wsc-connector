/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.security;

import org.mule.extension.ws.api.security.config.WssKeyStoreConfigurationAdapter;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.soap.api.security.SecurityStrategy;
import org.mule.soap.api.security.WssSignSecurityStrategy;
import org.mule.soap.api.security.stores.WssKeyStoreConfiguration;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;

/**
 * Signs the SOAP request that is being sent, using the private key of the key-store in the provided TLS context.
 *
 * @since 1.0
 */
public class WssSignSecurityStrategyAdapter implements SecurityStrategyAdapter {

  /**
   * The keystore to use when signing the message.
   */
  @Parameter
  @Expression(NOT_SUPPORTED)
  private WssKeyStoreConfigurationAdapter keyStoreConfiguration;

  @Override
  public SecurityStrategy getSecurityStrategy() {
    return new WssSignSecurityStrategy(new WssKeyStoreConfiguration(keyStoreConfiguration.getAlias(),
                                                                    keyStoreConfiguration.getPassword(),
                                                                    keyStoreConfiguration.getStorePath(),
                                                                    keyStoreConfiguration.getKeyPassword(),
                                                                    keyStoreConfiguration.getType()));
  }
}
