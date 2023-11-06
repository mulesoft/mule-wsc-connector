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
import org.mule.sdk.api.annotation.semantics.connectivity.ExcludeFromConnectivitySchema;
import org.mule.soap.api.security.SecurityStrategy;
import org.mule.soap.api.security.WssSignSecurityStrategy;
import org.mule.soap.api.security.configuration.WssPart;
import org.mule.soap.api.security.configuration.WssSignConfiguration;
import org.mule.soap.api.security.stores.WssKeyStoreConfiguration;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
  @ExcludeFromConnectivitySchema
  private WssSignConfigurationAdapter signAlgorithmConfiguration;

  public WssSignSecurityStrategyAdapter() {
  }

  @Override
  public SecurityStrategy getSecurityStrategy() {

    WssKeyStoreConfiguration wssKeyStoreConfiguration = new WssKeyStoreConfiguration(keyStoreConfiguration.getAlias(),
                                                                                     keyStoreConfiguration.getPassword(),
                                                                                     keyStoreConfiguration.getStorePath(),
                                                                                     keyStoreConfiguration.getKeyPassword(),
                                                                                     keyStoreConfiguration.getType());

    String signatureAlgorithm = signAlgorithmConfiguration.getSignatureAlgorithm() != null
        ? signAlgorithmConfiguration.getSignatureAlgorithm().toString()
        : null;

    List<WssPart> wssSignParts = null;
    if (signAlgorithmConfiguration.getWssParts() != null) {
      wssSignParts = signAlgorithmConfiguration.getWssParts().stream()
          .map(wssSignPartAdapter -> new WssPart(wssSignPartAdapter.getEncode().toString(),
                                                 wssSignPartAdapter.getNamespace(),
                                                 wssSignPartAdapter.getLocalname()))
          .collect(Collectors.toList());
    }

    WssSignConfiguration wssSignConfiguration =
        new WssSignConfiguration(signAlgorithmConfiguration.getSignatureKeyIdentifier().toString(),
                                 signatureAlgorithm,
                                 signAlgorithmConfiguration.getSignatureDigestAlgorithm().toString(),
                                 signAlgorithmConfiguration.getSignatureC14nAlgorithm().toString(),
                                 wssSignParts);

    return new WssSignSecurityStrategy(wssKeyStoreConfiguration, wssSignConfiguration);
  }

  public WssKeyStoreConfigurationAdapter getKeyStoreConfiguration() {
    return keyStoreConfiguration;
  }

  public void setKeyStoreConfiguration(WssKeyStoreConfigurationAdapter keyStoreConfiguration) {
    this.keyStoreConfiguration = keyStoreConfiguration;
  }

  public WssSignConfigurationAdapter getSignAlgorithmConfiguration() {
    return signAlgorithmConfiguration;
  }

  public void setSignAlgorithmConfiguration(WssSignConfigurationAdapter signAlgorithmConfiguration) {
    this.signAlgorithmConfiguration = signAlgorithmConfiguration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    WssSignSecurityStrategyAdapter that = (WssSignSecurityStrategyAdapter) o;
    return Objects.equals(keyStoreConfiguration, that.keyStoreConfiguration) &&
        Objects.equals(signAlgorithmConfiguration, that.signAlgorithmConfiguration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(keyStoreConfiguration, signAlgorithmConfiguration);
  }
}
