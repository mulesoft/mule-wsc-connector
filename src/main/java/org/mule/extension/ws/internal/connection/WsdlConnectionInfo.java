/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.extension.ws.internal.connection;

import static java.lang.Thread.currentThread;
import static org.mule.runtime.api.meta.model.display.PathModel.Type.FILE;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.param.display.Path;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.values.ValuePart;
import org.mule.sdk.api.annotation.semantics.connectivity.Url;

import java.net.URL;
import java.util.Objects;

/**
 * Groups together the parameters retrieved from a WSDL that are going to be used for establishing a connection to a SOAP
 * endpoint.
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
  @Url
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
  @Url
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

  public String getAbsoluteWsdlLocation() {
    URL resource = currentThread().getContextClassLoader().getResource(wsdlLocation);
    return resource != null ? resource.toString() : wsdlLocation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    WsdlConnectionInfo that = (WsdlConnectionInfo) o;
    return Objects.equals(wsdlLocation, that.wsdlLocation) &&
        Objects.equals(service, that.service) &&
        Objects.equals(port, that.port) &&
        Objects.equals(address, that.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wsdlLocation, service, port, address);
  }
}
