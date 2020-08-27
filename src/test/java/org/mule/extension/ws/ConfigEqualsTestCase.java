/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Test;

import org.mule.extension.ws.api.addressing.AddressingConfiguration;
import org.mule.extension.ws.api.reliablemessaging.ReliableMessagingConfiguration;
import org.mule.extension.ws.api.transport.CustomHttpTransportConfiguration;
import org.mule.extension.ws.api.transport.CustomTransportConfiguration;
import org.mule.extension.ws.api.transport.DefaultHttpTransportConfiguration;
import org.mule.extension.ws.internal.connection.SoapClientConnectionProvider;


public class ConfigEqualsTestCase {

  @Test
  public void testSoapClientConnectionProviderEquals() {
    EasyRandomParameters parameters = new EasyRandomParameters();
    parameters.collectionSizeRange(1, 3);
    parameters.excludeField(field -> field.getName().equals("httpService") || field.getName().toLowerCase().contains("client")
        || field.getName().equals("customTransportConfiguration") || field.getName().equals("wsrmStore")
        || field.getName().equals("lockFactory"));
    EasyRandom factory = new EasyRandom(parameters);
    EasyRandom factory2 = new EasyRandom(parameters);

    for (int i = 0; i < 1000; i++) {

      SoapClientConnectionProvider serviceSecurity = factory.nextObject(SoapClientConnectionProvider.class);
      SoapClientConnectionProvider anotherServiceSecurity = factory.nextObject(SoapClientConnectionProvider.class);
      SoapClientConnectionProvider clonedServiceSecurity = factory2.nextObject(SoapClientConnectionProvider.class);
      SoapClientConnectionProvider anotherClonedServiceSecurity = factory2.nextObject(SoapClientConnectionProvider.class);

      assertEquals(serviceSecurity, clonedServiceSecurity);
      assertTrue(serviceSecurity.hashCode() == clonedServiceSecurity.hashCode());
      assertEquals(anotherServiceSecurity, anotherClonedServiceSecurity);
      assertTrue(anotherServiceSecurity.hashCode() == anotherClonedServiceSecurity.hashCode());
      assertNotEquals(serviceSecurity, anotherServiceSecurity);
      assertFalse(serviceSecurity.hashCode() == anotherServiceSecurity.hashCode());
      assertNotEquals(clonedServiceSecurity, anotherClonedServiceSecurity);
      assertFalse(clonedServiceSecurity.hashCode() == anotherClonedServiceSecurity.hashCode());

    }
  }

  @Test
  public void testCustomTransportsEquals() {
    EasyRandomParameters parameters = new EasyRandomParameters();
    parameters.collectionSizeRange(1, 3);
    EasyRandom factory = new EasyRandom(parameters);
    EasyRandom factory2 = new EasyRandom(parameters);

    for (int i = 0; i < 1000; i++) {

      CustomTransportConfiguration customTransport = factory.nextObject(CustomHttpTransportConfiguration.class);
      CustomTransportConfiguration anotherCustomTransport = factory.nextObject(DefaultHttpTransportConfiguration.class);
      CustomTransportConfiguration clonedCustomTransport = factory2.nextObject(CustomHttpTransportConfiguration.class);
      CustomTransportConfiguration anotherClonedCustomTransport = factory2.nextObject(DefaultHttpTransportConfiguration.class);

      assertEquals(customTransport, clonedCustomTransport);
      assertTrue(customTransport.hashCode() == clonedCustomTransport.hashCode());
      assertEquals(anotherCustomTransport, anotherClonedCustomTransport);
      assertTrue(anotherCustomTransport.hashCode() == anotherClonedCustomTransport.hashCode());
      assertNotEquals(customTransport, anotherCustomTransport);
      assertFalse(customTransport.hashCode() == anotherCustomTransport.hashCode());
      assertNotEquals(clonedCustomTransport, anotherClonedCustomTransport);
      assertFalse(clonedCustomTransport.hashCode() == anotherClonedCustomTransport.hashCode());

    }
  }

  @Test
  public void testAddressingEquals() {
    EasyRandomParameters parameters = new EasyRandomParameters();
    parameters.collectionSizeRange(1, 3);
    EasyRandom factory = new EasyRandom(parameters);
    EasyRandom factory2 = new EasyRandom(parameters);

    for (int i = 0; i < 1000; i++) {

      AddressingConfiguration addressingConfiguration = factory.nextObject(AddressingConfiguration.class);
      AddressingConfiguration clonedAddressingConfiguration = factory2.nextObject(AddressingConfiguration.class);

      assertEquals(addressingConfiguration, clonedAddressingConfiguration);
      assertTrue(addressingConfiguration.hashCode() == clonedAddressingConfiguration.hashCode());

    }
  }

  @Test
  public void testReliableMessagingEquals() {
    EasyRandomParameters parameters = new EasyRandomParameters();
    parameters.collectionSizeRange(1, 3);
    EasyRandom factory = new EasyRandom(parameters);
    EasyRandom factory2 = new EasyRandom(parameters);

    for (int i = 0; i < 1000; i++) {

      ReliableMessagingConfiguration reliableMessaging = factory.nextObject(ReliableMessagingConfiguration.class);
      ReliableMessagingConfiguration clonedReliableMessaging = factory2.nextObject(ReliableMessagingConfiguration.class);

      assertEquals(reliableMessaging, reliableMessaging);
      assertEquals(clonedReliableMessaging, clonedReliableMessaging);
      assertEquals(reliableMessaging, clonedReliableMessaging);
      assertTrue(reliableMessaging.hashCode() == clonedReliableMessaging.hashCode());

    }
  }

  @Test
  public void testReliableMessagingNotEquals() {
    EasyRandomParameters parameters = new EasyRandomParameters();
    parameters.collectionSizeRange(1, 3);
    EasyRandom factory = new EasyRandom(parameters);

    ReliableMessagingConfiguration reliableMessaging = factory.nextObject(ReliableMessagingConfiguration.class);
    assertFalse(reliableMessaging.equals(null));
    assertFalse(reliableMessaging.equals(new Object()));
  }

}
