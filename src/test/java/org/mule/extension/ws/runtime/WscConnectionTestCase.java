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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mule.extension.ws.AbstractWscTestCase;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.message.Message;

import java.net.URL;

import static java.lang.Thread.currentThread;
import static org.hamcrest.Matchers.instanceOf;
import static org.mule.extension.ws.AllureConstants.WscFeature.WSC_EXTENSION;
import static org.mule.extension.ws.SoapTestUtils.assertSimilarXml;

@Feature(WSC_EXTENSION)
@Story("Connection")
public class WscConnectionTestCase extends AbstractWscTestCase {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private static final String LOCAL_WSDL_FLOW = "withLocalWsdlConnection";
  private static final String RPC_CONNECTION = "rpcConnection";

  @Override
  protected String getConfigurationFile() {
    return "config/connection.xml";
  }

  @Test
  @Description("Tries to instantiate a connection with an RPC WSDL and fails.")
  public void rpcWsdlFails() throws Exception {
    expectedException.expectMessage("RPC WSDLs are not supported");
    expectedException.expectCause(instanceOf(ConnectionException.class));
    URL wsdl = currentThread().getContextClassLoader().getResource("wsdl/rpc.wsdl");
    flowRunner(RPC_CONNECTION).withVariable("wsdl", wsdl.getPath()).run();
  }

  @Test
  @Description("Consumes an operation using a connection that uses a local .wsdl file")
  public void localWsdlConnection() throws Exception {
    Message msg = flowRunner(LOCAL_WSDL_FLOW)
        .withVariable("req", testValues.getEchoResquest())
        .run().getMessage();
    String out = (String) msg.getPayload().getValue();
    assertSimilarXml(testValues.getEchoResponse(), out);
  }
}
