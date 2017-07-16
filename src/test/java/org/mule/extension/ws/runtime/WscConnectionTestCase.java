/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.runtime;

import static java.lang.Thread.currentThread;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.StringContains.containsString;
import static org.mule.extension.ws.AllureConstants.WscFeature.WSC_EXTENSION;
import static org.mule.service.soap.SoapTestUtils.assertSimilarXml;

import org.mule.extension.ws.AbstractSoapServiceTestCase;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.message.Message;
import org.junit.Test;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.net.URL;

@Feature(WSC_EXTENSION)
@Story("Connection")
public class WscConnectionTestCase extends AbstractSoapServiceTestCase {

  private static final String LOCAL_WSDL_FLOW = "withLocalWsdlConnection";
  private static final String RPC_CONNECTION = "rpcConnection";

  @Override
  protected String getConfigurationFile() {
    return "config/connection.xml";
  }

  @Test
  @Description("Tries to instantiate a connection with an RPC WSDL and fails.")
  public void rpcWsdlFails() throws Exception {
    URL wsdl = currentThread().getContextClassLoader().getResource("wsdl/rpc.wsdl");
    Throwable e = flowRunner(RPC_CONNECTION).withVariable("wsdl", wsdl.getPath()).runExpectingException().getRootCause();
    assertThat(e.getMessage(), containsString("RPC WSDLs are not supported"));
    assertThat(e, instanceOf(ConnectionException.class));
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
