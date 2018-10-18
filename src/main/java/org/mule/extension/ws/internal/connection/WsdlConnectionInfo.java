/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.extension.ws.internal.connection;

import static org.mule.runtime.api.meta.model.display.PathModel.Type.FILE;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.param.display.Path;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.values.ValuePart;

/**
 * Groups together the parameters retrieved from a WSDL that are going to be used for establishing a connection to a SOAP endpoint.
 *
 * @since 1.0
 */
public class WsdlConnectionInfo {

  /**
   * The WSDL file URL remote or local.
   */
  @Placement(order = 1)
  @Parameter
  @Example("http://www.somehost.com/location?wsdl")
  @Path(type = FILE, acceptedFileExtensions = "wsdl", acceptsUrls = true)
  private String wsdlLocation;

  /**
   * The service name.
   */
  @Placement(order = 2)
  @Parameter
  @ValuePart(order = 1)
  private String service;

  /**
   * The port name.
   */
  @Placement(order = 3)
  @Parameter
  @ValuePart(order = 2)
  private String port;

  /**
   * The address of the web service.
   */
  @Parameter
  @Optional
  @Placement(order = 4)
  @ValuePart(order = 3)
  private String address;

  public String getService() {
    return service;
  }

  public String getPort() {
    return port;
  }

  public String getAddress() {
    return address;
  }

  public String getWsdlLocation() {
    return wsdlLocation;
  }
}
