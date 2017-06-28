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
import org.mule.runtime.api.metadata.resolving.AttributesTypeResolver;
import org.mule.runtime.extension.internal.soap.metadata.SoapOutputTypeBuilder;
import org.mule.runtime.soap.api.client.metadata.SoapOperationMetadata;

/**
 * {@link AttributesTypeResolver} implementation for the {@link ConsumeOperation}.
 *
 * Builds an object type with a generic dictionary where all the protocol headers are located and a more specific
 * soap headers element with the specific outbound SOAP headers returned by the operation.
 *
 * @since 1.0
 */
public final class ConsumeAttributesResolver extends BaseWscResolver implements AttributesTypeResolver<String> {


  @Override
  public MetadataType getAttributesType(MetadataContext context, String operationName)
      throws MetadataResolvingException, ConnectionException {
    SoapOperationMetadata outputMetadata = getMetadataResolver(context).getOutputMetadata(operationName);
    return SoapOutputTypeBuilder.buildOutputAttributesType(outputMetadata.getHeadersType(), context.getTypeBuilder());
  }
}
