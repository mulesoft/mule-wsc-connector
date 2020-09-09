/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.error;

import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

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
   * Error thrown when something went wrong while dispatching.
   */
  CANNOT_DISPATCH,

  /**
   * Error thrown when something went wrong while using the RM store.
   */
  RM_STORE,

  /**
   * Error thrown when a SOAP FAULT was returned by the server
   */
  SOAP_FAULT,

  /**
   * Error thrown when the dispatching process timed out.
   */
  TIMEOUT
}
