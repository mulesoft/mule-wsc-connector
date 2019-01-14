/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.security;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;

import org.mule.extension.ws.api.security.config.WssKeyStoreConfigurationAdapter;
import org.mule.extension.ws.api.security.config.WssSignConfigurationAdapter;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.NullSafe;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.soap.api.security.SecurityStrategy;
import org.mule.soap.api.security.WssSignSecurityStrategy;
import org.mule.soap.api.security.configuration.WssSignConfiguration;
import org.mule.soap.api.security.stores.WssKeyStoreConfiguration;

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
  @Placement(order = 0)
  @Expression(NOT_SUPPORTED)
  private WssKeyStoreConfigurationAdapter keyStoreConfiguration;

  /**
   * The algorithms to use on the signing.
   */
  @Parameter
  @Optional
  @NullSafe
  @Placement(order = 1)
  @DisplayName("Signing algorithms configuration")
  @Expression(NOT_SUPPORTED)
  private WssSignConfigurationAdapter wssSignConfigurationAdapter;

  @Override
  public SecurityStrategy getSecurityStrategy() {

    WssKeyStoreConfiguration wssKeyStoreConfiguration = new WssKeyStoreConfiguration(keyStoreConfiguration.getAlias(),
                                                                                     keyStoreConfiguration.getPassword(),
                                                                                     keyStoreConfiguration.getStorePath(),
                                                                                     keyStoreConfiguration.getKeyPassword(),
                                                                                     keyStoreConfiguration.getType());

    String signatureAlgorithm = wssSignConfigurationAdapter.getSignatureAlgorithm() != null
        ? wssSignConfigurationAdapter.getSignatureAlgorithm().toString() : null;

    WssSignConfiguration wssSignConfiguration =
        new WssSignConfiguration(wssSignConfigurationAdapter.getSignatureKeyIdentifier().toString(),
                                 signatureAlgorithm,
                                 wssSignConfigurationAdapter.getSignatureDigestAlgorithm().toString(),
                                 wssSignConfigurationAdapter.getSignatureC14nAlgorithm().toString());


    return new WssSignSecurityStrategy(wssKeyStoreConfiguration, wssSignConfiguration);
  }
}
