/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.reliablemessaging.value;

import org.mule.extension.ws.api.addressing.AddressingVersion;
import org.mule.extension.ws.api.reliablemessaging.ReliableMessagingVersion;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.values.ValueBuilder;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.extension.api.values.ValueResolvingException;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@link ValueProvider} implementation which provides the possible and supported values for the
 * {@link ReliableMessagingVersion} parameter.
 *
 * @since 1.7
 */
public class ReliableMessagingVersionValueProvider implements ValueProvider {

  @Parameter
  @Optional
  private AddressingVersion wsaVersion;

  @Override
  public Set<Value> resolve() throws ValueResolvingException {
    return Arrays.stream(ReliableMessagingVersion.values())
        .filter(version -> version.getAddressingVersion().equals(wsaVersion))
        .map(version -> ValueBuilder.newValue(version.name()).withDisplayName(version.getVersion()).build())
        .collect(Collectors.toSet());
  }
}
