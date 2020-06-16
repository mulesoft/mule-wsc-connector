/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.addressing;

import org.mule.extension.ws.internal.addressing.properties.EndpointReferenceType;
import org.mule.extension.ws.internal.addressing.properties.RelatesToType;
import org.mule.extension.ws.internal.addressing.properties.URIType;

import javax.xml.namespace.QName;

import static java.util.AbstractMap.SimpleImmutableEntry;
import static java.util.Map.Entry;

public class DefaultHeadersVersionEncoder implements HeadersVersionEncoder {

  private final AddressingQNameResolver resolver;
  private final AddressingXmlTransformer transformer;

  public DefaultHeadersVersionEncoder(AddressingQNameResolver resolver, AddressingXmlTransformer transformer) {
    this.resolver = resolver;
    this.transformer = transformer;
  }

  @Override
  public Entry<String, String> encodeMessageIDHeader(URIType messageID, boolean mustUnderstand) {
    QName qname = resolver.getMessageIDQName();
    String xml = transformer.transformToXml(messageID, qname, mustUnderstand);
    return entry(qname.getLocalPart(), xml);
  }

  @Override
  public Entry<String, String> encodeToHeader(URIType to, boolean mustUnderstand) {
    QName qname = resolver.getToQName();
    String xml = transformer.transformToXml(to, qname, mustUnderstand);
    return entry(qname.getLocalPart(), xml);
  }

  @Override
  public Entry<String, String> encodeActionHeader(URIType action, boolean mustUnderstand) {
    QName qname = resolver.getActionQName();
    String xml = transformer.transformToXml(action, qname, mustUnderstand);
    return entry(qname.getLocalPart(), xml);
  }

  @Override
  public Entry<String, String> encodeFromHeader(EndpointReferenceType from, boolean mustUnderstand) {
    QName qname = resolver.getFromQName();
    String xml = transformer.transformToXml(from, qname, mustUnderstand);
    return entry(qname.getLocalPart(), xml);
  }

  @Override
  public Entry<String, String> encodeReplyToHeader(EndpointReferenceType replyTo, boolean mustUnderstand) {
    QName qname = resolver.getReplyToQName();
    String xml = transformer.transformToXml(replyTo, qname, mustUnderstand);
    return entry(qname.getLocalPart(), xml);
  }

  @Override
  public Entry<String, String> encodeFaultToHeader(EndpointReferenceType faultTo, boolean mustUnderstand) {
    QName qname = resolver.getFaultToQName();
    String xml = transformer.transformToXml(faultTo, qname, mustUnderstand);
    return entry(qname.getLocalPart(), xml);
  }

  @Override
  public Entry<String, String> encodeRelatesToHeader(RelatesToType relatesTo, boolean mustUnderstand) {
    QName qname = resolver.getRelatesToQName();
    String xml = transformer.transformToXml(relatesTo, qname, mustUnderstand);
    return entry(qname.getLocalPart(), xml);
  }

  private Entry<String, String> entry(String key, String value) {
    return new SimpleImmutableEntry(key, value);
  }
}
