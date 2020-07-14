/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.reliablemessaging;

import org.mule.extension.ws.api.addressing.AddressingVersion;

import static org.mule.extension.ws.api.addressing.AddressingVersion.WSA200408;
import static org.mule.extension.ws.api.addressing.AddressingVersion.WSA200508;

public enum ReliableMessagingVersion {

  WSRM_12_WSA_200508(WSA200508, "1.2"),
  WSRM_11_WSA_200508(WSA200508, "1.1"),
  WSRM_11_WSA_200408(WSA200408, "1.1"),
  WSRM_10_WSA_200408(WSA200408, "1.0");

  private final AddressingVersion addressingVersion;
  private final String version;

  ReliableMessagingVersion(AddressingVersion addressingVersion, String version) {
    this.addressingVersion = addressingVersion;
    this.version = version;
  }

  public AddressingVersion getAddressingVersion() {
    return addressingVersion;
  }

  public String getVersion() {
    return version;
  }
}
