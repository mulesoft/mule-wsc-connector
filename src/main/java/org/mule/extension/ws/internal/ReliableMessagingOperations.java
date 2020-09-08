/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal;

import org.mule.extension.ws.api.reliablemessaging.ReliableMessagingVersion;
import org.mule.extension.ws.internal.connection.WscSoapClient;
import org.mule.extension.ws.internal.error.CreateRMSequenceErrorTypeProvider;
import org.mule.extension.ws.internal.error.TerminateRMSequenceErrorTypeProvider;
import org.mule.extension.ws.internal.error.WscExceptionEnricher;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.extension.api.annotation.OnException;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.soap.api.rm.CreateSequenceRequest;
import org.mule.soap.api.rm.TerminateSequenceRequest;

/**
 * Reliable messaging protocol operations
 *
 * @since 2.0
 */
public class ReliableMessagingOperations {

  /**
   * Creates a reliable messaging sequence.
   *
   * @param connection the connection resolved to execute the operation.
   * @return the created sequence identifier
   */
  @Throws(CreateRMSequenceErrorTypeProvider.class)
  @OnException(WscExceptionEnricher.class)
  @MediaType(MediaType.TEXT_PLAIN)
  public String createRMSequence(@Connection WscSoapClient connection, @Config WebServiceConsumer config, ExtensionsClient client)
      throws ConnectionException {
    ReliableMessagingVersion version = config.getReliableMessaging().getVersion();
    CreateSequenceRequest request = CreateSequenceRequest.builder()
        .namespaceUri(version.getNamespaceUri())
        .addressingNamespaceUri(version.getAddressingVersion().getNamespaceUri())
        .sequenceTtl(config.getReliableMessaging().getSequenceTtl())
        .build();
    return connection.createSequence(request, client);
  }

  /**
   * Terminates a reliable messaging sequence.
   *
   * @param connection the connection resolved to execute the operation.
   * @param sequence the target sequence identifier
   */
  @Throws(TerminateRMSequenceErrorTypeProvider.class)
  @OnException(WscExceptionEnricher.class)
  public void terminateRMSequence(@Connection WscSoapClient connection, String sequence, ExtensionsClient client)
      throws ConnectionException {
    TerminateSequenceRequest request = TerminateSequenceRequest.builder().sequenceIdentifier(sequence).build();
    connection.terminateSequence(request, client);
  }
}
