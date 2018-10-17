/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.connection;

import org.mule.extension.ws.internal.error.DispatcherTimeoutException;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.core.api.util.IOUtils;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.domain.entity.InputStreamHttpEntity;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;
import org.mule.soap.api.transport.DispatcherException;
import org.mule.soap.api.transport.TransportDispatcher;
import org.mule.soap.api.transport.TransportRequest;
import org.mule.soap.api.transport.TransportResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static java.lang.String.join;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.mule.runtime.http.api.HttpConstants.Method.POST;

/**
 * {@link TransportDispatcher} implementation that sends messages using the {@link HttpClient}.
 *
 * @since 1.1
 */
public class DefaultHttpMessageDispatcher implements TransportDispatcher {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultHttpMessageDispatcher.class.getName());

  private final HttpClient client;
  private final int timeout;

  public DefaultHttpMessageDispatcher(HttpClient client, int timeout) {
    this.client = client;
    this.timeout = timeout;
  }

  @Override
  public TransportResponse dispatch(TransportRequest request) throws DispatcherException {
    try {
      InputStream content = this.logIfNeeded("Soap Request to [" + request.getAddress() + "]", request.getContent());
      HttpRequest httpPostRequest = HttpRequest.builder().uri(request.getAddress())
          .method(POST)
          .entity(new InputStreamHttpEntity(content))
          .headers(new MultiMap<>(request.getHeaders()))
          .build();
      HttpResponse response = client.send(httpPostRequest, timeout, false, null);
      return new TransportResponse(logIfNeeded("Soap Response", response.getEntity().getContent()), toHeadersMap(response));
    } catch (IOException ioe) {
      throw new DispatcherException("An error occurred while sending the SOAP request", ioe);
    } catch (TimeoutException te) {
      throw new DispatcherTimeoutException("The SOAP request timed out", te);
    }
  }

  private InputStream logIfNeeded(String title, InputStream content) {
    if (LOGGER.isDebugEnabled()) {
      String c = IOUtils.toString(content);
      LOGGER.debug("Logging " + title);
      LOGGER.debug("-----------------------------------");
      LOGGER.debug(c);
      LOGGER.debug("-----------------------------------");
      return new ByteArrayInputStream(c.getBytes());
    } else {
      return content;
    }
  }

  private Map<String, String> toHeadersMap(HttpResponse response) {
    return response.getHeaderNames().stream().collect(toMap(identity(), name -> join(" ", response.getHeaderValues(name))));
  }
}
