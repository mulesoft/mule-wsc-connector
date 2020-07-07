/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.addressing;

import java.io.Serializable;

/**
 * Web service addressing attributes
 *
 * @since 2.0
 */
public class AddressingAttributes implements Serializable {

  private String messageId;

  public AddressingAttributes() {}

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }
}
