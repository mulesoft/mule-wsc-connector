/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.addressing;

import javax.xml.namespace.QName;

/**
 * Provide WS-Addressing headers QNames based on a namespace
 *
 * @since 2.0
 */
public abstract class AbstractAddressingQNameResolver implements AddressingQNameResolver {

  private static final String WSA_PREFIX = "wsa";
  private static final String WSA_MESSAGEID_NAME = "MessageID";
  private static final String WSA_TO_NAME = "To";
  private static final String WSA_ACTION_NAME = "Action";
  private static final String WSA_FROM_NAME = "From";
  private static final String WSA_REPLYTO_NAME = "ReplyTo";
  private static final String WSA_FAULTTO_NAME = "FaultTo";
  private static final String WSA_RELATESTO_NAME = "RelatesTo";

  private final String namespace;

  public AbstractAddressingQNameResolver(String namespace) {
    this.namespace = namespace;
  }

  @Override
  public QName getMessageIDQName() {
    return new QName(namespace, WSA_MESSAGEID_NAME, WSA_PREFIX);
  }

  @Override
  public QName getToQName() {
    return new QName(namespace, WSA_TO_NAME, WSA_PREFIX);
  }

  @Override
  public QName getFromQName() {
    return new QName(namespace, WSA_FROM_NAME, WSA_PREFIX);
  }

  @Override
  public QName getActionQName() {
    return new QName(namespace, WSA_ACTION_NAME, WSA_PREFIX);
  }

  @Override
  public QName getReplyToQName() {
    return new QName(namespace, WSA_REPLYTO_NAME, WSA_PREFIX);
  }

  @Override
  public QName getFaultToQName() {
    return new QName(namespace, WSA_FAULTTO_NAME, WSA_PREFIX);
  }

  @Override
  public QName getRelatesToQName() {
    return new QName(namespace, WSA_RELATESTO_NAME, WSA_PREFIX);
  }
}
