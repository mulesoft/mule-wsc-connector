/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.mule.extension.ws.api.SoapOutputEnvelope;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.api.streaming.bytes.CursorStreamProvider;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

public class SoapTestUtils {

  public SoapTestUtils() {}

  public static void assertSimilarXml(String expected, String result) throws Exception {
    XMLUnit.setIgnoreWhitespace(true);
    Diff diff = XMLUnit.compareXML(result, expected);
    if (!diff.similar()) {
      System.out.println("Expected xml is:\n");
      System.out.println(prettyPrint(expected));
      System.out.println("########################################\n");
      System.out.println("But got:\n");
      System.out.println(prettyPrint(result));
    }

    Assert.assertThat(diff.similar(), Is.is(true));
  }

  public static void assertSimilarXml(String expected, InputStream result) throws Exception {
    assertSimilarXml(expected, IOUtils.toString(result));
  }

  private static String prettyPrint(String a) throws Exception {
    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    InputSource is = new InputSource();
    is.setCharacterStream(new StringReader(a));
    Document doc = db.parse(is);
    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty("indent", "yes");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    StreamResult result = new StreamResult(new StringWriter());
    DOMSource source = new DOMSource(doc);
    transformer.transform(source, result);
    return result.getWriter().toString();
  }

  public static String payloadBodyAsString(Message message) throws Exception {
    Object payload = message.getPayload().getValue();
    TypedValue<InputStream> body = ((SoapOutputEnvelope) payload).getBody();
    InputStream val =
        body.getValue() instanceof CursorStreamProvider ? ((CursorStreamProvider) body.getValue()).openCursor() : body.getValue();
    return IOUtils.toString(val);
  }
}
