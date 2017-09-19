/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.runtime;

import static org.mule.extension.ws.AllureConstants.WscFeature.WSC_EXTENSION;
import static org.mule.runtime.soap.api.SoapVersion.SOAP11;

import org.mule.service.soap.service.Mtom11Service;
import org.mule.service.soap.service.Mtom12Service;

import io.qameta.allure.Feature;
import io.qameta.allure.Stories;
import io.qameta.allure.Story;

@Feature(WSC_EXTENSION)
@Stories({@Story("Attachments"), @Story("MTOM")})
public class MtomAttachmentsTestCase extends AttachmentsTestCase {

  public MtomAttachmentsTestCase() {
    super(true, "attachment.txt");
  }

  @Override
  protected Object getServiceClass() {
    return soapVersion.equals(SOAP11) ? new Mtom11Service() : new Mtom12Service();
  }
}
