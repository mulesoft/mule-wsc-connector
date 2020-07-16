/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.addressing.value;

import org.mule.extension.ws.internal.connection.WscSoapClient;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.values.ValueBuilder;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.extension.api.values.ValueResolvingException;

import java.util.Set;

/**
 * {@link ValueProvider} implementation which provides possible values for the {@code wsa:Action} header
 *
 * @since 2.0
 */
public class AddressingActionValueProvider implements ValueProvider {

  @Connection
  private WscSoapClient client;

  @Parameter
  private String operation;

  @Override
  public Set<Value> resolve() throws ValueResolvingException {
    return ValueBuilder
        .getValuesFor(client.getInfo().getAddress() + "/" + client.getInfo().getPort() + "/" + operation + "Request");
  }
}
