/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.extension.ws.internal.value;

import static java.lang.Thread.currentThread;
import static java.util.stream.Collectors.toSet;
import static org.mule.runtime.extension.api.values.ValueBuilder.newValue;
import static org.mule.wsdl.parser.model.WsdlStyle.RPC;

import org.mule.extension.ws.internal.connection.WsdlConnectionInfo;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.values.ValueBuilder;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.extension.api.values.ValueResolvingException;
import org.mule.wsdl.parser.WsdlParser;
import org.mule.wsdl.parser.WsdlTypelessParser;
import org.mule.wsdl.parser.model.PortModel;
import org.mule.wsdl.parser.model.ServiceModel;
import org.mule.wsdl.parser.model.WsdlModel;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * {@link ValueProvider} implementation which provides the possible and supported values for the {@link WsdlConnectionInfo}
 * parameter.
 *
 * @since 1.0
 */
public class WsdlValueProvider implements ValueProvider {

  private static final String RPC_ERROR_MESSAGE = "RPC style WSDLs are not supported by the Web service Consumer";
  private static final String INVALID_VALUE = "INVALID_WSDL_VALUE";

  @Parameter
  private String wsdlLocation;

  @Override
  public Set<Value> resolve() throws ValueResolvingException {
    URL resource = currentThread().getContextClassLoader().getResource(wsdlLocation);
    String wsdl = resource != null ? resource.getPath() : wsdlLocation;
    WsdlModel wsdlModel = WsdlTypelessParser.Companion.parse(wsdl);

    if (wsdlModel.isWsdlStyle(RPC)) {
      throw new ValueResolvingException(RPC_ERROR_MESSAGE, INVALID_VALUE);
    }

    List<ServiceModel> services = wsdlModel.getServices();
    return serviceValues(services);
  }

  private Set<Value> serviceValues(List<ServiceModel> services) {
    Set<Value> values = new HashSet<>();
    for (ServiceModel service : services) {
      ValueBuilder serviceBuilder = newValue(service.getName());
      portValues(service.getPorts()).forEach(serviceBuilder::withChild);
      values.add(serviceBuilder.build());
    }
    return values;
  }

  private Set<ValueBuilder> portValues(List<PortModel> ports) {
    return ports.stream().map(port -> {
      ValueBuilder portBuilder = newValue(port.getName());
      String address = port.getAddress();
      if (address != null) {
        portBuilder.withChild(newValue(address));
      }
      return portBuilder;
    }).collect(toSet());
  }
}
