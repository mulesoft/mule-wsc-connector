/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal;

import org.mule.extension.ws.internal.metadata.SoapAttachmentsTypeResolver;
import org.mule.extension.ws.internal.metadata.SoapBodyTypeResolver;
import org.mule.extension.ws.internal.metadata.SoapHeadersTypeResolver;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.extension.api.annotation.metadata.TypeResolver;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.NullSafe;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

import java.io.InputStream;
import java.util.Map;

/**
 * Component that specifies how to create a proper SOAP request using the {@link WebServiceConsumer}.
 *
 * @since 1.0
 */
public class SoapMessageBuilder {

  /**
   * The XML body to include in the SOAP message, with all the required parameters, or {@code null} if no params are required.
   */
  @Parameter
  @Content(primary = true)
  @TypeResolver(SoapBodyTypeResolver.class)
  @Summary("The XML body to include in the SOAP message, with all the required parameters.")
  private TypedValue<InputStream> body;

  /**
   * The XML headers to include in the SOAP message.
   */
  @Parameter
  @Optional
  @Content
  @TypeResolver(SoapHeadersTypeResolver.class)
  @Summary("The XML headers to include in the SOAP message.")
  private InputStream headers;

  /**
   * The attachments to include in the SOAP request.
   */
  @Parameter
  @Optional
  @NullSafe
  @Content
  @TypeResolver(SoapAttachmentsTypeResolver.class)
  @Summary("The attachments to include in the SOAP request.")
  private Map<String, TypedValue<?>> attachments;

  public TypedValue<InputStream> getBody() {
    return body;
  }

  public InputStream getHeaders() {
    return headers;
  }

  public Map<String, TypedValue<?>> getAttachments() {
    return attachments;
  }
}
