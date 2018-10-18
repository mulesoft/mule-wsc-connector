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
import static org.mule.extension.ws.SoapTestXmlValues.DOWNLOAD_ATTACHMENT;
import static org.mule.extension.ws.SoapTestXmlValues.ECHO;
import static org.mule.extension.ws.SoapTestXmlValues.ECHO_ACCOUNT;
import static org.mule.extension.ws.SoapTestXmlValues.ECHO_HEADERS;
import static org.mule.extension.ws.SoapTestXmlValues.FAIL;
import static org.mule.extension.ws.SoapTestXmlValues.LARGE;
import static org.mule.extension.ws.SoapTestXmlValues.NO_PARAMS;
import static org.mule.extension.ws.SoapTestXmlValues.NO_PARAMS_HEADERS;
import static org.mule.extension.ws.SoapTestXmlValues.ONE_WAY;
import static org.mule.extension.ws.SoapTestXmlValues.UPLOAD_ATTACHMENT;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Test;
import org.mule.extension.ws.internal.WebServiceConsumer;
import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.api.metadata.MetadataKeysContainer;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import java.util.Set;

@Feature(WSC_EXTENSION)
@Story("Metadata")
public class KeysMetadataTestCase extends AbstractMetadataTestCase {

  private static final String[] OPERATIONS =
      {ECHO,
          ECHO_ACCOUNT,
          ECHO_HEADERS,
          FAIL,
          NO_PARAMS_HEADERS,
          NO_PARAMS,
          UPLOAD_ATTACHMENT,
          DOWNLOAD_ATTACHMENT,
          LARGE,
          ONE_WAY};

  @Test
  @Description("Checks the MetadataKeys for the WSC")
  public void getOperationKeys() {
    MetadataResult<MetadataKeysContainer> result = service.getMetadataKeys(location(ECHO_ACCOUNT_FLOW));
    assertThat(result.isSuccess(), is(true));
    Set<MetadataKey> keys = result.get().getKeys(WebServiceConsumer.NAME).get();
    assertThat(keys, hasSize(OPERATIONS.length));
    keys.forEach(key -> assertThat(key.getId(), isIn(OPERATIONS)));
  }
}
