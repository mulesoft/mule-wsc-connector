/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.mock;
import static org.hamcrest.MatcherAssert.assertThat;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.mule.extension.ws.api.transport.CustomTransportConfiguration;
import org.mule.extension.ws.internal.connection.DefaultHttpMessageDispatcher;
import org.mule.extension.ws.internal.connection.WscSoapClient;
import org.mule.extension.ws.internal.connection.WsdlConnectionInfo;
import org.mule.runtime.core.api.util.func.CheckedSupplier;
import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.client.auth.HttpAuthentication;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import org.mule.soap.api.client.SoapClient;
import org.mule.soap.api.message.SoapRequest;
import org.mule.soap.api.message.SoapResponse;
import org.mule.soap.api.transport.TransportDispatcher;
import org.mule.soap.api.transport.TransportRequest;
import org.mule.soap.api.transport.TransportResponse;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;

public class ConsumeTestCase {

  private ExecutorService executorService;

  @Before
  public void before() {
    executorService = newFixedThreadPool(2);
  }

  @After
  public void after() throws Exception {
    if (executorService != null) {
      executorService.shutdownNow();
      executorService.awaitTermination(5, SECONDS);
    }
  }

  @Test
  public void consumeConcurrently() {
    WsdlConnectionInfo info = mock(WsdlConnectionInfo.class);
    CountDownLatch latch = new CountDownLatch(1);
    CustomTransportConfiguration configuration = mock(CustomTransportConfiguration.class);
    ExtensionsClient extensionsClient = mock(ExtensionsClient.class);
    SoapClient soapClient = mock(SoapClient.class);
    SoapRequest soapRequest = mock(SoapRequest.class);
    CheckedSupplier<SoapClient> supplier = mock(CheckedSupplier.class);

    when(soapClient.consume(any(SoapRequest.class), any(TransportDispatcher.class))).thenReturn(mock(SoapResponse.class));
    when(supplier.get()).then(new Answer<SoapClient>() {

      @Override
      public SoapClient answer(InvocationOnMock invocation) throws Throwable {
        latch.await();
        return soapClient;
      }
    });

    WscSoapClient client = new WscSoapClient(info, supplier, configuration, null);

    executorService.submit(() -> client.consume(soapRequest, extensionsClient));
    executorService.submit(() -> client.consume(soapRequest, extensionsClient));

    latch.countDown();

    verify(supplier, times(1)).get();
  }

  @Test
  public void consumeDefaultContenType() throws IOException, TimeoutException {
    HttpClient mockClient = mock(HttpClient.class);
    HttpResponse response = HttpResponse.builder().statusCode(500).build();
    when(mockClient.send(any(HttpRequest.class), anyInt(), anyBoolean(), any(HttpAuthentication.class))).thenReturn(response);
    TransportDispatcher dispatcher = new DefaultHttpMessageDispatcher(mockClient, 1000);
    TransportRequest transportRequest = new TransportRequest(mock(InputStream.class), "localhost", new HashMap<>());
    TransportResponse transportResponse = dispatcher.dispatch(transportRequest);
    assertThat(transportResponse.getContentType(), is("application/xml"));
  }

}
