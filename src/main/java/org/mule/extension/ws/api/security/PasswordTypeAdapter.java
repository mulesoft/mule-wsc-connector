/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.security;

import org.mule.soap.api.security.PasswordType;

/**
 * The password types that can be used in WSS strategies.
 *
 * @since 1.2
 */
public enum PasswordTypeAdapter {

  DIGEST(PasswordType.DIGEST), TEXT(PasswordType.TEXT);

  private PasswordType type;

  PasswordTypeAdapter(PasswordType type) {
    this.type = type;
  }

  public PasswordType getType() {
    return type;
  }
}
