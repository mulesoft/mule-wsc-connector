/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.metadata;

import org.mule.extension.ws.internal.ConsumeOperation;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;
import org.mule.runtime.extension.api.soap.metadata.SoapOutputTypeBuilder;
import org.mule.runtime.soap.api.client.metadata.SoapOperationMetadata;

/**
 * Resolves the metadata for output payload of the {@link ConsumeOperation}.
 *
 * @since 1.0
 */
public class ConsumeOutputResolver extends BaseWscResolver implements OutputTypeResolver<String> {

  @Override
  public String getResolverName() {
    return "ConsumeOutputResolver";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MetadataType getOutputType(MetadataContext context, String ope) throws MetadataResolvingException, ConnectionException {
    SoapOperationMetadata metadata = getMetadataResolver(context).getOutputMetadata(ope);
    return SoapOutputTypeBuilder.buildOutputType(metadata.getBodyType(),
                                                 metadata.getHeadersType(),
                                                 metadata.getAttachmentsType(),
                                                 context.getTypeBuilder());
  }
}
