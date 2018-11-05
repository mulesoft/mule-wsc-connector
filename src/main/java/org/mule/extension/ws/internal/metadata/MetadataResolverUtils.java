/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.metadata;

import static org.mule.runtime.api.metadata.resolving.FailureCode.INVALID_CONFIGURATION;
import static org.mule.runtime.api.metadata.resolving.FailureCode.INVALID_METADATA_KEY;

import org.mule.extension.ws.internal.connection.WscSoapClient;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataCache;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataResolvingException;

import org.mule.extension.ws.internal.connection.WsdlConnectionInfo;
import org.mule.wsdl.parser.WsdlParser;
import org.mule.wsdl.parser.exception.OperationNotFoundException;
import org.mule.wsdl.parser.model.PortModel;
import org.mule.wsdl.parser.model.ServiceModel;
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
    WsdlConnectionInfo info = context.<WscSoapClient>getConnection().get().getInfo();
    WsdlModel model = getOrCreateWsdlModel(context, info.getWsdlLocation());
    try {
      ServiceModel service = model.getService(info.getService());
      if (service == null) {
        throw new MetadataResolvingException("service name [" + info.getService() + "] not found in wsdl", INVALID_CONFIGURATION);
      }
      PortModel port = service.getPort(info.getPort());
      if (port == null) {
        throw new MetadataResolvingException("port name [" + info.getPort() + " ] not found in wsdl", INVALID_CONFIGURATION);
      }
      return port.getOperation(operation);
    } catch (OperationNotFoundException e) {
      throw new MetadataResolvingException("Operation [" + operation + "] was not found in the wsdl file", INVALID_METADATA_KEY);
    }
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
