/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.metadata;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mule.extension.ws.AllureConstants.WscFeature.WSC_EXTENSION;
import static org.mule.service.soap.SoapTestXmlValues.ONE_WAY;

import org.mule.metadata.api.model.NullType;
import org.mule.runtime.api.meta.model.operation.OperationModel;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Test;

@Feature(WSC_EXTENSION)
@Story("Metadata")
public class OneWayMetadataTestCase extends AbstractMetadataTestCase {

  private static final String ONE_WAY_FLOW = "oneWayMetadata";

  @Test
  @Description("Checks the dynamic metadata of the request body parameter for the echoAccount operation")
  public void getEchoAccountInputBody() {
    OperationModel result = getMetadata(ONE_WAY_FLOW, ONE_WAY).get().getModel();
    assertThat(result.getOutput().getType(), is(instanceOf(NullType.class)));
  }
}
