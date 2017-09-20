/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws;

import static java.lang.Thread.currentThread;
import static java.util.Arrays.asList;
import static org.mule.runtime.soap.api.SoapVersion.SOAP11;
import static org.mule.runtime.soap.api.SoapVersion.SOAP12;
import static org.mule.service.soap.SoapTestXmlValues.HEADER_IN;
import static org.mule.service.soap.SoapTestXmlValues.HEADER_INOUT;

import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.soap.api.SoapVersion;
import org.mule.service.soap.SoapTestXmlValues;
import org.mule.service.soap.server.HttpServer;
import org.mule.service.soap.service.Soap11Service;
import org.mule.service.soap.service.Soap12Service;
import org.mule.tck.junit4.rule.DynamicPort;
import org.mule.tck.junit4.rule.SystemProperty;
import org.mule.test.runner.RunnerDelegateTo;

import java.util.Collection;

import org.apache.cxf.interceptor.Interceptor;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Rule;
import org.junit.runners.Parameterized;

@RunnerDelegateTo(Parameterized.class)
public abstract class AbstractSoapServiceTestCase extends MuleArtifactFunctionalTestCase {

  public final SoapTestXmlValues testValues = new SoapTestXmlValues("http://service.soap.service.mule.org/");

  @Rule
  public DynamicPort port = new DynamicPort("servicePort");

  @Rule
  public SystemProperty humanWsdlPath;

  @Parameterized.Parameter
  public SoapVersion soapVersion = SOAP11;

  @Parameterized.Parameter(1)
  public Object serviceClass = new Soap11Service();

  protected HttpServer httpServer;

  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return asList(new Object[][] {
        {SOAP11, new Soap11Service()},
        {SOAP12, new Soap12Service()}
    });
  }

  @Override
  protected String[] getConfigFiles() {
    return new String[] {getConfigurationFile()};
  }

  @Override
  protected void doSetUpBeforeMuleContextCreation() throws Exception {
    super.doSetUpBeforeMuleContextCreation();
    System.setProperty("humanWsdl", currentThread().getContextClassLoader().getResource("wsdl/human.wsdl").getPath());
    System.setProperty("soapVersion", soapVersion.toString());
    System.setProperty("serviceClass", getServiceClass().getClass().getName());
    XMLUnit.setIgnoreWhitespace(true);
    httpServer = getServer();
  }

  protected HttpServer getServer() throws Exception {
    return new HttpServer(port.getNumber(), buildInInterceptor(), buildOutInterceptor(), getServiceClass());
  }

  protected Message runFlowWithRequest(String flowName, String request) throws Exception {
    return flowRunner(flowName)
        .withPayload(request)
        .withVariable(HEADER_IN, testValues.getHeaderIn())
        .withVariable(HEADER_INOUT, testValues.getHeaderInOutRequest())
        .keepStreamsOpen()
        .run()
        .getMessage();
  }

  protected abstract String getConfigurationFile();

  protected Object getServiceClass() {
    return serviceClass;
  }

  protected Interceptor buildInInterceptor() {
    return null;
  }

  protected Interceptor buildOutInterceptor() {
    return null;
  }

  @Override
  protected void doTearDownAfterMuleContextDispose() throws Exception {
    httpServer.stop();
  }
}
