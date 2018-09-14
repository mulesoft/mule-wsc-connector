/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.metadata;

import org.mule.extension.ws.internal.ConsumeOperation;
import org.mule.extension.ws.internal.WebServiceConsumer;
import org.mule.extension.ws.internal.connection.WscSoapClient;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.NullType;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;
import org.mule.soap.api.metadata.SoapMetadataResolverFactory;
import org.mule.soap.api.metadata.SoapOperationMetadata;

/**
 * Resolves the metadata for output payload of the {@link ConsumeOperation}.
 *
 * @since 1.0
 */
public class ConsumeOutputResolver implements OutputTypeResolver<String> {

  public static final String BODY = "body";
  public static final String HEADERS = "headers";
  public static final String ATTACHMENTS = "attachments";

  @Override
  public String getCategoryName() {
    return WebServiceConsumer.NAME;
  }

  @Override
  public String getResolverName() {
    return WebServiceConsumer.NAME + " Output Resolver";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MetadataType getOutputType(MetadataContext context, String ope) throws ConnectionException {
    WscSoapClient client = context.<WscSoapClient>getConnection().get();
    SoapOperationMetadata resolve = SoapMetadataResolverFactory.getDefault().create(client.getWsdlLocation()).resolve(ope);
    ObjectTypeBuilder output = context.getTypeBuilder().objectType();

    MetadataType body = resolve.getOutput().getBody();
    MetadataType headers = resolve.getOutput().getHeaders();
    MetadataType attachments = resolve.getOutput().getAttachments();

    if (isNullType(body) && isNullType(headers) && isNullType(attachments)) {
      return context.getTypeBuilder().nullType().build();
    }

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
