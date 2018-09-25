/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.error;

import com.google.common.collect.ImmutableSet;
import org.mule.extension.ws.internal.ConsumeOperation;
import org.mule.runtime.extension.api.annotation.error.ErrorTypeProvider;
import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

import java.util.Set;

import static org.mule.extension.ws.internal.error.WscError.BAD_REQUEST;
import static org.mule.extension.ws.internal.error.WscError.BAD_RESPONSE;
import static org.mule.extension.ws.internal.error.WscError.CANNOT_DISPATCH;
import static org.mule.extension.ws.internal.error.WscError.ENCODING;
import static org.mule.extension.ws.internal.error.WscError.INVALID_WSDL;
import static org.mule.extension.ws.internal.error.WscError.SOAP_FAULT;

/**
 * {@link ErrorTypeProvider} implementation for the {@link ConsumeOperation}.
 *
 * @since 1.0
 */
public class ConsumeErrorTypeProvider implements ErrorTypeProvider {

  /**
   * @return all the error types that can be thrown by the {@link ConsumeOperation}.
   */
  @Override
  public Set<ErrorTypeDefinition> getErrorTypes() {
    return ImmutableSet.of(BAD_REQUEST, BAD_RESPONSE, ENCODING, INVALID_WSDL, SOAP_FAULT, CANNOT_DISPATCH);
  }
}
