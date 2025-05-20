/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.error;

import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.of;
import static org.mule.extension.ws.internal.error.WscError.BAD_REQUEST;
import static org.mule.extension.ws.internal.error.WscError.BAD_RESPONSE;
import static org.mule.extension.ws.internal.error.WscError.EMPTY_RESPONSE;
import static org.mule.extension.ws.internal.error.WscError.CANNOT_DISPATCH;
import static org.mule.extension.ws.internal.error.WscError.ENCODING;
import static org.mule.extension.ws.internal.error.WscError.INVALID_WSDL;
import static org.mule.extension.ws.internal.error.WscError.TIMEOUT;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;

import org.mule.extension.ws.internal.ConsumeOperation;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.runtime.extension.api.runtime.exception.ExceptionHandler;
import org.mule.soap.api.client.BadRequestException;
import org.mule.soap.api.client.BadResponseException;
import org.mule.soap.api.client.EmptyResponseException;
import org.mule.soap.api.exception.EncodingException;
import org.mule.soap.api.exception.InvalidWsdlException;
import org.mule.soap.api.exception.SoapFaultException;
import org.mule.soap.api.transport.DispatcherException;

/**
 * {@link ExceptionHandler} implementation to wrap unexpected exceptions thrown by the {@link ConsumeOperation} and if a Soap
 * Fault is returned by the server we wrap that exception in an {@link SoapFaultMessageAwareException}.
 *
 * @since 1.0
 */
public class WscExceptionEnricher extends ExceptionHandler {


  private static final Map<Class<?>, WscError> ERRORS_MAPPING =
      unmodifiableMap(of(
                         new SimpleEntry<>(BadResponseException.class, BAD_RESPONSE),
                         new SimpleEntry<>(EmptyResponseException.class, EMPTY_RESPONSE),
                         new SimpleEntry<>(BadRequestException.class, BAD_REQUEST),
                         new SimpleEntry<>(DispatcherException.class, CANNOT_DISPATCH),
                         new SimpleEntry<>(DispatcherTimeoutException.class, TIMEOUT),
                         new SimpleEntry<>(InvalidWsdlException.class, INVALID_WSDL),
                         new SimpleEntry<>(EncodingException.class, ENCODING))
                             .collect(toMap(Entry::getKey, Entry::getValue)));

  /**
   * {@inheritDoc}
   */
  @Override
  public Exception enrichException(Exception e) {

    if (e instanceof ModuleException) {
      return e;
    }
    if (e instanceof DispatcherException && !(e instanceof DispatcherTimeoutException)) {
      return new DispatcherMessageAwareException((DispatcherException) e);
    }
    if (e instanceof SoapFaultException) {
      return new SoapFaultMessageAwareException(((SoapFaultException) e));
    }
    WscError error = ERRORS_MAPPING.get(e.getClass());
    if (error != null) {
      return new ModuleException(error, e);
    }
    return new WscException("Unexpected error while consuming web service: " + e.getMessage(), e);
  }
}
