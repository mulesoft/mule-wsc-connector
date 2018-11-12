/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.metadata;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

import org.mule.runtime.api.metadata.MetadataKeysContainer;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import org.junit.Test;

public class MetadataNegativeTestCase extends AbstractMetadataTestCase {

  @Test
  public void invalidService() {
    MetadataResult<MetadataKeysContainer> result = service.getMetadataKeys(location("invalid-service-flow"));
    assertThat(result.isSuccess(), is(false));
    assertThat(result.getFailures().size(), is(1));
    assertThat(result.getFailures().get(0).getReason(), containsString("service name [INVALID] not found in wsdl"));
  }

  @Test
  public void invalidPort() {
    MetadataResult<MetadataKeysContainer> result = service.getMetadataKeys(location("invalid-port-flow"));
    assertThat(result.isSuccess(), is(false));
    assertThat(result.getFailures().size(), is(1));
    assertThat(result.getFailures().get(0).getReason(), containsString("port name [INVALID] not found in wsdl"));
  }
}
