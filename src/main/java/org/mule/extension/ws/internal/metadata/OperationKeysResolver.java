/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.metadata;

import static java.util.stream.Collectors.toSet;
import static org.mule.runtime.api.metadata.MetadataKeyBuilder.newKey;
import static org.mule.runtime.api.metadata.resolving.FailureCode.INVALID_CONFIGURATION;
import static org.mule.runtime.api.metadata.resolving.FailureCode.INVALID_METADATA_KEY;

import org.mule.extension.ws.internal.ConsumeOperation;
import org.mule.extension.ws.internal.WebServiceConsumer;
import org.mule.extension.ws.internal.connection.WscSoapClient;
import org.mule.extension.ws.internal.connection.WsdlConnectionInfo;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.api.metadata.resolving.TypeKeysResolver;

import org.mule.wsdl.parser.exception.OperationNotFoundException;
import org.mule.wsdl.parser.model.PortModel;
import org.mule.wsdl.parser.model.ServiceModel;
import org.mule.wsdl.parser.model.WsdlModel;

import java.util.Set;

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
  public Set<MetadataKey> getKeys(MetadataContext context) throws ConnectionException, MetadataResolvingException {
    WsdlConnectionInfo info = context.<WscSoapClient>getConnection().get().getInfo();
    WsdlModel model = MetadataResolverUtils.getInstance().getOrCreateWsdlModel(context, info.getWsdlLocation());
    ServiceModel service = model.getService(info.getService());
    if (service == null) {
      throw new MetadataResolvingException("service name [" + info.getService() + "] not found in wsdl", INVALID_CONFIGURATION);
    }
    PortModel port = service.getPort(info.getPort());
    if (port == null) {
      throw new MetadataResolvingException("port name [" + info.getPort() + " ] not found in wsdl", INVALID_CONFIGURATION);
    }
    return port.getOperations().stream().map(ope -> newKey(ope.getName()).build()).collect(toSet());
  }
}
