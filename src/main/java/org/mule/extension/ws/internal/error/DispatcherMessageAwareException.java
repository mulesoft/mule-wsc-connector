/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.error;

import static org.mule.extension.ws.internal.error.WscError.CANNOT_DISPATCH;

import static java.util.Objects.nonNull;

import org.mule.runtime.api.exception.ErrorMessageAwareException;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.soap.api.transport.DispatcherException;

public class DispatcherMessageAwareException extends ModuleException implements ErrorMessageAwareException {

  private final Message msg;

  public DispatcherMessageAwareException(DispatcherException exception) {
    super(exception.getMessage(), CANNOT_DISPATCH, exception);
    Throwable rootCause = findRootCause(exception);
    if (rootCause instanceof ErrorMessageAwareException) {
      this.msg = ((ErrorMessageAwareException) rootCause).getErrorMessage();
    } else {
      this.msg = Message.builder().value(exception.getMessage()).build();
    }
  }

  @Override
  public Message getErrorMessage() {
    return msg;
  }

  private Throwable findRootCause(Throwable throwable) {
    Throwable cause = throwable;
    while (cause != null && cause.getCause() != null) {
      cause = cause.getCause();
    }
    return cause;
  }
}
