/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.runtime.wss;

import static java.lang.String.format;
import static org.mule.extension.ws.SoapTestUtils.assertSimilarXml;

import io.qameta.allure.Description;
import org.junit.Test;
import org.mule.extension.ws.AbstractWscTestCase;
import org.mule.runtime.api.message.Message;

public abstract class AbstractWebServiceSecurityTestCase extends AbstractWscTestCase {

  private final String security;

  AbstractWebServiceSecurityTestCase(String security) {
    this.security = security;
  }

  @Override
  protected String getConfigurationFile() {
    return format("config/wss/%s.xml", security);
  }

  @Test
  @Description("Consumes a simple operation of a secured web service and expects a valid response")
  public void expectedSecuredRequest() throws Exception {
    Message message = runFlowWithRequest(security + "Flow", testValues.getEchoRequest());
    String out = (String) message.getPayload().getValue();
    assertSimilarXml(testValues.getEchoResponse(), out);
  }
}
