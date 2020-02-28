/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws;

import static java.util.Collections.unmodifiableMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.TreeMap;

import org.junit.Test;

import org.mule.extension.ws.api.SoapAttributes;

public class SoapAttributesTestCase {

  @Test
  public void toStringAttributes() {
    String result = new SoapAttributes(
                                       unmodifiableMap(new TreeMap<String, String>() {

                                         {
                                           put("Header1", "Value1");
                                           put("Header2", "Value2");
                                         }
                                       }),
                                       unmodifiableMap(new TreeMap<String, String>() {

                                         {
                                           put("statusCode", "200");
                                           put("reasonPhrase", "OK");
                                         }
                                       }))
                                           .toString();

    assertThat(result, is("{\n"
        + "  additionalTransportData = [\n"
        + "    reasonPhrase:OK,\n"
        + "    statusCode:200\n"
        + "  ]\n"
        + "  protocolHeaders = [\n"
        + "    Header1:Value1,\n"
        + "    Header2:Value2\n"
        + "  ]"
        + "\n}"));
  }
}
