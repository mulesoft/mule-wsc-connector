/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.reliablemessaging;

import org.mule.soap.api.message.ReliableMessagingProperties;

/**
 * Model of Reliable Messaging Properties
 *
 * @since 1.7
 */
public class ReliableMessagingPropertiesImpl implements ReliableMessagingProperties {

  private final String sequenceIdentifier;

  public ReliableMessagingPropertiesImpl(String sequenceIdentifier) {
    this.sequenceIdentifier = sequenceIdentifier;
  }

  @Override
  public String getSequenceIdentifier() {
    return sequenceIdentifier;
  }
}
