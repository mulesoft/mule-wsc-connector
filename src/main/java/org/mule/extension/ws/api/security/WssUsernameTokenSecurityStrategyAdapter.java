/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.extension.ws.api.security;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.soap.api.security.SecurityStrategy;
import org.mule.soap.api.security.WssUsernameTokenSecurityStrategy;

import java.util.Objects;

/**
 * Provides the capability to authenticate using Username and Password with a SOAP service by adding the UsernameToken element in
 * the SOAP request.
 *
 * @since 1.0
 */
public class WssUsernameTokenSecurityStrategyAdapter implements SecurityStrategyAdapter {

  /**
   * The username required to authenticate with the service.
   */
  @Parameter
  private String username;

  /**
   * The password for the provided username required to authenticate with the service.
   */
  @Parameter
  @Password
  private String password;

  /**
   * A {@link PasswordTypeAdapter} which qualifies the {@link #password} parameter.
   */
  @Parameter
  @Optional(defaultValue = "TEXT")
  @Summary("The type of the password that is provided. One of Digest or Text")
  private PasswordTypeAdapter passwordType;

  /**
   * Specifies a if a cryptographically random nonce should be added to the message.
   */
  @Parameter
  @Optional
  private boolean addNonce;

  /**
   * Specifies if a timestamp should be created to indicate the creation time of the message.
   */
  @Parameter
  @Optional
  private boolean addCreated;

  @Override
  public SecurityStrategy getSecurityStrategy() {
    return new WssUsernameTokenSecurityStrategy(username, password, passwordType.getType(), addNonce, addCreated);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    WssUsernameTokenSecurityStrategyAdapter that = (WssUsernameTokenSecurityStrategyAdapter) o;
    return addNonce == that.addNonce &&
        addCreated == that.addCreated &&
        Objects.equals(username, that.username) &&
        Objects.equals(password, that.password) &&
        passwordType == that.passwordType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password, passwordType, addNonce, addCreated);
  }
}
