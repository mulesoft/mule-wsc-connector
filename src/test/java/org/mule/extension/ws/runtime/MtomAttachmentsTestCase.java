/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.runtime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mule.extension.ws.AllureConstants.WscFeature.WSC_EXTENSION;
import static org.mule.runtime.soap.api.SoapVersion.SOAP11;

import io.qameta.allure.Stories;
import org.mule.extension.ws.service.Mtom11Service;
import org.mule.extension.ws.service.Mtom12Service;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.core.api.util.IOUtils;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;

@Feature(WSC_EXTENSION)
@Stories({@Story("Attachments"), @Story("MTOM")})
public class MtomAttachmentsTestCase extends AttachmentsTestCase {

  public MtomAttachmentsTestCase() {
    super(true);
  }

  @Override
  protected void assertDownloadedAttachment(Message attachmentPart) throws XMLStreamException, IOException {
    String expectedAttachmentContent = resourceAsString(ATTACHMENT_NAME);
    assertThat(IOUtils.toString((InputStream) attachmentPart.getPayload().getValue()), is(expectedAttachmentContent));
  }

  @Override
  protected Object getServiceClass() {
    return soapVersion.equals(SOAP11) ? new Mtom11Service() : new Mtom12Service();
  }

}
