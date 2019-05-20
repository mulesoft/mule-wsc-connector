/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.metadata;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mule.extension.ws.AllureConstants.WscFeature.WSC_EXTENSION;
import static org.mule.extension.ws.SoapTestXmlValues.ECHO;
import static org.mule.extension.ws.SoapTestXmlValues.ECHO_ACCOUNT;
import static org.mule.extension.ws.SoapTestXmlValues.NO_PARAMS;
import static org.mule.extension.ws.internal.metadata.ConsumeOutputResolver.BODY;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Test;

import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.StringType;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.metadata.descriptor.ComponentMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import java.util.Collection;
import java.util.Iterator;

@Feature(WSC_EXTENSION)
@Story("Metadata")
public class BodyMetadataTestCase extends AbstractMetadataTestCase {

  @Test
  @Description("Test metadata from weird imports")
  public void getWeirdImportsMetadata() throws Exception {
    MetadataType body = getBody("weirdImports", "G177BulkUndoLock");
    int a = 2;
  }

  private MetadataType getBody(String flow, String key) {
    MetadataResult<ComponentMetadataDescriptor<OperationModel>> result = getMetadata(flow, key);
    return getParameterType(result.get().getModel().getAllParameterModels(), BODY);
  }
}
