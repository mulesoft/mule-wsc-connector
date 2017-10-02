/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.runtime;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertThat;
import static org.mule.extension.ws.AllureConstants.WscFeature.WSC_EXTENSION;
import static org.mule.runtime.soap.api.SoapVersion.SOAP11;

import org.mule.extension.ws.AbstractSoapServiceTestCase;
import org.mule.functional.api.exception.ExpectedError;
import org.mule.runtime.soap.api.exception.BadRequestException;
import org.mule.runtime.soap.api.exception.SoapFaultException;

import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Stories;
import io.qameta.allure.Story;

@Feature(WSC_EXTENSION)
@Stories({@Story("Operation Execution"), @Story("Soap Fault")})
public class SoapFaultTestCase extends AbstractSoapServiceTestCase {

  private static final String FAIL_FLOW = "failOperation";

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
    expected.expectCause(allOf(instanceOf(SoapFaultException.class),
                               new TypeSafeMatcher<SoapFaultException>(SoapFaultException.class) {

                                 @Override
                                 protected boolean matchesSafely(SoapFaultException sf) {
                                   // Receiver is for SOAP12. Server is for SOAP11
                                   assertThat(sf.getFaultCode().getLocalPart(), isOneOf("Server", "Receiver"));
                                   assertThat(sf.getMessage(), containsString("Fail Message"));

                                   return true;
                                 }

                                 @Override
                                 public void describeTo(org.hamcrest.Description description) {}
                               }));

    flowRunner(FAIL_FLOW).withPayload(testValues.getFailRequest()).run();
  }

  @Test
  @Description("Consumes an operation that does not exist and throws a SOAP Fault because of it and asserts the thrown exception")
  public void noExistentOperation() throws Exception {
    String badRequest = "<con:noOperation xmlns:con=\"http://service.soap.service.mule.org/\"/>";

    expected.expectErrorType("WSC", SOAP_FAULT);
    if (soapVersion.equals(SOAP11)) {
      expected.expectMessage(containsString("{http://service.soap.service.mule.org/}noOperation was not recognized"));
    } else {
      expected.expectMessage(containsString("Unexpected wrapper element {http://service.soap.service.mule.org/}noOperation"));
    }
    expected.expectCause(allOf(instanceOf(SoapFaultException.class),
                               new TypeSafeMatcher<SoapFaultException>(SoapFaultException.class) {

                                 @Override
                                 protected boolean matchesSafely(SoapFaultException sf) {
                                   // Sender is for SOAP12. Client is for SOAP11
                                   assertThat(sf.getFaultCode().getLocalPart(), isOneOf("Client", "Sender"));

                                   return true;
                                 }

                                 @Override
                                 public void describeTo(org.hamcrest.Description description) {}
                               }));

    flowRunner(FAIL_FLOW).withPayload(badRequest).run();
  }

  @Test
  @Description("Consumes an operation with a body that is not a valid XML")
  public void echoBodyIsNotValidXml() throws Exception {
    expected.expectErrorType("WSC", BAD_REQUEST);
    expected.expectMessage(is("Error consuming the operation [fail], the request body is not a valid XML"));
    expected.expectCause(instanceOf(BadRequestException.class));

    flowRunner(FAIL_FLOW).withPayload("not a valid XML file").run();
  }
}
