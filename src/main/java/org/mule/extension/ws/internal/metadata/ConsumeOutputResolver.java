/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.metadata;

import static org.mule.extension.ws.internal.utils.StringUtils.isNullOrEmpty;

import org.mule.extension.ws.internal.ConsumeOperation;
import org.mule.extension.ws.internal.WebServiceConsumer;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;

/**
 * Resolves the metadata for output payload of the {@link ConsumeOperation}.
 *
 * @since 1.0
 */
public class ConsumeOutputResolver extends AbstractOutputResolver implements OutputTypeResolver<ConsumeKey> {

  @Override
  public String getCategoryName() {
    return WebServiceConsumer.NAME;
  }

  @Override
  public String getResolverName() {
    return WebServiceConsumer.NAME + "OutputResolver";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MetadataType getOutputType(MetadataContext context, ConsumeKey key)
      throws ConnectionException, MetadataResolvingException {
    if (!isNullOrEmpty(key.getReplyTo())) {
      return context.getTypeBuilder().nullType().build();
    }
    return getOperationOutputType(context, key.getOperation());
  }
}
