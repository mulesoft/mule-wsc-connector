/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.security.config;

import static org.mule.runtime.api.meta.model.display.PathModel.Location.EMBEDDED;
import static org.mule.runtime.api.meta.model.display.PathModel.Type.FILE;

import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.extension.api.annotation.param.display.Path;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

import java.util.Objects;

/**
 * Implementation for Key Stores, used for encryption, decryption and signing.
 *
 * @since 1.0
 */
@Alias("wss-key-store-configuration")
public class WssKeyStoreConfigurationAdapter {

  public WssKeyStoreConfigurationAdapter() {}

  @Parameter
  @Placement(order = 0)
  @Summary("The location of the KeyStore file")
  @Path(type = FILE, location = EMBEDDED)
  private String keyStorePath;

  @Parameter
  @Placement(order = 1)
  @Summary("The alias of the private key to use")
  private String alias;

  @Parameter
  @Placement(order = 2)
  @Summary("The password to access the store.")
  @Password
  private String password;

  @Parameter
  @Placement(order = 3)
  @Summary("The password used to access the private key.")
  @Optional
  @Password
  private String keyPassword;

  @Parameter
  @Optional(defaultValue = "jks")
  @Placement(order = 4)
  @Summary("The type of store (jks, pkcs12, jceks, or any other)")
  private String type;

  /**
   * @return The password used to access the private key.
   */
  public String getKeyPassword() {
    return keyPassword;
  }

  /**
   * @return The alias of the private key to use.
   */
  public String getAlias() {
    return alias;
  }

  /**
   * {@inheritDoc}
   */
  public String getStorePath() {
    return keyStorePath;
  }

  /**
   * {@inheritDoc}
   */
  public String getPassword() {
    return password;
  }

  /**
   * {@inheritDoc}
   */
  public String getType() {
    return type;
  }

  public String getKeyStorePath() {
    return keyStorePath;
  }

  public void setKeyStorePath(String keyStorePath) {
    this.keyStorePath = keyStorePath;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setKeyPassword(String keyPassword) {
    this.keyPassword = keyPassword;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    WssKeyStoreConfigurationAdapter that = (WssKeyStoreConfigurationAdapter) o;
    return Objects.equals(keyStorePath, that.keyStorePath) &&
        Objects.equals(alias, that.alias) &&
        Objects.equals(password, that.password) &&
        Objects.equals(keyPassword, that.keyPassword) &&
        Objects.equals(type, that.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(keyStorePath, alias, password, keyPassword, type);
  }
}
