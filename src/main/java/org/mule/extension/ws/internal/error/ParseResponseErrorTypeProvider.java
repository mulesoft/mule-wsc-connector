/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.error;

import org.mule.extension.ws.internal.ParseResponseOperation;
import org.mule.runtime.extension.api.annotation.error.ErrorTypeProvider;
import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

import java.util.Set;

import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.of;
import static org.mule.extension.ws.internal.error.WscError.*;

/**
 * {@link ErrorTypeProvider} implementation for the {@link ParseResponseOperation}.
 *
 * @since 1.7
 */
public class ParseResponseErrorTypeProvider implements ErrorTypeProvider {

  /**
   * @return all the error types that can be thrown by the {@link ParseResponseOperation}.
   */
  @Override
  public Set<ErrorTypeDefinition> getErrorTypes() {
    return unmodifiableSet(of(BAD_RESPONSE, ENCODING, INVALID_WSDL, SOAP_FAULT)
        .collect(toSet()));
  }
}
