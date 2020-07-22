/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal;

import static org.mule.extension.ws.internal.error.WscError.BAD_REQUEST;
import static org.mule.runtime.api.metadata.DataType.INPUT_STREAM;
import static org.mule.runtime.api.metadata.MediaType.XML;
import static org.mule.runtime.http.api.HttpHeaders.Names.CONTENT_TYPE;

import org.mule.extension.ws.api.SoapAttributes;
import org.mule.extension.ws.api.SoapOutputEnvelope;
import org.mule.extension.ws.api.TransportConfiguration;
import org.mule.extension.ws.internal.connection.WscSoapClient;
import org.mule.extension.ws.internal.error.ConsumeErrorTypeProvider;
import org.mule.extension.ws.internal.error.WscExceptionEnricher;
import org.mule.extension.ws.internal.metadata.ConsumeOutputResolver;
import org.mule.extension.ws.internal.metadata.OperationKeysResolver;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.el.BindingContext;
import org.mule.runtime.api.el.MuleExpressionLanguage;
import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.api.metadata.MediaType;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.api.transformation.TransformationService;
import org.mule.runtime.extension.api.annotation.OnException;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.metadata.MetadataKeyId;
import org.mule.runtime.extension.api.annotation.metadata.OutputResolver;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.streaming.StreamingHelper;
import org.mule.soap.api.message.SoapAttachment;
import org.mule.soap.api.message.SoapRequest;
import org.mule.soap.api.message.SoapRequestBuilder;
import org.mule.soap.api.message.SoapResponse;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
  @OutputResolver(output = ConsumeOutputResolver.class)
  public Result<SoapOutputEnvelope, SoapAttributes> consume(@Connection WscSoapClient connection,
                                                            @MetadataKeyId(OperationKeysResolver.class) String operation,
                                                            @ParameterGroup(name = "Message",
                                                                showInDsl = true) SoapMessageBuilder message,
                                                            @ParameterGroup(
                                                                name = "Transport Configuration") TransportConfiguration transportConfig,
                                                            StreamingHelper streamingHelper,
                                                            ExtensionsClient client)
      throws ConnectionException {
    SoapRequest request = getSoapRequest(operation, message, transportConfig.getTransportHeaders()).build();
    SoapResponse response = connection.consume(request, client);
    return Result.<SoapOutputEnvelope, SoapAttributes>builder()
        .output(new SoapOutputEnvelope(response, streamingHelper))
        .attributes(new SoapAttributes(response.getTransportHeaders(), response.getTransportAdditionalData()))
        .build();
  }

  private SoapRequestBuilder getSoapRequest(String operation, SoapMessageBuilder message, Map<String, String> transportHeaders) {
    SoapRequestBuilder requestBuilder = SoapRequest.builder();

    requestBuilder.attachments(toSoapAttachments(message.getAttachments()));
    requestBuilder.operation(operation);

    Optional<MediaType> mediaType = getMediaType(message.getBody());
    mediaType.ifPresent(mt -> requestBuilder.contentType(mt.toRfcString()));

    requestBuilder.transportHeaders(transportHeaders);

    InputStream headers = message.getHeaders();
    if (headers != null) {
      requestBuilder.soapHeaders((Map<String, String>) evaluateHeaders(headers));
    }

    requestBuilder.content(message.getBody().getValue());

    requestBuilder.useXMLInitialDeclaration(message.isUseXMLInitialDeclaration());

    return requestBuilder;
  }

  private Map<String, SoapAttachment> toSoapAttachments(Map<String, TypedValue<?>> attachments) {
    Map<String, SoapAttachment> soapAttachmentMap = new HashMap<>();
    attachments.forEach((name, attachment) -> {
      try {
        InputStream stream = toInputStream(attachment);
        SoapAttachment soapAttachment = new SoapAttachment(stream, attachment.getDataType().getMediaType().toRfcString());
        soapAttachmentMap.put(name, soapAttachment);
      } catch (Exception e) {
        throw new ModuleException("Error while adding attachments to the soap request", BAD_REQUEST, e);
      }
    });
    return soapAttachmentMap;
  }

  private Optional<MediaType> getMediaType(TypedValue<InputStream> typedValue) {
    DataType dataType = typedValue.getDataType();
    return dataType != null ? Optional.of(dataType.getMediaType()) : Optional.empty();
  }

  private InputStream toInputStream(TypedValue typedValue) {
    Object value = typedValue.getValue();
    if (value instanceof InputStream) {
      return (InputStream) value;
    }
    return (InputStream) transformationService.transform(value, DataType.fromObject(value), INPUT_STREAM);
  }

  private Object evaluateHeaders(InputStream headers) {
    BindingContext context = BindingContext.builder().addBinding("payload", new TypedValue<>(headers, XML_STREAM)).build();
    Object expressionResult = expressionExecutor.evaluate("%dw 2.0 \n"
        + "output application/java \n"
        + "---\n"
        + "payload.headers mapObject (value, key) -> {\n"
        + "    '$key' : write((key): value, \"application/xml\")\n"
        + "}", context).getValue();
    if (expressionResult == null) {
      throw new ModuleException("Invalid input headers XML: It must be an xml with the root tag named \'headers\'.",
                                BAD_REQUEST);
    }
    return expressionResult;
  }
}
