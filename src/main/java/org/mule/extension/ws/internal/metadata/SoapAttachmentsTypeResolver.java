/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.metadata;

import org.mule.extension.ws.internal.WebServiceConsumer;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.api.metadata.resolving.InputTypeResolver;
import org.mule.wsdl.parser.model.operation.OperationModel;

/**
 * {@link InputTypeResolver} implementation to resolve metadata for the message attachments of a particular operation.
 *
 * @since 1.0
 */
public class SoapAttachmentsTypeResolver implements InputTypeResolver<ConsumeKey> {

  @Override
  public String getCategoryName() {
    return WebServiceConsumer.NAME;
  }

  @Override
  public String getResolverName() {
    return WebServiceConsumer.NAME + "AttachmentsResolver";
  }

  @Override
  public MetadataType getInputMetadata(MetadataContext context, ConsumeKey key)
      throws ConnectionException, MetadataResolvingException {
    OperationModel operationModel =
        MetadataResolverUtils.getInstance().getOperationFromCacheOrCreate(context, key.getOperation());
    return operationModel.getInputType().getAttachments();
  }
}
