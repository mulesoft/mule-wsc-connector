/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import org.mule.extension.ws.api.SoapOutputEnvelope;
import org.mule.runtime.extension.api.runtime.streaming.StreamingHelper;
import org.mule.soap.api.message.SoapAttachment;
import org.mule.soap.internal.message.DefaultSoapResponse;

public class SoapOutputEnvelopeTestCase {

  private StreamingHelper streamingHelper = getMockedStreamingHelper();

  private StreamingHelper getMockedStreamingHelper() {
    StreamingHelper helper = mock(StreamingHelper.class);
    when(helper.resolveCursorProvider(any())).then(a -> a.getArguments()[0]);
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

    Map<String, String> hs = unmodifiableMap(new TreeMap(of(new SimpleEntry<>("header1", "<header1>content</header1>"),
                                                            new SimpleEntry<>("header2", "<header2>content</header2>"))
                                                                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue))));

    ByteArrayInputStream dummyContent = new ByteArrayInputStream(new byte[] {});
    Map<String, SoapAttachment> as =
        unmodifiableMap(new TreeMap(of(
                                       new SimpleEntry<>("attachment1", new SoapAttachment(dummyContent, "text/json")),
                                       new SimpleEntry<>("attachment2", new SoapAttachment(dummyContent, "text/json")))
                                           .collect(toMap(Map.Entry::getKey, Map.Entry::getValue))));

    DefaultSoapResponse response = new DefaultSoapResponse(body, hs, emptyMap(), emptyMap(), as, "text/xml");
    String result = new SoapOutputEnvelope(response, streamingHelper).toString();
    assertThat(result, is("{\n"
        + "body:<xml>ABC</xml>,\n"
        + "headers: ["
        + "\"<header2>content</header2>\",\n"
        + "  \"<header1>content</header1>\"],\n"
        + "attachments: [attachment2, attachment1]\n"
        + "}"));
  }
}
