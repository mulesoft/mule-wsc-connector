/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal;

import static org.mule.extension.ws.internal.WebServiceConsumer.NAME;
import static org.mule.sdk.api.meta.JavaVersion.JAVA_8;
import static org.mule.sdk.api.meta.JavaVersion.JAVA_11;
import static org.mule.sdk.api.meta.JavaVersion.JAVA_17;

import org.mule.extension.ws.api.transport.CustomHttpTransportConfiguration;
import org.mule.extension.ws.api.transport.CustomTransportConfiguration;
import org.mule.extension.ws.api.transport.DefaultHttpTransportConfiguration;
import org.mule.extension.ws.internal.connection.SoapClientConnectionProvider;
import org.mule.extension.ws.internal.error.WscError;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.SubTypeMapping;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.runtime.extension.api.annotation.error.ErrorTypes;
import org.mule.sdk.api.annotation.JavaVersionSupport;

/**
 * Web Service Consumer extension used to consume SOAP web services.
 *
 * @since 1.0
 */
@ErrorTypes(WscError.class)
@Operations(ConsumeOperation.class)
@ConnectionProviders(SoapClientConnectionProvider.class)
@SubTypeMapping(baseType = CustomTransportConfiguration.class,
    subTypes = {CustomHttpTransportConfiguration.class, DefaultHttpTransportConfiguration.class})
@Extension(name = NAME)
@JavaVersionSupport(value = {JAVA_17})
@Xml(prefix = "wsc")
public class WebServiceConsumer {

  public static final String NAME = "Web Service Consumer";
}
