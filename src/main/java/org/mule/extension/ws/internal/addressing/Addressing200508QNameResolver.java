/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.addressing;

import org.mule.extension.ws.api.addressing.AddressingVersion;

/**
 * Provide WS-Addressing headers QNames of {@link AddressingVersion} 2005-08
 *
 * @since 2.0
 */
public class Addressing200508QNameResolver extends AbstractAddressingQNameResolver {

  public static final String WSA_NAMESPACE_NAME =
      "http://www.w3.org/2005/08/addressing";

  public Addressing200508QNameResolver() {
    super(WSA_NAMESPACE_NAME);
  }
}
