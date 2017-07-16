/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.runtime;

import static org.apache.commons.io.IOUtils.copy;
import static org.apache.commons.io.IOUtils.toInputStream;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mule.runtime.api.metadata.MediaType.parse;
import static org.mule.service.soap.SoapTestUtils.assertSimilarXml;
import static org.mule.service.soap.SoapTestXmlValues.DOWNLOAD_ATTACHMENT;
import static org.mule.service.soap.SoapTestXmlValues.UPLOAD_ATTACHMENT;

import org.mule.extension.ws.AbstractSoapServiceTestCase;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.api.message.MultiPartPayload;
import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.api.metadata.MediaType;
import org.mule.runtime.extension.api.soap.SoapAttachment;
import org.mule.tck.junit4.rule.SystemProperty;
import org.junit.Rule;
import org.junit.Test;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

public abstract class AttachmentsTestCase extends AbstractSoapServiceTestCase {

  private static final DataType HTML_DATA_TYPE = DataType.builder().type(InputStream.class).mediaType(parse("text/html")).build();

  public static final String ATTACHMENT_CONTENT = "Some Content";
  public static final String ATTACHMENT_NAME = "attachment.txt";

  @Rule
  public SystemProperty mtom;

  AttachmentsTestCase(Boolean isMtom) {
    mtom = new SystemProperty("mtomEnabled", isMtom.toString());
  }

  @Override
  protected String getConfigurationFile() {
    return "config/attachments.xml";
  }

  @Test
  @Description("Uploads an attachment to the server")
  public void uploadAttachment() throws Exception {
    Message message = flowRunner(UPLOAD_ATTACHMENT)
        .withPayload(testValues.getUploadAttachmentRequest())
        .withVariable("inAttachment", getTestAttachment())
        .withVariable("attachmentContent", ATTACHMENT_CONTENT, HTML_DATA_TYPE)
        .run().getMessage();
    assertSimilarXml((String) message.getPayload().getValue(), testValues.getUploadAttachmentResponse());
  }

  @Test
  @Description("Downloads an attachment from the server")
  public void downloadAttachment() throws Exception {
    Message message = flowRunner(DOWNLOAD_ATTACHMENT).withPayload(testValues.getDownloadAttachmentRequest()).run().getMessage();
    MultiPartPayload multipart = (MultiPartPayload) message.getPayload().getValue();

    List<Message> parts = multipart.getParts();
    assertThat(parts, hasSize(2));
    Message bodyPart = parts.get(0);
    Message attachmentPart = parts.get(1);
    assertDownloadedAttachment(attachmentPart);
  }

  @Step("Checks that the content of the downloaded attachment is correct")
  protected abstract void assertDownloadedAttachment(Message attachmentPart) throws XMLStreamException, IOException;

  @Step("Prepares a test attachment")
  public SoapAttachment getTestAttachment() {
    SoapAttachment attachment = mock(SoapAttachment.class);
    when(attachment.getContent()).thenReturn(toInputStream(ATTACHMENT_CONTENT));
    when(attachment.getContentType()).thenReturn(MediaType.BINARY);
    return attachment;
  }

  String resourceAsString(final String resource) throws XMLStreamException, IOException {
    final InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
    StringWriter writer = new StringWriter();
    copy(is, writer);
    return writer.toString();
  }
}
