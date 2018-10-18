/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.mule.extension.ws.api.SoapAttributes;

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
