/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.runtime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mule.extension.ws.AllureConstants.WscFeature.WSC_EXTENSION;
import static org.mule.service.soap.SoapTestUtils.assertSimilarXml;
import static org.mule.service.soap.SoapTestUtils.payloadBodyAsString;
import static org.mule.service.soap.SoapTestXmlValues.HEADER_INOUT;
import static org.mule.service.soap.SoapTestXmlValues.HEADER_OUT;

import org.mule.extension.ws.AbstractSoapServiceTestCase;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.extension.api.soap.SoapOutputPayload;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Test;
import java.util.Map;

@Feature(WSC_EXTENSION)
@Story("Operation Execution")
public class EchoTestCase extends AbstractSoapServiceTestCase {

  private static final String ECHO_FLOW = "echoOperation";
  private static final String ECHO_HEADERS_FLOW = "echoWithHeadersOperation";
  private static final String ECHO_ACCOUNT_FLOW = "echoAccountOperation";

  @Override
  protected String getConfigurationFile() {
    return "config/echo.xml";
  }

  @Test
  @Description("Consumes an operation that expects a simple type and returns a simple type")
  public void echoOperation() throws Exception {
    Message message = runFlowWithRequest(ECHO_FLOW, testValues.getEchoResquest());
    assertSimilarXml(testValues.getEchoResponse(), payloadBodyAsString(message));
  }

  @Test
  @Description("Consumes an operation that expects an input and a set of headers and returns a simple type and a set of headers")
  public void echoWithHeadersOperation() throws Exception {
    Message message = runFlowWithRequest(ECHO_HEADERS_FLOW, testValues.getEchoWithHeadersRequest());

    assertSimilarXml(testValues.getEchoWithHeadersResponse(), payloadBodyAsString(message));

    SoapOutputPayload payload = (SoapOutputPayload) message.getPayload().getValue();
    assertThat(payload.getHeaders().entrySet(), hasSize(2));

    String inoutHeader = payload.getHeaders().entrySet().stream()
        .filter(h -> h.getKey().equals(HEADER_INOUT))
        .map(Map.Entry::getValue).findFirst().get();
    assertSimilarXml(testValues.getHeaderInOutResponse(), inoutHeader);

    String outHeader = payload.getHeaders().entrySet().stream()
        .filter(h -> h.getKey().equals(HEADER_OUT))
        .map(Map.Entry::getValue).findFirst().get();
    assertSimilarXml(testValues.getHeaderOut(), outHeader);
  }

  @Test
  @Description("Consumes an operation that expects 2 parameters (a simple one and a complex one) and returns a complex type")
  public void echoAccountOperation() throws Exception {
    Message message = runFlowWithRequest(ECHO_ACCOUNT_FLOW, testValues.getEchoAccountRequest());
    assertSimilarXml(testValues.getEchoAccountResponse(), payloadBodyAsString(message));
    SoapOutputPayload payload = (SoapOutputPayload) message.getPayload().getValue();
    assertThat(payload.getHeaders().isEmpty(), is(true));
  }
}
