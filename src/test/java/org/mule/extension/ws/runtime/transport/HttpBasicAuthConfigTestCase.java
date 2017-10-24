/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.runtime.transport;

import static org.hamcrest.Matchers.containsString;
import static org.mule.extension.ws.AllureConstants.WscFeature.WSC_EXTENSION;
import static org.mule.service.soap.SoapTestUtils.assertSimilarXml;

import org.mule.extension.ws.AbstractWscTestCase;
import org.mule.functional.api.exception.ExpectedError;
import org.mule.runtime.api.message.Message;
import org.mule.service.soap.server.BasicAuthHttpServer;
import org.mule.service.soap.server.HttpServer;
import org.mule.tck.junit4.rule.SystemProperty;
import org.mule.tck.util.TestConnectivityUtils;

import org.junit.Rule;
import org.junit.Test;

import io.qameta.allure.Feature;
import io.qameta.allure.Stories;
import io.qameta.allure.Story;

@Feature(WSC_EXTENSION)
@Stories({@Story("Operation Execution"), @Story("Custom Transport"), @Story("Http")})
public class HttpBasicAuthConfigTestCase extends AbstractWscTestCase {

  @Rule
  public ExpectedError expected = ExpectedError.none();

  @Rule
  public SystemProperty rule = TestConnectivityUtils.disableAutomaticTestConnectivity();

  @Test
  public void requestWithHttpBasicAuthConfiguration() throws Exception {
    Message message = runFlowWithRequest("basicAuthRequester", testValues.getEchoResquest());
    String out = (String) message.getPayload().getValue();
    assertSimilarXml(testValues.getEchoResponse(), out);
  }

  @Test
  public void requestWithUnauthorizedConfiguration() throws Exception {
    expectUnauthorizedError();
    flowRunner("unauthorizedRequest").withPayload(testValues.getEchoResquest()).run();
  }

  @Test
  public void authorizedRemoteProtectedWsdl() throws Exception {
    Message message = runFlowWithRequest("authorizedRemoteProtectedWsdl", testValues.getEchoResquest());
    String out = (String) message.getPayload().getValue();
    assertSimilarXml(testValues.getEchoResponse(), out);
  }

  @Test
  public void unauthorizedRemoteProtectedWsdl() throws Exception {
    expectUnauthorizedError();
    flowRunner("unauthorizedRemoteProtectedWsdl").run();
  }

  private void expectUnauthorizedError() {
    expected.expectErrorType("HTTP", "UNAUTHORIZED");
    expected.expectMessage(containsString("failed: unauthorized (401)"));
  }

  @Override
  protected HttpServer getServer() throws Exception {
    return new BasicAuthHttpServer(port.getNumber(), buildInInterceptor(), buildOutInterceptor(), getServiceClass());
  }

  @Override
  protected String getConfigurationFile() {
    return "config/transport/basic-auth-http-custom-transport.xml";
  }
}
