/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal;

import org.mule.extension.ws.api.SoapAttributes;
import org.mule.extension.ws.api.SoapOutputEnvelope;
import org.mule.extension.ws.internal.connection.WscSoapClient;
import org.mule.extension.ws.internal.error.ParseResponseErrorTypeProvider;
import org.mule.extension.ws.internal.error.WscExceptionEnricher;
import org.mule.extension.ws.internal.metadata.ConsumeOutputResolver;
import org.mule.extension.ws.internal.metadata.OperationKeysResolver;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.extension.api.annotation.OnException;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.metadata.MetadataKeyId;
import org.mule.runtime.extension.api.annotation.metadata.OutputResolver;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.streaming.StreamingHelper;
import org.mule.soap.api.message.SoapResponse;

/**
 * The {@link ParseResponseOperation} knows how to convert a raw response into a SOAP one.
 * <p>
 * The parse response operation expects a response and optionally a set of transport headers and additional data.
 * <p>
 *
 * @since 2.0
 */
public class ParseResponseOperation {

  /**
   * Parses a response of an operation from a SOAP Web Service.
   *
   * @param connection the connection resolved to handle the response.
   * @param operation  the name of the web service operation that the response is related to.
   */
  @OnException(WscExceptionEnricher.class)
  @Throws(ParseResponseErrorTypeProvider.class)
  @OutputResolver(output = ConsumeOutputResolver.class)
  public Result<SoapOutputEnvelope, SoapAttributes> parseResponse(@Connection WscSoapClient connection,
                                                                  @MetadataKeyId(OperationKeysResolver.class) String operation,
                                                                  @ParameterGroup(name = "Response",
                                                                      showInDsl = true) Response response,
                                                                  StreamingHelper streamingHelper)
      throws ConnectionException {
    SoapResponse soapResponse = connection.parseResponse(operation, response.getTransportResponse());
    return Result.<SoapOutputEnvelope, SoapAttributes>builder()
        .output(new SoapOutputEnvelope(soapResponse, streamingHelper))
        .attributes(new SoapAttributes(soapResponse.getTransportHeaders(), soapResponse.getTransportAdditionalData()))
        .build();
  }
}
