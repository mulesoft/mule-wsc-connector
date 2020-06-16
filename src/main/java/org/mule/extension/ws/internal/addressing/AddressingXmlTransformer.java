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

public interface AddressingXmlTransformer {

  String transformToXml(EndpointReferenceType endpoint, QName qname, boolean mustUnderstand);

  String transformToXml(URIType uri, QName qname, boolean mustUnderstand);

  String transformToXml(RelatesToType relatesTo, QName qname, boolean mustUnderstand);
}
