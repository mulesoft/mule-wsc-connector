/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.runtime.transport;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mule.extension.ws.AllureConstants.WscFeature.WSC_EXTENSION;
import static org.mule.service.soap.SoapTestUtils.assertSimilarXml;
import static org.mule.tck.junit4.matcher.ErrorTypeMatcher.errorType;

import org.mule.extension.ws.AbstractSoapServiceTestCase;
import org.mule.runtime.api.message.Error;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.core.api.exception.MessagingException;
import org.mule.service.soap.server.BasicAuthHttpServer;
import org.mule.service.soap.server.HttpServer;
import org.mule.tck.junit4.rule.SystemProperty;
import org.mule.tck.util.TestConnectivityUtils;
import org.junit.Rule;
import org.junit.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Stories;
import java.util.Optional;

@Features(WSC_EXTENSION)
@Stories({"Operation Execution", "Custom Transport", "Http"})
public class HttpBasicAuthConfigTestCase extends AbstractSoapServiceTestCase {

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
    assertUnauthorizedError(flowRunner("unauthorizedRequest").withPayload(testValues.getEchoResquest()).runExpectingException());
  }

  @Test
  public void authorizedRemoteProtectedWsdl() throws Exception {
    Message message = runFlowWithRequest("authorizedRemoteProtectedWsdl", testValues.getEchoResquest());
    String out = (String) message.getPayload().getValue();
    assertSimilarXml(testValues.getEchoResponse(), out);
  }

  @Test
  public void unauthorizedRemoteProtectedWsdl() throws Exception {
    assertUnauthorizedError(flowRunner("unauthorizedRemoteProtectedWsdl").runExpectingException());
  }

  private void assertUnauthorizedError(MessagingException e) {
    Optional<Error> error = e.getEvent().getError();
    assertThat(error.isPresent(), is(true));
    assertThat(error.get().getErrorType(), errorType("HTTP", "UNAUTHORIZED"));
    assertThat(error.get().getDescription(), containsString("failed: unauthorized (401)"));
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
