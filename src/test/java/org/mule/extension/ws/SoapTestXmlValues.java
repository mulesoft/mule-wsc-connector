/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws;


public class SoapTestXmlValues {

  public static final String ECHO = "echo";
  public static final String ECHO_HEADERS = "echoWithHeaders";
  public static final String ECHO_ACCOUNT = "echoAccount";
  public static final String NO_PARAMS = "noParams";
  public static final String NO_PARAMS_HEADERS = "noParamsWithHeader";
  public static final String FAIL = "fail";
  public static final String UPLOAD_ATTACHMENT = "uploadAttachment";
  public static final String DOWNLOAD_ATTACHMENT = "downloadAttachment";
  public static final String LARGE = "large";
  public static final String ONE_WAY = "oneWay";

  public static final String HEADER_IN = "headerIn";
  public static final String HEADER_INOUT = "headerInOut";
  public static final String HEADER_OUT = "headerOut";

  private final String ns;

  public SoapTestXmlValues(String ns) {
    this.ns = ns;
  }

  public String getEchoRequest() {
    return buildXml(ECHO, "<text>test</text>");
  }

  public String getEchoResponse() {
    return buildResponseXml(ECHO, "<text>test response</text>");
  }

  public String getEchoAccountRequest() {
    return buildXml(ECHO_ACCOUNT, "<account>\n"
        + "  <id>12</id>\n"
        + "  <items>chocolate</items>\n"
        + "  <items>banana</items>\n"
        + "  <items>dulce de leche</items>\n"
        + "  <startingDate>2016-09-23T00:00:00-03:00</startingDate>\n"
        + "</account>\n"
        + "<name>Juan</name>");
  }

  public String getEchoAccountResponse() {
    return buildResponseXml(ECHO_ACCOUNT, "<account>\n"
        + "  <clientName>Juan</clientName>\n"
        + "  <id>12</id>\n"
        + "  <items>chocolate</items>\n"
        + "  <items>banana</items>\n"
        + "  <items>dulce de leche</items>\n"
        + "  <startingDate>2016-09-23T00:00:00-03:00</startingDate>\n"
        + "</account>");
  }

  public String getEchoWithHeadersRequest() {
    return buildXml(ECHO_HEADERS, "<text>test</text>");
  }

  public String getOneWayRequest() {
    return buildXml("oneWay", "<text>text</text>");
  }

  public String getEchoWithHeadersResponse() {
    return buildResponseXml(ECHO_HEADERS, "<text>test response</text>");
  }

  public String getFailRequest() {
    return buildXml(FAIL, "<text>Fail Message</text>");
  }

  public String getNoParamsRequest() {
    return buildXml(NO_PARAMS, "");
  }

  public String getNoParamsResponse() {
    return buildResponseXml(NO_PARAMS, "<text>response</text>");
  }

  public String getNoParamsWithHeadersRequest() {
    return buildXml(NO_PARAMS_HEADERS, "");
  }

  public String getNoParamsWithHeadersResponse() {
    return buildResponseXml(NO_PARAMS_HEADERS, "<text>Header In Value</text>");
  }

  public String getDownloadAttachmentRequest() {
    return buildXml(DOWNLOAD_ATTACHMENT, "<fileName>attachment.txt</fileName>");
  }

  public String getDownloadAttachmentResponse() {
    return buildResponseXml(DOWNLOAD_ATTACHMENT, "");
  }

  public String getUploadAttachmentRequest() {
    return buildXml(UPLOAD_ATTACHMENT, "");
  }

  public String getUploadAttachmentResponse() {
    return buildResponseXml(UPLOAD_ATTACHMENT, "<result>Ok</result>");
  }

  public String getHeaderIn() {
    return buildXml(HEADER_IN, "Header In Value");
  }

  public String getHeaderInOutRequest() {
    return buildXml(HEADER_INOUT, "Header In Out Value");
  }

  public String getHeaderInOutResponse() {
    return buildXml(HEADER_INOUT, "Header In Out Value INOUT");
  }

  public String getHeaderOut() {
    return buildXml(HEADER_OUT, "Header In Value OUT");
  }

  public String buildXml(String operation, String value) {
    // defined this way <con:OPERATION xmlns:con="NAMESPACE">VALUE<con:OPERATION/>
    return "<con:" + operation + " xmlns:con=\"" + ns + "\">" + value + "</con:" + operation + ">";
  }

  private String buildResponseXml(String operation, String value) {
    return buildXml(operation + "Response", value);
  }
}
