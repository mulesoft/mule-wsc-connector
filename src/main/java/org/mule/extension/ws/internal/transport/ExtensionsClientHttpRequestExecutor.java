/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.transport;

import static java.lang.String.join;
import static java.lang.Thread.currentThread;
import static java.util.stream.Collectors.toMap;
import static org.mule.runtime.api.metadata.DataType.INPUT_STREAM;
import static org.mule.runtime.http.api.HttpConstants.Method.GET;
import static org.mule.runtime.http.api.HttpConstants.Method.POST;

import org.mule.extension.http.api.HttpAttributes;
import org.mule.extension.http.api.HttpResponseAttributes;
import org.mule.extension.ws.api.transport.HttpRequestResponse;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.api.streaming.bytes.CursorStreamProvider;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.api.util.Pair;
import org.mule.runtime.extension.api.client.DefaultOperationParameters;
import org.mule.runtime.extension.api.client.DefaultOperationParametersBuilder;
import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.soap.api.transport.DispatcherException;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Executes an HTTP requester operation using the {@link ExtensionsClient}.
 *
 * @since 1.1
 */
public class ExtensionsClientHttpRequestExecutor {

  private final String requesterConfig;
  private final ExtensionsClient client;

  public ExtensionsClientHttpRequestExecutor(String requesterConfig, ExtensionsClient client) {
    this.requesterConfig = requesterConfig;
    this.client = client;
  }

  public HttpRequestResponse get(String url, Map<String, String> headers) {
    return this.request(GET.toString(), url, headers, null);
  }

  public HttpRequestResponse post(String url, Map<String, String> headers, InputStream body) {
    return this.request(POST.toString(), url, headers, body);
  }

  private HttpRequestResponse request(String method, String url, Map<String, String> headers,
                                      InputStream body) {
    DefaultOperationParametersBuilder params = DefaultOperationParameters
        .builder()
        .configName(requesterConfig)
        .addParameter("method", method)
        .addParameter("url", url)
        .addParameter("headers", new MultiMap<>(headers))
        .addParameter("targetValue", "#[payload]");

    if (body != null) {
      params.addParameter("body", new TypedValue<>(body, INPUT_STREAM));
    }

    try {
      Result result = client.executeAsync("HTTP", "request", params.build()).get();
      InputStream content = getContent(result);
      Map<String, String> httpHeaders = getHttpHeaders(result);
      Map<String, String> statusLine = getStatusLine(result);
      return new HttpRequestResponse(content, httpHeaders, statusLine);
    } catch (ExecutionException e) {
      throw new DispatcherException("Could not dispatch soap message using the [" + requesterConfig + "] HTTP configuration",
                                    e.getCause());
    } catch (InterruptedException e) {
      currentThread().interrupt();
      throw new DispatcherException("Could not dispatch soap message using the [" + requesterConfig + "] HTTP configuration", e);
    }
  }

  private Map<String, String> getHttpHeaders(Result<Object, Object> result) {
    try {
      Optional httpAttributes = result.getAttributes();
      if (!httpAttributes.isPresent()) {
        throw new IllegalStateException("No Http Attributes found on the response, cannot get response headers.");
      } else {
        Map<String, ? extends List<String>> headers = ((HttpAttributes) httpAttributes.get()).getHeaders().toListValuesMap();
        return headers.entrySet().stream().collect(toMap(Map.Entry::getKey, e -> join(" ", e.getValue())));
      }
    } catch (Exception e) {
      throw new IllegalStateException("Something went wrong when introspecting the http response attributes.", e);
    }
  }

  private Map<String, String> getStatusLine(Result<Object, Object> result) {
    try {
      Optional httpResponseAttributes = result.getAttributes();
      if (!httpResponseAttributes.isPresent()) {
        throw new IllegalStateException("No Http Attributes found on the response, cannot get response headers.");
      } else {
        Map<String, String> statusLine = new HashMap<>();
        statusLine.put("statusCode", String.valueOf(((HttpResponseAttributes) httpResponseAttributes.get()).getStatusCode()));
        statusLine.put("reasonPhrase", ((HttpResponseAttributes) httpResponseAttributes.get()).getReasonPhrase());
        return statusLine;
      }
    } catch (Exception e) {
      throw new IllegalStateException("Something went wrong when introspecting the http response attributes.", e);
    }
  }

  private InputStream getContent(Result<Object, Object> result) {
    Object output = result.getOutput();
    if (output instanceof CursorStreamProvider) {
      return ((CursorStreamProvider) output).openCursor();
    } else if (output instanceof InputStream) {
      return (InputStream) output;
    } else {
      throw new IllegalStateException("Content was expected to be an stream but got a [" + output.getClass().getName() + "]");
    }
  }
}
