/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api;

import static java.nio.charset.Charset.forName;
import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static org.mule.runtime.api.metadata.DataType.builder;
import static org.mule.runtime.api.metadata.MediaType.APPLICATION_XML;

import org.apache.commons.io.IOUtils;
import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.api.streaming.bytes.CursorStreamProvider;
import org.mule.runtime.extension.api.runtime.streaming.StreamingHelper;
import org.mule.soap.api.message.SoapAttachment;
import org.mule.soap.api.message.SoapResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Represents the response of the consume operation. Bundles the SOAP body, SOAP Headers and Mime Attachments if there are any.
 *
 * @since 1.0
 */
public class SoapOutputEnvelope {

  private static final Logger LOGGER = LoggerFactory.getLogger(SoapOutputEnvelope.class.getName());

  private final TypedValue<InputStream> body;
  private final Map<String, TypedValue<InputStream>> attachments;
  private final Map<String, TypedValue<String>> headers;

  public SoapOutputEnvelope(SoapResponse response, StreamingHelper streamingHelper) {
    this.body = wrapBody(response.getContent(), response.getContentType(), streamingHelper);
    this.headers = wrapHeaders(response.getSoapHeaders());
    this.attachments = wrapAttachments(response.getAttachments(), streamingHelper);
  }

  private TypedValue<InputStream> wrapBody(InputStream body, String contentType, StreamingHelper helper) {
    DataType dataType = builder().type(InputStream.class).mediaType(contentType).build();
    return new TypedValue(helper.resolveCursorProvider(body), dataType);
  }

  private Map<String, TypedValue<String>> wrapHeaders(Map<String, String> headers) {
    Map<String, TypedValue<String>> wrapped = headers
        .entrySet()
        .stream()
        .collect(toMap(Map.Entry::getKey, e -> new TypedValue(e.getValue(), builder().mediaType(APPLICATION_XML).build())));
    return unmodifiableMap(wrapped);
  }

  private Map<String, TypedValue<InputStream>> wrapAttachments(Map<String, SoapAttachment> attachments, StreamingHelper helper) {
    Map<String, TypedValue<InputStream>> wrapped = attachments
        .entrySet()
        .stream()
        .collect(toMap(Map.Entry::getKey, e -> {
          DataType dataType = builder().type(InputStream.class).mediaType(e.getValue().getContentType()).build();
          return new TypedValue(helper.resolveCursorProvider(e.getValue().getContent()), dataType);
        }));
    return unmodifiableMap(wrapped);
  }

  /**
   * @return The xml response body. Represents the <SOAP:BODY> element
   */
  public TypedValue<InputStream> getBody() {
    return body;
  }

  /**
   * @return A set of attachments bounded to the response, an empty map if there is no attachments.
   */
  public Map<String, TypedValue<InputStream>> getAttachments() {
    return attachments;
  }

  /**
   * @return A set of XML SOAP headers. Represents the content inside the <SOAP:HEADERS> element.
   */
  public Map<String, TypedValue<String>> getHeaders() {
    return headers;
  }

  @Override
  public String toString() {
    try {
      String hs = headers.values().stream().map(v -> "\"" + v.getValue() + "\"").collect(joining(",\n  "));
      String as = String.join(", ", attachments.keySet());
      return "{\n" +
          "body:" + getBodyString() + ",\n" +
          "headers: [" + hs + "]" + ",\n" +
          "attachments: [" + as + "]" + "\n" +
          "}";
    } catch (Exception e) {
      LOGGER.debug("Error building soap output envelope string: " + e.getMessage(), e);
      return "Error building soap output envelope string";
    }
  }

  private String getBodyString() throws IOException {
    Object value = body.getValue();
    InputStream stream = null;
    if (value instanceof CursorStreamProvider) {
      stream = ((CursorStreamProvider) value).openCursor();
    } else if (value instanceof InputStream) {
      stream = ((InputStream) value);
    }
    return stream != null ? IOUtils.toString(stream, getBodyCharset()) : value.toString();
  }

  private Charset getBodyCharset() {
    DataType dataType = body.getDataType();
    Charset defaultCharset = forName("UTF-8");
    return dataType != null ? dataType.getMediaType().getCharset().orElse(defaultCharset) : defaultCharset;
  }
}
