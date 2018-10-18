/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.metadata;

import static java.util.stream.Collectors.joining;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mule.runtime.api.component.location.Location.builder;
import static org.mule.runtime.api.metadata.MetadataKeyBuilder.newKey;

import io.qameta.allure.Step;
import org.mule.extension.ws.AbstractWscTestCase;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.runtime.api.component.location.Location;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.metadata.MetadataService;
import org.mule.runtime.api.metadata.descriptor.ComponentMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataFailure;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import javax.inject.Inject;
import java.util.List;

public abstract class AbstractMetadataTestCase extends AbstractWscTestCase {

  protected static final String ECHO_ACCOUNT_FLOW = "getEchoAccountMetadata";
  protected static final String ECHO_FLOW = "getEchoMetadata";
  protected static final String NO_PARAMS_FLOW = "getNoParams";
  protected static final String ECHO_HEADERS_FLOW = "getEchoHeadersMetadata";

  @Inject
  protected MetadataService service;

  @Override
  protected String getConfigurationFile() {
    return "config/metadata.xml";
  }

  @Step("Retrieve Dynamic Metadata")
  protected MetadataResult<ComponentMetadataDescriptor<OperationModel>> getMetadata(String flow, String key) {
    MetadataResult<ComponentMetadataDescriptor<OperationModel>> result =
        service.getOperationMetadata(location(flow), newKey(key).build());
    assertThat("Failures: " + result.getFailures().stream().map(MetadataFailure::toString).collect(joining(", \n\t")),
               result.isSuccess(), is(true));

    return result;
  }

  protected Location location(String flow) {
    return builder().globalName(flow).addProcessorsPart().addIndexPart(0).build();
  }

  protected ObjectType toObjectType(MetadataType type) {
    assertThat(type, is(instanceOf(ObjectType.class)));
    return (ObjectType) type;
  }

  protected MetadataType getParameterType(List<ParameterModel> parameters, String name) {
    return parameters.stream().filter(p -> p.getName().equals(name)).findFirst().get().getType();
  }
}
