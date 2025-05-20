/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.error;

/**
 * Exception thrown when an unknown error occurred when using the Web Service Consumer.
 *
 * @since 1.1.2
 */
public class WscException extends Exception {

  public WscException(String msg, Exception cause) {
    super(msg, cause);
  }
}
