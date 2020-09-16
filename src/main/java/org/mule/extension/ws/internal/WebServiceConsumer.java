/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal;

import static org.mule.extension.ws.internal.WebServiceConsumer.NAME;

import org.mule.extension.ws.api.addressing.AddressingConfiguration;
import org.mule.extension.ws.api.reliablemessaging.ReliableMessagingConfiguration;
import org.mule.extension.ws.api.transport.CustomHttpTransportConfiguration;
import org.mule.extension.ws.api.transport.CustomTransportConfiguration;
import org.mule.extension.ws.api.transport.DefaultHttpTransportConfiguration;
import org.mule.extension.ws.internal.connection.SoapClientConnectionProvider;
import org.mule.extension.ws.internal.error.WscError;
import org.mule.runtime.api.lifecycle.Initialisable;
import org.mule.runtime.api.lifecycle.InitialisationException;
import org.mule.runtime.extension.api.annotation.Export;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.SubTypeMapping;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.runtime.extension.api.annotation.error.ErrorTypes;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

/**
 * Web Service Consumer extension used to consume SOAP web services.
 *
 * @since 1.0
 */
@ErrorTypes(WscError.class)
@Operations({ConsumeOperation.class, ParseResponseOperation.class, ReliableMessagingOperations.class})
@ConnectionProviders(SoapClientConnectionProvider.class)
@SubTypeMapping(baseType = CustomTransportConfiguration.class,
    subTypes = {CustomHttpTransportConfiguration.class, DefaultHttpTransportConfiguration.class})
@Extension(name = NAME)
@Xml(prefix = "wsc")
@Export(classes = ReliableMessagingConfiguration.class)
public class WebServiceConsumer implements Initialisable {

  public static final String NAME = "Web Service Consumer";

  /**
   * Web Service Addressing configuration
   *
   * @since 1.7
   */
  @ParameterGroup(name = "wsa", showInDsl = true)
  @DisplayName("Web Service Addressing")
  private AddressingConfiguration wsAddressing;

  /**
   * Web Service Reliable Messaging configuration
   *
   * @since 1.7
   */
  @ParameterGroup(name = "wsrm", showInDsl = true)
  @DisplayName("Web Service Reliable Messaging")
  private ReliableMessagingConfiguration reliableMessaging;

  @Override
  public void initialise() throws InitialisationException {
    reliableMessaging.doInitialise(wsAddressing.getWsaVersion(), this);
  }

  public ReliableMessagingConfiguration getReliableMessaging() {
    return reliableMessaging;
  }
}
