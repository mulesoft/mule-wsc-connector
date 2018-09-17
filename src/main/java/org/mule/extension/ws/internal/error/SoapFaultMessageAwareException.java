/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.error;

import org.mule.runtime.api.exception.ErrorMessageAwareException;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.soap.api.exception.SoapFaultException;

import static org.mule.extension.ws.internal.error.WscError.SOAP_FAULT;

/**
 * {@link ErrorMessageAwareException} that provides all the soap fault information on the `error.errorMessage`.
 *
 * @since 1.1
 */
public class SoapFaultMessageAwareException extends ModuleException implements ErrorMessageAwareException {

  private final Message msg;

  public SoapFaultMessageAwareException(SoapFaultException cause) {
    super(cause.getMessage(), SOAP_FAULT, cause);
    this.msg = Message.builder().value(cause).build();
  }

  @Override
  public Message getErrorMessage() {
    return msg;
  }
}
