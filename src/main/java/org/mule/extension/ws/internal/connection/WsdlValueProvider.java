package org.mule.extension.ws.internal.connection;

import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.values.ValueBuilder;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.extension.api.values.ValueResolvingException;
import org.mule.wsdl.parser.WsdlParser;
import org.mule.wsdl.parser.model.PortModel;
import org.mule.wsdl.parser.model.ServiceModel;
import org.mule.wsdl.parser.model.WsdlModel;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.mule.runtime.extension.api.values.ValueBuilder.newValue;

/**
 * {@link ValueProvider} implementation which provides the possible and supported values for the {@link WsdlConnectionInfo}
 * parameter.
 *
 * @since 1.0
 */
public class WsdlValueProvider implements ValueProvider {

  @Parameter
  private String wsdlLocation;

  @Override
  public Set<Value> resolve() throws ValueResolvingException {
    WsdlModel wsdlModel = WsdlParser.Companion.parse(wsdlLocation);
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
      URL address = port.getAddress();
      if (address != null) {
        portBuilder.withChild(newValue(address.toString()));
      }
      return portBuilder;
    }).collect(toSet());
  }
}
