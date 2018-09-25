/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.runtime;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Test;
import org.mule.extension.ws.AbstractWscTestCase;
import org.mule.runtime.api.message.Message;

import static org.mule.extension.ws.AllureConstants.WscFeature.WSC_EXTENSION;
import static org.mule.extension.ws.SoapTestUtils.assertSimilarXml;

@Feature(WSC_EXTENSION)
@Story("Operation Execution")
public class NoParamsTestCase extends AbstractWscTestCase {

  private static final String NO_PARAMS_FLOW = "noParamsOperation";

  @Override
  protected String getConfigurationFile() {
    return "config/noParams.xml";
  }

  @Test
  @Description("Consumes an operation that expects no parameters and returns a simple type")
  public void noParamsOperation() throws Exception {
    Message message = runFlowWithRequest(NO_PARAMS_FLOW, testValues.getNoParamsRequest());
    String payload = (String) message.getPayload().getValue();
    assertSimilarXml(testValues.getNoParamsResponse(), payload);
  }

  @Test
  @Description("Consumes an operation that expects no parameters and returns a simple type")
  public void noParamsOperationNoPayload() throws Exception {
    Message message = flowRunner(NO_PARAMS_FLOW).run().getMessage();
    String payload = (String) message.getPayload().getValue();
    assertSimilarXml(testValues.getNoParamsResponse(), payload);
  }
}
