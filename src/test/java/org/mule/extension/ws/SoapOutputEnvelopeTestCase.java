/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.mule.extension.ws.api.SoapOutputEnvelope;
import org.mule.runtime.extension.api.runtime.streaming.StreamingHelper;
import org.mule.soap.api.message.SoapAttachment;
import org.mule.soap.internal.message.DefaultSoapResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

public class SoapOutputEnvelopeTestCase {

  private StreamingHelper streamingHelper = getMockedStreamingHelper();

  private StreamingHelper getMockedStreamingHelper() {
    StreamingHelper helper = mock(StreamingHelper.class);
    when(helper.resolveCursorProvider(anyObject())).then(a -> a.getArguments()[0]);
    return helper;
  }

  @Test
  public void toStringOnlyBody() {
    InputStream body = new ByteArrayInputStream("<xml>ABC</xml>".getBytes(UTF_8));
    DefaultSoapResponse response = new DefaultSoapResponse(body, emptyMap(), emptyMap(), emptyMap(), emptyMap(), "text/xml");
    String result = new SoapOutputEnvelope(response, streamingHelper).toString();
    assertThat(result, is("{\n"
        + "body:<xml>ABC</xml>,\n"
        + "headers: [],\n"
        + "attachments: []\n"
        + "}"));
  }

  @Test
  public void toStringFullPayload() {
    InputStream body = new ByteArrayInputStream("<xml>ABC</xml>".getBytes(UTF_8));
    Map<String, String> hs = ImmutableMap.of("header1", "<header1>content</header1>",
                                             "header2", "<header2>content</header2>");
    ByteArrayInputStream dummyContent = new ByteArrayInputStream(new byte[] {});
    Map<String, SoapAttachment> as = ImmutableMap.of("attachment1", new SoapAttachment(dummyContent, "text/json"),
                                                     "attachment2", new SoapAttachment(dummyContent, "text/json"));
    DefaultSoapResponse response = new DefaultSoapResponse(body, hs, emptyMap(), emptyMap(), as, "text/xml");
    String result = new SoapOutputEnvelope(response, streamingHelper).toString();
    assertThat(result, is("{\n"
        + "body:<xml>ABC</xml>,\n"
        + "headers: [\"<header1>content</header1>\",\n"
        + "  \"<header2>content</header2>\"],\n"
        + "attachments: [attachment1, attachment2]\n"
        + "}"));
  }
}
