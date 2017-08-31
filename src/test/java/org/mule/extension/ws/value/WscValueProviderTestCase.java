package org.mule.extension.ws.value;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mule.extension.ws.AbstractSoapServiceTestCase;
import org.mule.runtime.api.component.location.Location;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.api.value.ValueProviderService;
import org.mule.runtime.api.value.ValueResult;
import org.mule.tck.junit4.rule.SystemProperty;
import org.mule.tck.util.TestConnectivityUtils;

import java.util.Set;

import static java.lang.Thread.currentThread;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.startsWith;
import static org.mule.tck.junit4.matcher.ValueMatcher.valueWithId;

public class WscValueProviderTestCase extends AbstractSoapServiceTestCase {

  protected ValueProviderService service;

  @Rule
  public SystemProperty systemProperty = TestConnectivityUtils.disableAutomaticTestConnectivity();

  @Before
  public void init() throws Exception {
    System.setProperty("humanWsdl", currentThread().getContextClassLoader().getResource("wsdl/human.wsdl").getPath());
    service = muleContext.getRegistry().lookupObject(ValueProviderService.class);
  }

  @Override
  protected String getConfigurationFile() {
    return "config/wsld-value-provider.xml";
  }

  @Test
  public void delta() {
    ValueResult result = service.getValues(Location.builder().globalName("delta").addConnectionPart().build(), "Connection");
    Set<Value> values = result.getValues();
    assertThat(values, hasItems(valueWithId("TicketServiceService").withPartName("service")
        .withChilds(valueWithId("TicketServicePort").withPartName("port")
            .withChilds(valueWithId(startsWith("http://training-u.cloudhub.io")).withPartName("address")))));
  }

  @Test
  public void humanResources() {
    ValueResult result =
        service.getValues(Location.builder().globalName("human").addConnectionPart().build(), "Connection");
    Set<Value> values = result.getValues();
    assertThat(values, hasItems(valueWithId("Human_ResourcesService").withPartName("service")
        .withChilds(valueWithId("Human_Resources").withPartName("port"))));
  }
}
