/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.runtime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mule.extension.ws.AllureConstants.WscFeature.WSC_EXTENSION;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Stories;
import io.qameta.allure.Story;
import org.junit.Rule;
import org.junit.Test;
import org.mule.extension.ws.AbstractWscTestCase;
import org.mule.functional.api.exception.ExpectedError;
import org.mule.runtime.core.api.event.CoreEvent;

@Feature(WSC_EXTENSION)
@Stories({@Story("Operation Execution"), @Story("Soap Fault")})
public class SoapFaultTestCase extends AbstractWscTestCase {

  private static final String FAIL_FLOW = "failOperation";
  private static final String NO_OP_FLOW = "noOperation";

  // TODO MULE-12038
  private static final String SOAP_FAULT = "SOAP_FAULT";
  private static final String BAD_REQUEST = "BAD_REQUEST";

  @Rule
  public ExpectedError expected = ExpectedError.none();

  @Override
  protected String getConfigurationFile() {
    return "config/fail.xml";
  }

  @Test
  @Description("Consumes an operation that throws a SOAP Fault and expects a Soap Fault Exception")
  public void failOperation() throws Exception {
    expected.expectErrorType("WSC", SOAP_FAULT);
    flowRunner(FAIL_FLOW).withPayload(testValues.getFailRequest()).run();
  }

  @Test
  public void failAndCheckErrorMessage() throws Exception {
    CoreEvent failAndCheckPayload = flowRunner("failAndCheckErrorMessage").withPayload(testValues.getFailRequest()).run();
    assertThat(failAndCheckPayload.getMessage().getPayload().getValue().toString(), containsString("<text>Fail Message</text>"));
  }

  @Test
  @Description("Consumes an operation that does not exist and throws a SOAP Fault because of it and asserts the thrown exception")
  public void noExistentOperation() throws Exception {
    expected.expectErrorType("WSC", BAD_REQUEST);
    expected.expectMessage(containsString("[noOperation] does not exist in the WSDL file"));
    flowRunner(NO_OP_FLOW).withPayload("<con:noOperation xmlns:con=\"http://service.ws.extension.mule.org/\"/>").run();
  }

  @Test
  @Description("Consumes an operation with a body that is not a valid XML")
  public void echoBodyIsNotValidXml() throws Exception {
    expected.expectErrorType("WSC", BAD_REQUEST);
    expected.expectMessage(is("Error consuming the operation [fail], the request body is not a valid XML"));

    flowRunner(FAIL_FLOW).withPayload("not a valid XML file").run();
  }
}
