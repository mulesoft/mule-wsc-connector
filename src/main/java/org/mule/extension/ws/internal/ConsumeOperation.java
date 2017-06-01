/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal;


import static org.mule.runtime.api.metadata.DataType.INPUT_STREAM;
import static org.mule.runtime.api.metadata.MediaType.XML;
import org.mule.extension.ws.internal.metadata.ConsumeAttributesResolver;
import org.mule.extension.ws.internal.metadata.ConsumeOutputResolver;
import org.mule.extension.ws.internal.metadata.OperationKeysResolver;
import org.mule.runtime.api.el.BindingContext;
import org.mule.runtime.api.el.MuleExpressionLanguage;
import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.core.api.TransformationService;
import org.mule.runtime.extension.api.annotation.OnException;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.metadata.MetadataKeyId;
import org.mule.runtime.extension.api.annotation.metadata.OutputResolver;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.soap.SoapAttachment;
import org.mule.runtime.soap.api.client.SoapClient;
import org.mule.runtime.soap.api.exception.BadRequestException;
import org.mule.runtime.soap.api.exception.SoapFaultException;
import org.mule.runtime.soap.api.message.SoapAttributes;
import org.mule.runtime.soap.api.message.SoapRequest;
import org.mule.runtime.soap.api.message.SoapRequestBuilder;
import org.mule.runtime.soap.api.message.SoapResponse;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * The only {@link WebServiceConsumer} operation. the {@link ConsumeOperation} consumes an operation of the connected web service
 * and returns it's response.
 * <p>
 * The consume operation expects an XML body and a set of headers and attachments if required.
 * <p>
 *
 * @since 1.0
 */
public class ConsumeOperation {

  private static final DataType XML_STREAM = DataType.builder().type(InputStream.class).mediaType(XML).build();

  @Inject
  private MuleExpressionLanguage expressionExecutor;

  @Inject
  private TransformationService transformationService;

  /**
   * Consumes an operation from a SOAP Web Service.
   *
   * @param connection the connection resolved to execute the operation.
   * @param operation  the name of the web service operation that aims to invoke.
   * @param message    the constructed SOAP message to perform the request.
   */
  @OnException(WscExceptionEnricher.class)
  @Throws(ConsumeErrorTypeProvider.class)
  @OutputResolver(output = ConsumeOutputResolver.class, attributes = ConsumeAttributesResolver.class)
  public Result<?, SoapAttributes> consume(@Connection SoapClient connection,
                                           @MetadataKeyId(OperationKeysResolver.class) String operation,
                                           @ParameterGroup(name = "Message", showInDsl = true) SoapMessageBuilder message,
                                           @ParameterGroup(
                                               name = "Transport Configuration") TransportConfiguration transportConfig)
      throws SoapFaultException {
    SoapRequestBuilder requestBuilder = getSoapRequest(operation, message, transportConfig.getTransportHeaders());
    SoapResponse response = connection.consume(requestBuilder.build());
    return response.getAsResult();
  }

  private SoapRequestBuilder getSoapRequest(String operation, SoapMessageBuilder message, Map<String, String> transportHeaders) {
    SoapRequestBuilder requestBuilder = SoapRequest.builder();
    requestBuilder.withAttachments(toSoapAttachments(message.getAttachments()));
    requestBuilder.withOperation(operation);
    requestBuilder.withTransportHeaders(transportHeaders);

    InputStream headers = message.getHeaders();
    if (headers != null) {
      requestBuilder.withSoapHeaders((Map<String, String>) evaluateHeaders(headers));
    }
    requestBuilder.withContent(message.getBody());
    return requestBuilder;
  }

  private Map<String, SoapAttachment> toSoapAttachments(Map<String, TypedValue<?>> attachments) {
    Map<String, SoapAttachment> soapAttachmentMap = new HashMap<>();
    attachments.forEach((name, attachment) -> {
      try {
        InputStream stream = toInputStream(attachment);
        SoapAttachment soapAttachment = new SoapAttachment(stream, attachment.getDataType().getMediaType());
        soapAttachmentMap.put(name, soapAttachment);
      } catch (Exception e) {
        throw new BadRequestException("Error while adding attachments to the soap request", e);
      }
    });
    return soapAttachmentMap;
  }

  private InputStream toInputStream(TypedValue typedValue) throws Exception {
    Object value = typedValue.getValue();
    if (value instanceof InputStream) {
      return (InputStream) value;
    }
    return (InputStream) transformationService.transform(value, DataType.fromObject(value), INPUT_STREAM);
  }

  private Object evaluateHeaders(InputStream headers) {
    BindingContext context = BindingContext.builder().addBinding("payload", new TypedValue<>(headers, XML_STREAM)).build();
    return expressionExecutor.evaluate("%dw 2.0 \n"
        + "output application/java \n"
        + "---\n"
        + "payload.headers mapObject (value, key) -> {\n"
        + "    '$key' : write((key): value, \"application/xml\")\n"
        + "}", context).getValue();
  }
}
