/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.mule.extension.ws.api.SoapAttributes;
import org.mule.extension.ws.api.SoapOutputEnvelope;
import org.mule.runtime.extension.api.runtime.streaming.StreamingHelper;
import org.mule.soap.api.message.SoapAttachment;
import org.mule.soap.internal.message.DefaultSoapResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SoapAttributesTestCase {

  @Test
  public void toStringAttributes() {
    String result = new SoapAttributes(ImmutableMap.of("Header1", "Value1", "Header2", "Value2")).toString();
    assertThat(result, is("{\n"
        + "  protocolHeaders = [\n"
        + "    Header1:Value1,\n"
        + "    Header2:Value2\n"
        + "  ]"
        + "\n}"));
  }
}
