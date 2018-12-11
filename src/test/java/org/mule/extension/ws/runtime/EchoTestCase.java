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
import static org.mule.extension.ws.SoapTestUtils.assertSimilarXml;
import static org.mule.extension.ws.SoapTestUtils.payloadBodyAsString;
import static org.mule.extension.ws.SoapTestXmlValues.HEADER_IN;
import static org.mule.extension.ws.SoapTestXmlValues.HEADER_INOUT;
import static org.mule.extension.ws.SoapTestXmlValues.HEADER_OUT;
import static org.mule.functional.api.exception.ExpectedError.none;

import org.mule.extension.ws.AbstractWscTestCase;
import org.mule.extension.ws.api.SoapOutputEnvelope;
import org.mule.functional.api.exception.ExpectedError;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.api.streaming.bytes.CursorStreamProvider;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;

@Feature(WSC_EXTENSION)
@Story("Operation Execution")
public class EchoTestCase extends AbstractWscTestCase {

  private static final String ECHO_FLOW = "echoOperation";
  private static final String ECHO_HEADERS_FLOW = "echoWithHeadersOperation";
  private static final String ECHO_ACCOUNT_FLOW = "echoAccountOperation";
  private static final String ECHO_ACCOUNT_DYNAMIC_FLOW = "echoAccountOperation";
  private static final String JSON_RESPONSE = "jsonResponse";
  private static final String ECHO_INVALID_STATIC_HEADERS_FLOW = "echoWithInvalidStaticHeadersOperation";

  @Rule
  public ExpectedError expectedError = none();

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
    Message message = flowRunner(ECHO_HEADERS_FLOW)
        .withVariable(HEADER_IN, testValues.getHeaderIn())
        .withVariable(HEADER_INOUT, testValues.getHeaderInOutRequest())
        .keepStreamsOpen()
        .run()
        .getMessage();

    assertSimilarXml(testValues.getEchoWithHeadersResponse(), payloadBodyAsString(message));

    SoapOutputEnvelope payload = (SoapOutputEnvelope) message.getPayload().getValue();
    assertThat(payload.getHeaders().entrySet(), hasSize(2));

    String inoutHeader = payload.getHeaders().entrySet().stream()
        .filter(h -> h.getKey().equals(HEADER_INOUT))
        .map(h -> h.getValue().getValue()).findFirst().get();
    assertSimilarXml(testValues.getHeaderInOutResponse(), inoutHeader);

    String outHeader = payload.getHeaders().entrySet().stream()
        .filter(h -> h.getKey().equals(HEADER_OUT))
        .map(h -> h.getValue().getValue()).findFirst().get();
    assertSimilarXml(testValues.getHeaderOut(), outHeader);
  }

  @Test
  @Description("Consumes an operation and transforms the whole payload into a json")
  public void responseToJson() throws Exception {
    Object payload = flowRunner(JSON_RESPONSE)
        .withVariable(HEADER_IN, testValues.getHeaderIn())
        .withVariable(HEADER_INOUT, testValues.getHeaderInOutRequest())
        .keepStreamsOpen()
        .run()
        .getMessage()
        .getPayload()
        .getValue();
    String payloadString = IOUtils.toString(((CursorStreamProvider) payload).openCursor());
    assertThat(payloadString, is("{\n"
        + "  \"body\": {\n"
        + "    \"echoWithHeadersResponse\": {\n"
        + "      \"text\": \"test response\"\n"
        + "    }\n"
        + "  },\n"
        + "  \"attachments\": {\n"
        + "    \n"
        + "  },\n"
        + "  \"headers\": {\n"
        + "    \"headerOut\": {\n"
        + "      \"headerOut\": \"Header In Value OUT\"\n"
        + "    },\n"
        + "    \"headerInOut\": {\n"
        + "      \"headerInOut\": \"Header In Out Value INOUT\"\n"
        + "    }\n"
        + "  }\n"
        + "}"));
  }

  @Test
  @Description("Consumes an operation that expects 2 parameters (a simple one and a complex one) and returns a complex type")
  public void echoAccountOperation() throws Exception {
    Message message = runFlowWithRequest(ECHO_ACCOUNT_FLOW, testValues.getEchoAccountRequest());
    assertSimilarXml(testValues.getEchoAccountResponse(), payloadBodyAsString(message));
    SoapOutputEnvelope payload = (SoapOutputEnvelope) message.getPayload().getValue();
    assertThat(payload.getHeaders().isEmpty(), is(true));
  }

  @Test
  @Description("Consumes an operation that uses a dynamic config with a dynamic wsdlLocation")
  public void withDynamicConfiguration() throws Exception {
    Message message = flowRunner(ECHO_ACCOUNT_DYNAMIC_FLOW).withVariable("wsdlLocation", httpServer.getDefaultAddress())
        .withPayload(testValues.getEchoAccountRequest()).keepStreamsOpen().run().getMessage();
    assertSimilarXml(testValues.getEchoAccountResponse(), payloadBodyAsString(message));
    SoapOutputEnvelope payload = (SoapOutputEnvelope) message.getPayload().getValue();
    assertThat(payload.getHeaders().isEmpty(), is(true));
  }

  @Test
  @Description("Consumes an operation that expects an input and a set of static headers badly formed.")
  public void echoWithStaticHeadersOperation() throws Exception {
    expectedError.expectErrorType("WSC", "BAD_REQUEST");
    flowRunner(ECHO_INVALID_STATIC_HEADERS_FLOW)
        .withVariable(HEADER_IN, testValues.getHeaderIn())
        .withVariable(HEADER_INOUT, testValues.getHeaderInOutRequest())
        .keepStreamsOpen()
        .run();
  }

}
