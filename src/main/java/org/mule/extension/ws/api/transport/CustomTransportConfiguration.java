/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.transport;

import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.soap.api.transport.TransportDispatcher;
import org.mule.soap.api.transport.locator.TransportResourceLocator;

/**
 * Contract for objects that enables the use of a custom transport executing operations of other plugins by using the
 * {@link ExtensionsClient} to send soap the messages.
 *
 * @since 1.0
 */
public interface CustomTransportConfiguration {

  /**
   * Builds a new {@link TransportDispatcher} using the {@link ExtensionsClient}.
   *
   * @param client the extensions client.
   * @return a new {@link TransportDispatcher}.
   */
  TransportDispatcher buildDispatcher(ExtensionsClient client);

  /**
   * Builds a new {@link TransportResourceLocator} using the {@link ExtensionsClient}.
   *
   * @param client the extensions client.
   * @return a new {@link TransportResourceLocator} instance.
   */
  TransportResourceLocator resourceLocator(ExtensionsClient client);
}
