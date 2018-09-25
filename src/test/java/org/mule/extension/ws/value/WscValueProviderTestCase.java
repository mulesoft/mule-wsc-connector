/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.extension.ws.value;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mule.extension.ws.AbstractWscTestCase;
import org.mule.runtime.api.component.location.Location;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.api.value.ValueProviderService;
import org.mule.runtime.api.value.ValueResult;
import org.mule.test.runner.RunnerDelegateTo;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mule.runtime.api.value.ValueProviderService.VALUE_PROVIDER_SERVICE_KEY;
import static org.mule.tck.junit4.matcher.ValueMatcher.valueWithId;

@RunnerDelegateTo()
public class WscValueProviderTestCase extends AbstractWscTestCase {

  public static final String CONNECTION = "Connection";

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Inject
  @Named(VALUE_PROVIDER_SERVICE_KEY)
  private ValueProviderService service;

  @Override
  public boolean enableLazyInit() {
    return true;
  }

  @Override
  public boolean disableXmlValidations() {
    return true;
  }

  @Override
  protected String getConfigurationFile() {
    return "config/wsld-value-provider.xml";
  }

  @Test
  public void weatherWsdl() {
    ValueResult result = service.getValues(Location.builder().globalName("weather").addConnectionPart().build(), CONNECTION);
    Set<Value> values = result.getValues();
    assertThat(result.isSuccess(), is(true));
    assertThat(values, hasItems(valueWithId("GlobalWeather").withPartName("service")
        .withChilds(valueWithId("GlobalWeatherHttpGet").withPartName("port")
            .withChilds(valueWithId(startsWith("http://www.webservicex.com/globalweather.asmx")).withPartName("address")))));
  }

  @Test
  public void humanWsdl() {
    ValueResult result =
        service.getValues(Location.builder().globalName("human").addConnectionPart().build(), CONNECTION);
    Set<Value> values = result.getValues();
    assertThat(result.isSuccess(), is(true));
    assertThat(values, hasItems(valueWithId("Human_ResourcesService").withPartName("service")
        .withChilds(valueWithId("Human_Resources").withPartName("port"))));
  }

  @Test
  public void rpcWsdlFails() {
    ValueResult values = service.getValues(Location.builder().globalName("rpc").addConnectionPart().build(), CONNECTION);
    assertThat(values.getFailure().isPresent(), is(true));
    assertThat(values.getFailure().get().getReason(), containsString("RPC style WSDLs are not supported"));
  }
}
