package org.mule.extension.ws.internal.connection;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.values.ValuePart;

/**
 * Groups together the parameters retrieved from a WSDL that are going to be used for establishing a connection to a SOAP endpoint.
 *
 * @since 1.0
 */
public class WsdlConnectionInfo {

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
}
