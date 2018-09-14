/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.runtime.wss;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.junit.Test;
import org.mule.runtime.api.message.Message;

import java.util.HashMap;
import java.util.Map;

import static org.mule.extension.ws.AllureConstants.WscFeature.WSC_EXTENSION;
import static org.mule.extension.ws.SoapTestUtils.assertSimilarXml;

@Feature(WSC_EXTENSION)
@Story("WSS")
public class WssTimestampTestCase extends AbstractWebServiceSecurityTestCase {

  private final static String TIMESTAMP = "timestamp";

  public WssTimestampTestCase() {
    super(TIMESTAMP);
  }

  @Override
  protected Interceptor buildInInterceptor() {
    final Map<String, Object> props = new HashMap<>();
    props.put("action", "Timestamp");
    return new WSS4JInInterceptor(props);
  }

  @Override
  protected Interceptor buildOutInterceptor() {
    final Map<String, Object> props = new HashMap<>();
    props.put("action", "Timestamp");
    return new WSS4JOutInterceptor(props);
  }

  @Test
  @Description("Consumes a simple operation ")
  public void expectedSecuredRequest() throws Exception {
    Message message = runFlowWithRequest("less-than-a-second-flow", testValues.getEchoResquest());
    String out = (String) message.getPayload().getValue();
    assertSimilarXml(testValues.getEchoResponse(), out);
  }
}
