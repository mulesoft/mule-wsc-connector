/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.error;

import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

import java.util.Optional;

/**
 * Errors thrown by the WSC.
 *
 * @since 1.0
 */
public enum WscError implements ErrorTypeDefinition<WscError> {

  /**
   * Error thrown when an invalid WSDL file was provided.
   */
  INVALID_WSDL,

  /**
   * Error thrown when an encoding problem occurred.
   */
  ENCODING,

  /**
   * Error thrown when an error occurred trying to build the request.
   */
  BAD_REQUEST,

  /**
   * Error thrown when an error occurred parsing the response.
   */
  BAD_RESPONSE,

  /**
   * Error thrown when an error occurred parsing the response, and it is an Illegal empty response.
   */
  EMPTY_RESPONSE(BAD_RESPONSE),

  /**
   * Error thrown when something went wrong while dispatching.
   */
  CANNOT_DISPATCH,

  /**
   * Error thrown when a SOAP FAULT was returned by the server
   */
  SOAP_FAULT,

  /**
   * Error thrown when the dispatching process timed out.
   */
  TIMEOUT;

  private ErrorTypeDefinition<? extends Enum<?>> parent;

  WscError(ErrorTypeDefinition<? extends Enum<?>> parent) {
    this.parent = parent;
  }

  WscError() {}

  @Override
  public Optional<ErrorTypeDefinition<? extends Enum<?>>> getParent() {
    return Optional.ofNullable(parent);
  }
}
