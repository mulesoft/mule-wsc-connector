/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws;

import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.junit.Test;

import org.mule.extension.ws.api.SoapAttributes;
import org.mule.extension.ws.api.addressing.AddressingAttributes;

public class SoapAttributesTestCase {

  @Test
  public void toStringAttributes() {

    String result = new SoapAttributes(
                                       unmodifiableMap(new TreeMap(of(new SimpleEntry<>("Header1", "Value1"),
                                                                      new SimpleEntry<>("Header2", "Value2"))
                                                                          .collect(toMap(Entry::getKey, Entry::getValue)))),
                                       unmodifiableMap(new TreeMap(of(new SimpleEntry<>("statusCode", "200"),
                                                                      new SimpleEntry<>("reasonPhrase", "OK"))
                                                                          .collect(toMap(Entry::getKey, Entry::getValue)))))
                                                                              .toString();

    assertThat(result, is("{\n"
        + "  additionalTransportData = [\n"
        + "    reasonPhrase:OK,\n"
        + "    statusCode:200\n"
        + "  ]\n"
        + "  protocolHeaders = [\n"
        + "    Header1:Value1,\n"
        + "    Header2:Value2\n"
        + "  ]\n"
        + "}\n"));
  }

  @Test
  public void toStringAttributesWithMessageID() {

    AddressingAttributes addressing = new AddressingAttributes("12345");
    String result = new SoapAttributes(
                                       unmodifiableMap(new TreeMap(of(new SimpleEntry<>("Header1", "Value1"),
                                                                      new SimpleEntry<>("Header2", "Value2"))
                                                                          .collect(toMap(Entry::getKey, Entry::getValue)))),
                                       unmodifiableMap(new TreeMap(of(new SimpleEntry<>("statusCode", "200"),
                                                                      new SimpleEntry<>("reasonPhrase", "OK"))
                                                                          .collect(toMap(Entry::getKey, Entry::getValue)))),
                                       addressing)
                                           .toString();

    assertThat(result, is("{\n"
        + "  additionalTransportData = [\n"
        + "    reasonPhrase:OK,\n"
        + "    statusCode:200\n"
        + "  ]\n"
        + "  protocolHeaders = [\n"
        + "    Header1:Value1,\n"
        + "    Header2:Value2\n"
        + "  ]\n"
        + "  addressing = {\n"
        + "    messageId:12345\n"
        + "  }\n"
        + "}\n"));
  }
}
