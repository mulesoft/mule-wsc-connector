/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.metadata;

import org.mule.extension.ws.internal.WebServiceConsumer;
import org.mule.extension.ws.internal.connection.WscSoapClient;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.api.metadata.resolving.InputTypeResolver;
import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.soap.api.transport.locator.TransportResourceLocator;
import org.mule.wsdl.parser.model.operation.OperationModel;

/**
 * {@link InputTypeResolver} implementation to resolve metadata for the message headers of a particular operation.
 *
 * @since 1.0
 */
public class SoapHeadersTypeResolver implements InputTypeResolver<String> {

  @Override
  public String getCategoryName() {
    return WebServiceConsumer.NAME;
  }

  @Override
  public String getResolverName() {
    return WebServiceConsumer.NAME + "HeadersResolver";
  }

  @Override
  public MetadataType getInputMetadata(MetadataContext context, String operation)
      throws ConnectionException, MetadataResolvingException {

    WscSoapClient wscSoapClient = (WscSoapClient) context.getConnection().get();
    MetadataResolverUtils metadataResolverUtils = new MetadataResolverUtils(wscSoapClient);

    OperationModel operationModel = metadataResolverUtils.getOperationFromCacheOrCreate(context, operation);
    return operationModel.getInputType().getHeaders();
  }
}
