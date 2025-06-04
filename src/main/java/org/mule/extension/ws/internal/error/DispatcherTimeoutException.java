/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.error;

import org.mule.soap.api.transport.DispatcherException;

/**
 * Exception thrown when a TIMEOUT error occurred while dispatching the request.
 *
 * @since 1.1.2
 */
public class DispatcherTimeoutException extends DispatcherException {

  public DispatcherTimeoutException(String msg, Exception cause) {
    super(msg, cause);
  }
}
