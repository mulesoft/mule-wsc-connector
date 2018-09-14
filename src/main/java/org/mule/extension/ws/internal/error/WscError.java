/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.error;

import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

public enum WscError implements ErrorTypeDefinition<WscError> {

  INVALID_WSDL, ENCODING, BAD_REQUEST, BAD_RESPONSE, CANNOT_DISPATCH, CONNECTIVITY, SOAP_FAULT
}
