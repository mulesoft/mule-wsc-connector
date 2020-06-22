/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.addressing;

import org.mule.extension.ws.api.addressing.AddressingVersion;

import javax.xml.namespace.QName;

/**
 * Provide WS-Addressing headers QNames of {@link AddressingVersion} 2004-08
 *
 * @since 2.0
 */
public class OldAddressingQNameResolver implements AddressingQNameResolver {

  public static final String WSA_NAMESPACE_NAME =
      "http://schemas.xmlsoap.org/ws/2004/08/addressing";
  private static final String WSA_PREFIX = "wsa";
  private static final String WSA_MESSAGEID_NAME = "MessageID";
  private static final String WSA_TO_NAME = "To";
  private static final String WSA_ACTION_NAME = "Action";
  private static final String WSA_FROM_NAME = "From";
  private static final String WSA_REPLYTO_NAME = "ReplyTo";
  private static final String WSA_FAULTTO_NAME = "FaultTo";
  private static final String WSA_RELATESTO_NAME = "RelatesTo";
  private static final String WSA_RELATIONSHIPTYPE_NAME = "RelationshipType";
  private static final String WSA_ADDRESS_NAME = "Address";

  @Override
  public QName getMessageIDQName() {
    return new QName(WSA_NAMESPACE_NAME, WSA_MESSAGEID_NAME, WSA_PREFIX);
  }

  @Override
  public QName getToQName() {
    return new QName(WSA_NAMESPACE_NAME, WSA_TO_NAME, WSA_PREFIX);
  }

  @Override
  public QName getFromQName() {
    return new QName(WSA_NAMESPACE_NAME, WSA_FROM_NAME, WSA_PREFIX);
  }

  @Override
  public QName getActionQName() {
    return new QName(WSA_NAMESPACE_NAME, WSA_ACTION_NAME, WSA_PREFIX);
  }

  @Override
  public QName getReplyToQName() {
    return new QName(WSA_NAMESPACE_NAME, WSA_REPLYTO_NAME, WSA_PREFIX);
  }

  @Override
  public QName getFaultToQName() {
    return new QName(WSA_NAMESPACE_NAME, WSA_FAULTTO_NAME, WSA_PREFIX);
  }

  @Override
  public QName getRelatesToQName() {
    return new QName(WSA_NAMESPACE_NAME, WSA_RELATESTO_NAME, WSA_PREFIX);
  }

  @Override
  public QName getRelationshipTypeQName() {
    return new QName(WSA_NAMESPACE_NAME, WSA_RELATIONSHIPTYPE_NAME, WSA_PREFIX);
  }

  @Override
  public QName getAddressQName() {
    return new QName(WSA_NAMESPACE_NAME, WSA_ADDRESS_NAME, WSA_PREFIX);
  }
}
