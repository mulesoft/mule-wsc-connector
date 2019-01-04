/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.runtime.transport;

import static org.mule.extension.ws.AllureConstants.WscFeature.WSC_EXTENSION;
import static org.mule.extension.ws.api.SoapVersionAdapter.SOAP11;

import io.qameta.allure.Feature;
import org.junit.Rule;
import org.junit.Test;
import org.mule.extension.ws.AbstractWscTestCase;
import org.mule.extension.ws.service.SlowService;
import org.mule.extension.ws.service.SlowService12;
import org.mule.functional.api.exception.ExpectedError;

@Feature(WSC_EXTENSION)
public class DefaultHttpConfigTestCase extends AbstractWscTestCase {

  @Rule
  public ExpectedError expectedError = ExpectedError.none();

  @Override
  protected String getConfigurationFile() {
    return "config/transport/default-transport.xml";
  }

  @Test
  public void timeoutError() throws Exception {
    expectedError.expectErrorType("WSC", "TIMEOUT");
    runFlowWithRequest("timeoutError", testValues.getEchoRequest());
  }

  @Override
  protected Object getServiceClass() {
    return soapVersion.equals(SOAP11) ? new SlowService() : new SlowService12();
  }
}
