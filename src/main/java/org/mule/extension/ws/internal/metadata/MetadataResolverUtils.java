/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.metadata;

import static org.mule.runtime.api.metadata.resolving.FailureCode.INVALID_METADATA_KEY;

import org.mule.extension.ws.internal.connection.WscSoapClient;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataCache;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.wsdl.parser.WsdlParser;
import org.mule.wsdl.parser.model.WsdlModel;
import org.mule.wsdl.parser.model.operation.OperationModel;
import org.mule.wsdl.parser.serializer.WsdlModelSerializer;

/**
 * Utility class for resolvers to get already loaded models located in the {@link MetadataCache}, if not there will load and
 * store them there.
 *
 * @since 1.2
 */
public class MetadataResolverUtils {

  private MetadataResolverUtils() {}

  private static MetadataResolverUtils instance;

  public static MetadataResolverUtils getInstance() {
    if (instance == null) {
      instance = new MetadataResolverUtils();
    }
    return instance;
  }

  public OperationModel getOperationFromCacheOrCreate(MetadataContext context, String operation)
      throws ConnectionException, MetadataResolvingException {
    String location = context.<WscSoapClient>getConnection().get().getInfo().getWsdlLocation();

    WsdlModel model = getOrCreateWsdlModel(context, location);

    OperationModel operationModel = model.getOperation(operation);
    if (operationModel == null) {
      throw new MetadataResolvingException("Operation [" + operation + "] was not found in the wsdl file", INVALID_METADATA_KEY);
    }
    return operationModel;
  }

  public WsdlModel getOrCreateWsdlModel(MetadataContext context, String location) {
    MetadataCache cache = context.getCache();
    return cache.get(location)
        .map(serialized -> WsdlModelSerializer.INSTANCE.deserialize((String) serialized))
        .orElseGet(() -> {
          WsdlModel wsdl = WsdlParser.Companion.parse(location, new MetadataCacheResourceLocatorDecorator(cache));
          String serialized = WsdlModelSerializer.INSTANCE.serialize(wsdl, false);
          cache.put(location, serialized);
          return wsdl;
        });
  }
}
