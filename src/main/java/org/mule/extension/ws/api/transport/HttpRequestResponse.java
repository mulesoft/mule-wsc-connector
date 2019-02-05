/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.transport;

import java.io.InputStream;
import java.util.Map;

/**
 * POJO that simulates a HTTP response.
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
   * @return a set containing values for the http headers.
   *
   * @since 1.2
   */
  public Map<String, String> getHttpHeaders() {
    return this.httpHeaders;
  }

  /**
   * @return a set containing values for the parts of the status line: status code, reason phrase and protocol version.
   *
   * @since 1.2
   */
  public Map<String, String> getStatusLine() {
    return this.statusLine;
  }
}
