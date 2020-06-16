/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.addressing;

import org.mule.runtime.api.el.MuleExpressionLanguage;
import org.mule.extension.ws.internal.addressing.properties.AddressingProperties;

public class AddressingHeadersResolverFactory {

  private final MuleExpressionLanguage expressionExecutor;

  public AddressingHeadersResolverFactory(MuleExpressionLanguage expressionExecutor) {
    this.expressionExecutor = expressionExecutor;
  }

  public AddressingHeadersResolver create(AddressingProperties properties) {

    AddressingQNameResolver resolver = properties.getNamespaceURI() == OldAddressingQNameResolver.WSA_NAMESPACE_NAME
        ? new OldAddressingQNameResolver()
        : new DefaultAddressingQNameResolver();

    return new AddressingHeadersResolver(new DefaultHeadersVersionEncoder(resolver,
                                                                          new DefaultAddressingXmlTransformer(expressionExecutor)));
  }
}
