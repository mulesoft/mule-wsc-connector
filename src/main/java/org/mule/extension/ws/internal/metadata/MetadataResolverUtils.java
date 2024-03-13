/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.metadata;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.mule.runtime.api.metadata.resolving.FailureCode.CONNECTION_FAILURE;
import static org.mule.runtime.api.metadata.resolving.FailureCode.INVALID_CONFIGURATION;
import static org.mule.runtime.api.metadata.resolving.FailureCode.INVALID_METADATA_KEY;

import org.mule.extension.ws.internal.connection.WscSoapClient;
import org.mule.extension.ws.internal.connection.WsdlConnectionInfo;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataCache;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.soap.api.transport.locator.TransportResourceLocator;
import org.mule.wsdl.parser.WsdlParser;
import org.mule.wsdl.parser.exception.OperationNotFoundException;
import org.mule.wsdl.parser.locator.ResourceLocator;
import org.mule.wsdl.parser.model.PortModel;
import org.mule.wsdl.parser.model.ServiceModel;
import org.mule.wsdl.parser.model.WsdlModel;
import org.mule.wsdl.parser.model.operation.OperationModel;

import java.io.InputStream;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * Utility class for resolvers to get already loaded models located in the {@link MetadataCache}, if not there will load and store
 * them there.
 *
 * Also for subsequent requests, this resolver contains an in-memory cache, that stores {@link WsdlModel} instances for a short
 * period of time to prevent dealing with multiple serialized models in memory.
 *
 * @since 1.2
 */
public class MetadataResolverUtils {

  /**
   * Caches {@link WsdlModel} instances so subsequent metadata resolution requests doesn't need to parse or deserialize wsdls
   * models, that are stored in the MetadataCache that use a persistent object store.
   */
  private final Cache<String, WsdlModel> recentlyQueriedCache = Caffeine.newBuilder().expireAfterAccess(1, MINUTES).build();

  private MetadataResolverUtils() {}

  private static MetadataResolverUtils instance;

  public static MetadataResolverUtils getInstance() {
    if (instance == null) {
      instance = new MetadataResolverUtils();
    }
    return instance;
  }

  OperationModel getOperationFromCacheOrCreate(MetadataContext context, String operation)
      throws ConnectionException, MetadataResolvingException {
    PortModel port = findPortFromContext(context);
    try {
      return port.getOperation(operation);
    } catch (OperationNotFoundException e) {
      throw new MetadataResolvingException("Operation [" + operation + "] was not found in the wsdl file", INVALID_METADATA_KEY);
    }
  }

  PortModel findPortFromContext(MetadataContext context) throws MetadataResolvingException, ConnectionException {
    WsdlConnectionInfo info = getWscSoapClient(context).getInfo();
    WsdlModel model = getWsdlModel(context, info.getAbsoluteWsdlLocation());
    ServiceModel service = model.getService(info.getService());
    if (service == null) {
      throw new MetadataResolvingException("service name [" + info.getService() + "] not found in wsdl", INVALID_CONFIGURATION);
    }
    PortModel port = service.getPort(info.getPort());
    if (port == null) {
      throw new MetadataResolvingException("port name [" + info.getPort() + "] not found in wsdl", INVALID_CONFIGURATION);
    }
    return port;
  }

  private WsdlModel getWsdlModel(MetadataContext context, String location)
      throws ConnectionException, MetadataResolvingException {
    WsdlModel model = recentlyQueriedCache.getIfPresent(location);
    if (model != null) {
      return model;
    }
    model = parseWithPersistentCache(context, location);
    recentlyQueriedCache.put(location, model);
    return model;
  }

  private WsdlModel parseWithPersistentCache(MetadataContext context, String location)
      throws ConnectionException, MetadataResolvingException {
    MetadataCache persistentCache = context.getCache();
    ResourceLocator locator = getResourceLocator(getWscSoapClient(context));
    return WsdlParser.Companion.parse(location, new MetadataCacheResourceLocatorDecorator(persistentCache, locator), "UTF-8");
  }

  private WscSoapClient getWscSoapClient(MetadataContext context) throws ConnectionException, MetadataResolvingException {
    return context.<WscSoapClient>getConnection()
        .orElseThrow(() -> new MetadataResolvingException("No connection available to retrieve metadata", CONNECTION_FAILURE));
  }

  private ResourceLocator getResourceLocator(WscSoapClient soapClient) {
    ExtensionsClient extensionsClient = soapClient.getExtensionsClient();
    TransportResourceLocator transportResourceLocator = soapClient.getTransportConfiguration().resourceLocator(extensionsClient);
    return new ResourceLocatorAdapter(transportResourceLocator);
  }

  private class ResourceLocatorAdapter implements ResourceLocator {

    private final TransportResourceLocator locator;

    ResourceLocatorAdapter(TransportResourceLocator locator) {
      this.locator = locator;
    }

    @Override
    public boolean handles(String s) {
      return locator.handles(s);
    }

    @Override
    public InputStream getResource(String s) {
      return locator.getResource(s);
    }
  }
}
