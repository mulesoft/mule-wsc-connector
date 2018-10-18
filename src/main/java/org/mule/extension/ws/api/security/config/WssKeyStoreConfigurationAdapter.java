/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
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
import org.mule.runtime.extension.api.annotation.param.display.Summary;

/**
 * Implementation for Key Stores, used for encryption, decryption and signing.
 *
 * @since 1.0
 */
@Alias("wss-key-store-configuration")
public class WssKeyStoreConfigurationAdapter {

  @Parameter
  @Summary("The alias of the private key to use")
  private String alias;

  @Parameter
  @Summary("The password used to access the private key.")
  @Optional
  @Password
  private String keyPassword;

  @Parameter
  @Summary("The password to access the store.")
  @Password
  private String password;

  @Parameter
  @Summary("The location of the KeyStore file")
  @Path(type = FILE, location = EMBEDDED)
  private String keyStorePath;

  @Parameter
  @Optional(defaultValue = "jks")
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
}
