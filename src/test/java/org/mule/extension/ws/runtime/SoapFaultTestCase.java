/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.runtime;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertThat;
import static org.mule.extension.ws.AllureConstants.WscFeature.WSC_EXTENSION;
import static org.mule.runtime.soap.api.SoapVersion.SOAP11;
import static org.mule.tck.junit4.matcher.ErrorTypeMatcher.errorType;
import org.mule.extension.ws.AbstractSoapServiceTestCase;
import org.mule.runtime.api.message.Error;
import org.mule.runtime.core.api.exception.EventProcessingException;
import org.mule.runtime.soap.api.exception.BadRequestException;
import org.mule.runtime.soap.api.exception.SoapFaultException;
import org.mule.tck.junit4.matcher.ErrorTypeMatcher;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Stories;
import io.qameta.allure.Story;
import org.hamcrest.Matchers;
import org.junit.Test;

@Feature(WSC_EXTENSION)
@Stories({@Story("Operation Execution"), @Story("Soap Fault")})
public class SoapFaultTestCase extends AbstractSoapServiceTestCase {

  private static final String FAIL_FLOW = "failOperation";

  // TODO MULE-12038
  private static final String SOAP_FAULT = "SOAP_FAULT";
  private static final String BAD_REQUEST = "BAD_REQUEST";

  @Override
  protected String getConfigurationFile() {
    return "config/fail.xml";
  }

  @Test
  @Description("Consumes an operation that throws a SOAP Fault and expects a Soap Fault Exception")
  public void failOperation() throws Exception {
    EventProcessingException me = flowRunner(FAIL_FLOW).withPayload(testValues.getFailRequest()).runExpectingException();
    Error error = me.getEvent().getError().get();

    assertThat(error.getErrorType(), is(errorType("WSC", SOAP_FAULT)));

    Throwable causeException = error.getCause();
    assertThat(causeException, instanceOf(SoapFaultException.class));
    SoapFaultException sf = (SoapFaultException) causeException;

    // Receiver is for SOAP12. Server is for SOAP11
    assertThat(sf.getFaultCode().getLocalPart(), isOneOf("Server", "Receiver"));
    assertThat(sf.getMessage(), containsString("Fail Message"));
  }

  @Test
  @Description("Consumes an operation that does not exist and throws a SOAP Fault because of it and asserts the thrown exception")
  public void noExistentOperation() throws Exception {
    String badRequest = "<con:noOperation xmlns:con=\"http://service.soap.service.mule.org/\"/>";
    EventProcessingException me = flowRunner(FAIL_FLOW).withPayload(badRequest).runExpectingException();

    Error error = me.getEvent().getError().get();

    assertThat(error.getErrorType(), Matchers.is(ErrorTypeMatcher.errorType("WSC", SOAP_FAULT)));
    assertThat(error.getCause(), instanceOf(SoapFaultException.class));
    SoapFaultException sf = (SoapFaultException) error.getCause();

    // Sender is for SOAP12. Client is for SOAP11
    assertThat(sf.getFaultCode().getLocalPart(), isOneOf("Client", "Sender"));

    String errorMessage;
    if (soapVersion.equals(SOAP11)) {
      errorMessage = "{http://service.soap.service.mule.org/}noOperation was not recognized";
    } else {
      errorMessage = "Unexpected wrapper element {http://service.soap.service.mule.org/}noOperation";
    }
    assertThat(sf.getMessage(), containsString(errorMessage));
  }

  @Test
  @Description("Consumes an operation with a body that is not a valid XML")
  public void echoBodyIsNotValidXml() throws Exception {
    EventProcessingException me = flowRunner(FAIL_FLOW).withPayload("not a valid XML file").runExpectingException();
    Error error = me.getEvent().getError().get();

    assertThat(error.getErrorType(), is(errorType("WSC", BAD_REQUEST)));
    assertThat(error.getCause(), instanceOf(BadRequestException.class));
    assertThat(error.getCause().getMessage(), is("Error consuming the operation [fail], the request body is not a valid XML"));
  }
}
