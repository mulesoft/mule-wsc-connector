/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.runtime.transport;

import static org.mule.extension.ws.AllureConstants.WscFeature.WSC_EXTENSION;
import static org.mule.extension.ws.SoapTestUtils.assertSimilarXml;

import io.qameta.allure.Feature;
import io.qameta.allure.Stories;
import io.qameta.allure.Story;
import org.junit.Test;
import org.mule.extension.ws.AbstractWscTestCase;
import org.mule.runtime.api.message.Message;

@Feature(WSC_EXTENSION)
@Stories({@Story("Operation Execution"), @Story("Custom Transport"), @Story("Http")})
public class SimpleHttpConfigTestCase extends AbstractWscTestCase {

  @Test
  public void simpleConfigNoAuthentication() throws Exception {
    Message message = runFlowWithRequest("simpleRequesterConfig", testValues.getEchoResquest());
    String out = (String) message.getPayload().getValue();
    assertSimilarXml(testValues.getEchoResponse(), out);
  }

  @Override
  protected String getConfigurationFile() {
    return "config/transport/simple-http-custom-transport.xml";
  }
}
