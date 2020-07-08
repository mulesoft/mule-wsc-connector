/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.metadata;

import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.NullType;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.wsdl.parser.model.operation.Type;

/**
 * Resolves the metadata for output payload of an operation.
 *
 * @since 2.0
 */
public abstract class AbstractOutputResolver {

  public static final String BODY = "body";
  public static final String HEADERS = "headers";
  public static final String ATTACHMENTS = "attachments";

  protected MetadataType getOperationOutputType(MetadataContext context, String operation)
      throws ConnectionException, MetadataResolvingException {

    Type outputType =
        MetadataResolverUtils.getInstance().getOperationFromCacheOrCreate(context, operation).getOutputType();
    MetadataType body = outputType.getBody();
    MetadataType headers = outputType.getHeaders();
    MetadataType attachments = outputType.getAttachments();

    if (isNullType(body) && isNullType(headers) && isNullType(attachments)) {
      return context.getTypeBuilder().nullType().build();
    }

    ObjectTypeBuilder output = context.getTypeBuilder().objectType();
    addIfNotNullType(output, BODY, body);
    addIfNotNullType(output, HEADERS, headers);
    addIfNotNullType(output, ATTACHMENTS, attachments);

    return output.build();
  }

  private void addIfNotNullType(ObjectTypeBuilder typeBuilder, String fieldName, MetadataType fieldType) {
    if (!isNullType(fieldType)) {
      typeBuilder.addField().key(fieldName).value(fieldType);
    }
  }

  private boolean isNullType(MetadataType metadataType) {
    return metadataType instanceof NullType;
  }
}
