/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mule.extension.ws.api.addressing.AddressingVersion.WSA200408;
import static org.mule.extension.ws.api.addressing.AddressingVersion.WSA200508;
import static org.mule.extension.ws.api.reliablemessaging.ReliableMessagingVersion.WSRM_10_WSA_200408;
import static org.mule.extension.ws.api.reliablemessaging.ReliableMessagingVersion.WSRM_11_WSA_200408;
import static org.mule.extension.ws.api.reliablemessaging.ReliableMessagingVersion.WSRM_12_WSA_200508;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.mule.extension.ws.api.addressing.AddressingConfiguration;
import org.mule.extension.ws.api.addressing.AddressingVersion;
import org.mule.extension.ws.api.reliablemessaging.ReliableMessagingConfiguration;
import org.mule.extension.ws.api.reliablemessaging.ReliableMessagingVersion;
import org.mule.extension.ws.internal.WebServiceConsumer;
import org.mule.runtime.api.lifecycle.Initialisable;
import org.mule.runtime.api.lifecycle.InitialisationException;

public class RMConfigInitialiseTestCase {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private EasyRandomParameters parameters;
  private WebServiceConsumer connector;
  private AddressingConfiguration addressing;

  @Before
  public void before() {
    parameters = new EasyRandomParameters()
        .collectionSizeRange(1, 3)
        .excludeField(field -> field.getName().equals("wsrmStore"));
    connector = mock(WebServiceConsumer.class);
    addressing = mock(AddressingConfiguration.class);
    when(connector.getAddressing()).thenReturn(addressing);
  }

  @Test
  public void failConfigInitWithNullWsaVersionTest() throws InitialisationException {
    when(addressing.getWsaVersion()).thenReturn(null);
    expectedException.expect(InitialisationException.class);
    expectedException.expectMessage("WSA version cannot be null");

    EasyRandom factory = new EasyRandom(parameters);
    ReliableMessagingConfiguration config = factory.nextObject(ReliableMessagingConfiguration.class);
    config.doInitialise(connector);
  }

  @Test
  public void defaultWsrmVersionFromWsa2004VersionTest() throws InitialisationException {
    when(addressing.getWsaVersion()).thenReturn(WSA200408);
    EasyRandom factory = new EasyRandom(parameters.randomize(String.class, () -> null));
    ReliableMessagingConfiguration config = factory.nextObject(ReliableMessagingConfiguration.class);
    config.doInitialise(connector);
    assertEquals(WSRM_11_WSA_200408, config.getVersion());
  }

  @Test
  public void defaultWsrmVersionFromWsa2005VersionTest() throws InitialisationException {
    when(addressing.getWsaVersion()).thenReturn(WSA200508);
    EasyRandom factory = new EasyRandom(parameters.randomize(String.class, () -> null));
    ReliableMessagingConfiguration config = factory.nextObject(ReliableMessagingConfiguration.class);
    config.doInitialise(connector);
    assertEquals(WSRM_12_WSA_200508, config.getVersion());
  }

  @Test
  public void failConfigInitWithInvalidWsrmVersionTest() throws InitialisationException {
    when(addressing.getWsaVersion()).thenReturn(WSA200408);
    expectedException.expect(InitialisationException.class);
    expectedException.expectMessage("Invalid WSRM version configured [Foo].");

    EasyRandom factory = new EasyRandom(parameters.randomize(String.class, () -> "Foo"));
    ReliableMessagingConfiguration config = factory.nextObject(ReliableMessagingConfiguration.class);
    config.doInitialise(connector);
  }

  @Test
  public void failConfigInitWithInvalidPairOfWsaWsrmVersionsTest() throws InitialisationException {
    ReliableMessagingVersion wsrmVersion = WSRM_12_WSA_200508;
    AddressingVersion wsaVersion = WSA200408;
    when(addressing.getWsaVersion()).thenReturn(wsaVersion);

    expectedException.expect(InitialisationException.class);
    expectedException.expectMessage("Invalid WSRM version configured [" + wsrmVersion.name() + "] for the selected WSA version ["
        + wsaVersion.name() + "].");

    EasyRandom factory = new EasyRandom(parameters.randomize(String.class, () -> wsrmVersion.name()));
    ReliableMessagingConfiguration config = factory.nextObject(ReliableMessagingConfiguration.class);
    config.doInitialise(connector);
  }

  @Test
  public void doInitialiseTest() throws InitialisationException {
    ReliableMessagingVersion wsrmVersion = WSRM_10_WSA_200408;
    AddressingVersion wsaVersion = WSA200408;
    when(addressing.getWsaVersion()).thenReturn(wsaVersion);

    EasyRandom factory = new EasyRandom(parameters.randomize(String.class, () -> wsrmVersion.name()));
    ReliableMessagingConfiguration config = factory.nextObject(ReliableMessagingConfiguration.class);
    config.doInitialise(connector);
    assertEquals(wsrmVersion, config.getVersion());
  }
}
