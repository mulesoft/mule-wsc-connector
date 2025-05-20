/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.transport;

import java.io.InputStream;
import java.util.Map;

/**
 * Abstraction of a HTTP Request Response.
 *
 * @since 1.2
 */
public class HttpRequestResponse {

  private final InputStream content;
  private final Map<String, String> httpHeaders;
  private final Map<String, String> statusLine;

  public HttpRequestResponse(InputStream content, Map<String, String> httpHeaders, Map<String, String> statusLine) {
    this.content = content;
    this.httpHeaders = httpHeaders;
    this.statusLine = statusLine;
  }

  /**
   * @return the content of the http response.
   *
   * @since 1.2
   */
  public InputStream getContent() {
    return this.content;
  }

  /**
   * @return a map containing values for the http headers.
   *
   * @since 1.2
   */
  public Map<String, String> getHttpHeaders() {
    return this.httpHeaders;
  }

  /**
   * @return a map containing values for the parts of the status line (for example status code, reason phrase, protocol version).
   *
   * @since 1.2
   */
  public Map<String, String> getStatusLine() {
    return this.statusLine;
  }
}
