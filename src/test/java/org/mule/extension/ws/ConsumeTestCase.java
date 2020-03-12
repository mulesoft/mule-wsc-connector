/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.mule.extension.ws.api.transport.CustomTransportConfiguration;
import org.mule.extension.ws.internal.connection.WscSoapClient;
import org.mule.extension.ws.internal.connection.WsdlConnectionInfo;
import org.mule.runtime.core.api.util.func.CheckedSupplier;
import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.soap.api.client.SoapClient;
import org.mule.soap.api.message.SoapRequest;
import org.mule.soap.api.message.SoapResponse;
import org.mule.soap.api.transport.TransportDispatcher;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

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
}
