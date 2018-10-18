/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(portName = "TestPort", serviceName = "TestService")
public class SlowService {

  @WebResult(name = "text")
  @WebMethod(action = "echoOperationCustomAction")
  public String echo(@WebParam(name = "text") String s) {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      // do nothing
    }
    return s + " response";
  }
}
