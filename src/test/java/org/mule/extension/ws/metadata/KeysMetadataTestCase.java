/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.metadata;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsIn.isIn;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mule.extension.ws.AllureConstants.WscFeature.WSC_EXTENSION;
import static org.mule.service.soap.SoapTestXmlValues.DOWNLOAD_ATTACHMENT;
import static org.mule.service.soap.SoapTestXmlValues.ECHO;
import static org.mule.service.soap.SoapTestXmlValues.ECHO_ACCOUNT;
import static org.mule.service.soap.SoapTestXmlValues.ECHO_HEADERS;
import static org.mule.service.soap.SoapTestXmlValues.FAIL;
import static org.mule.service.soap.SoapTestXmlValues.NO_PARAMS;
import static org.mule.service.soap.SoapTestXmlValues.NO_PARAMS_HEADERS;
import static org.mule.service.soap.SoapTestXmlValues.UPLOAD_ATTACHMENT;

import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.api.metadata.MetadataKeysContainer;
import org.mule.runtime.api.metadata.resolving.MetadataResult;
import org.junit.Test;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Stories;
import java.util.Set;

@Features(WSC_EXTENSION)
@Stories("Metadata")
public class KeysMetadataTestCase extends AbstractMetadataTestCase {

  private static final String[] OPERATIONS =
      {ECHO, ECHO_ACCOUNT, ECHO_HEADERS, FAIL, NO_PARAMS_HEADERS, NO_PARAMS, UPLOAD_ATTACHMENT, DOWNLOAD_ATTACHMENT};

  @Test
  @Description("Checks the MetadataKeys for the WSC")
  public void getOperationKeys() {
    MetadataResult<MetadataKeysContainer> result = service.getMetadataKeys(location(ECHO_ACCOUNT_FLOW));
    assertThat(result.isSuccess(), is(true));
    Set<MetadataKey> keys = result.get().getKeys("WebServiceConsumerCategory").get();
    assertThat(keys, hasSize(OPERATIONS.length));
    keys.forEach(key -> assertThat(key.getId(), isIn(OPERATIONS)));
  }
}
