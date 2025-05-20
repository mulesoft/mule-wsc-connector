/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api;

import org.mule.soap.api.SoapVersion;

/**
 * The possible SOAP versions of a SOAP service.
 *
 * @since 1.1
 */
public enum SoapVersionAdapter {

  SOAP11(SoapVersion.SOAP_11), SOAP12(SoapVersion.SOAP_12);

  private SoapVersion version;

  SoapVersionAdapter(SoapVersion version) {
    this.version = version;
  }

  public SoapVersion getVersion() {
    return version;
  }
}
