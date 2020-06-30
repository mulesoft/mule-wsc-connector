/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal;

import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.NullSafe;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.soap.api.transport.TransportResponse;

import java.io.InputStream;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**
 * Component that specifies how to create a proper SOAP response using the {@link WebServiceConsumer}.
 *
 * @since 2.0
 */
public class SoapResponseBuilder {

  /**
   * The body of the response
   */
  @Parameter
  @Content(primary = true)
  @Summary("The body of the SOAP response.")
  private TypedValue<InputStream> body;

  /**
   * The content type of the response
   */
  @Parameter
  @Summary("The content type of the response")
  private String contentType;

  /**
   * The transport headers of the response.
   */
  @Parameter
  @Optional
  @NullSafe
  @Summary("The transport headers of the SOAP response.")
  private Map<String, String> transportHeaders;

  public TransportResponse toTransportResponse() {

    Map<String, String> headers = ImmutableMap.<String, String>builder()
        .putAll(transportHeaders)
        .put("Content-Type", contentType)
        .build();
    return new TransportResponse(body.getValue(), headers);
  }
}
