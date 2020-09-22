/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.error;

import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.of;
import static org.mule.extension.ws.internal.error.WscError.CANNOT_DISPATCH;
import static org.mule.extension.ws.internal.error.WscError.SOAP_FAULT;
import static org.mule.extension.ws.internal.error.WscError.RM_STORE;
import static org.mule.extension.ws.internal.error.WscError.TIMEOUT;

import org.mule.extension.ws.internal.ReliableMessagingOperations;
import org.mule.runtime.extension.api.annotation.error.ErrorTypeProvider;
import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

import java.util.Set;

/**
 * {@link ErrorTypeProvider} implementation for the {@link ReliableMessagingOperations} create rm sequence operation.
 *
 * @since 1.7
 */
public class CreateRMSequenceErrorTypeProvider implements ErrorTypeProvider {

  /**
   * @return all the error types that can be thrown by the {@link ReliableMessagingOperations} create rm sequence operation.
   */
  @Override
  public Set<ErrorTypeDefinition> getErrorTypes() {
    return unmodifiableSet(of(SOAP_FAULT, CANNOT_DISPATCH, RM_STORE, TIMEOUT)
        .collect(toSet()));
  }
}
