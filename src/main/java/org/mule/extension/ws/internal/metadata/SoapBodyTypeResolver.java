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
import org.mule.runtime.api.metadata.resolving.InputTypeResolver;
import org.mule.soap.api.metadata.SoapMetadataResolver;
import org.mule.soap.api.metadata.SoapMetadataResolverFactory;

/**
 * {@link InputTypeResolver} implementation to resolve metadata for the message body of a particular operation.
 *
 * @since 1.0
 */
public class SoapBodyTypeResolver implements InputTypeResolver<String> {

  @Override
  public String getCategoryName() {
    return WebServiceConsumer.NAME;
  }

  @Override
  public String getResolverName() {
    return WebServiceConsumer.NAME + "BodyResolver";
  }

  @Override
  public MetadataType getInputMetadata(MetadataContext context, String operationName) throws ConnectionException {
    WscSoapClient client = context.<WscSoapClient>getConnection().get();
    SoapMetadataResolver soapMetadataResolver = SoapMetadataResolverFactory.getDefault().create(client.getWsdlLocation());
    return soapMetadataResolver.resolve(operationName).getInput().getBody();
  }
}
