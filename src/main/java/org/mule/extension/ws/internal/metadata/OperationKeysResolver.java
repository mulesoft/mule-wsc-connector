/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.metadata;

import org.mule.extension.ws.internal.ConsumeOperation;
import org.mule.extension.ws.internal.WebServiceConsumer;
import org.mule.extension.ws.internal.connection.WscSoapClient;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.api.metadata.resolving.TypeKeysResolver;
import org.mule.soap.api.metadata.SoapMetadataResolverFactory;

import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.mule.runtime.api.metadata.MetadataKeyBuilder.newKey;

/**
 * {@link TypeKeysResolver} implementation for the {@link ConsumeOperation}, retrieves a metadata key for each operation available
 * to be executed by a service.
 *
 * @since 1.0
 */
public class OperationKeysResolver implements TypeKeysResolver {

  @Override
  public String getCategoryName() {
    return WebServiceConsumer.NAME;
  }

  @Override
  public String getResolverName() {
    return WebServiceConsumer.NAME + "KeysResolver";
  }

  @Override
  public Set<MetadataKey> getKeys(MetadataContext context) throws ConnectionException {
    WscSoapClient client = context.<WscSoapClient>getConnection().get();
    Set<String> operations = SoapMetadataResolverFactory.getDefault().create(client.getWsdlLocation()).operations();
    return operations.stream().map(ope -> newKey(ope).build()).collect(toSet());
  }
}
