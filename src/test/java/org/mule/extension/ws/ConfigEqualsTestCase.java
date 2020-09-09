/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Test;

import org.mule.extension.ws.api.addressing.AddressingConfiguration;
import org.mule.extension.ws.api.reliablemessaging.ReliableMessagingConfiguration;
import org.mule.extension.ws.api.reliablemessaging.ReliableMessagingConnectionSettings;
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

      assertThat(serviceSecurity, is(clonedServiceSecurity));
      assertThat(serviceSecurity.hashCode(), is(clonedServiceSecurity.hashCode()));
      assertThat(anotherServiceSecurity, is(anotherClonedServiceSecurity));
      assertThat(anotherServiceSecurity.hashCode(), is(anotherClonedServiceSecurity.hashCode()));
      assertThat(serviceSecurity, not(anotherServiceSecurity));
      assertThat(serviceSecurity.hashCode(), not(anotherServiceSecurity.hashCode()));
      assertThat(clonedServiceSecurity, not(anotherClonedServiceSecurity));
      assertThat(clonedServiceSecurity.hashCode(), not(anotherClonedServiceSecurity.hashCode()));

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

      assertThat(customTransport, is(clonedCustomTransport));
      assertThat(customTransport.hashCode(), is(clonedCustomTransport.hashCode()));
      assertThat(anotherCustomTransport, is(anotherClonedCustomTransport));
      assertThat(anotherCustomTransport.hashCode(), is(anotherClonedCustomTransport.hashCode()));
      assertThat(customTransport, not(anotherCustomTransport));
      assertThat(customTransport.hashCode(), not(anotherCustomTransport.hashCode()));
      assertThat(clonedCustomTransport, not(anotherClonedCustomTransport));
      assertThat(clonedCustomTransport.hashCode(), not(anotherClonedCustomTransport.hashCode()));

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

      assertThat(addressingConfiguration, is(addressingConfiguration));
      assertThat(clonedAddressingConfiguration, is(clonedAddressingConfiguration));
      assertThat(addressingConfiguration, is(clonedAddressingConfiguration));
      assertThat(addressingConfiguration.hashCode(), is(clonedAddressingConfiguration.hashCode()));
    }
  }

  @Test
  public void testAddressingNotEquals() {
    EasyRandomParameters parameters = new EasyRandomParameters();
    parameters.collectionSizeRange(1, 3);
    EasyRandom factory = new EasyRandom(parameters);

    AddressingConfiguration addressingConfiguration = factory.nextObject(AddressingConfiguration.class);
    assertThat(addressingConfiguration, is(notNullValue()));
    assertThat(addressingConfiguration, not(equalTo(new Object())));
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

      assertThat(reliableMessaging, is(reliableMessaging));
      assertThat(clonedReliableMessaging, is(clonedReliableMessaging));
      assertThat(reliableMessaging, is(clonedReliableMessaging));
      assertThat(reliableMessaging.hashCode(), is(clonedReliableMessaging.hashCode()));

    }
  }

  @Test
  public void testReliableMessagingNotEquals() {
    EasyRandomParameters parameters = new EasyRandomParameters();
    parameters.collectionSizeRange(1, 3);
    EasyRandom factory = new EasyRandom(parameters);

    ReliableMessagingConfiguration reliableMessaging = factory.nextObject(ReliableMessagingConfiguration.class);
    assertThat(reliableMessaging, is(notNullValue()));
    assertThat(reliableMessaging, not(equalTo(new Object())));
  }

  @Test
  public void testReliableMessagingConnectionSettingsEquals() {
    EasyRandomParameters parameters = new EasyRandomParameters();
    parameters.collectionSizeRange(1, 3);
    parameters.excludeField(field -> field.getName().equals("wsrmStore"));
    EasyRandom factory = new EasyRandom(parameters);
    EasyRandom factory2 = new EasyRandom(parameters);

    for (int i = 0; i < 1000; i++) {

      ReliableMessagingConnectionSettings reliableMessaging = factory.nextObject(ReliableMessagingConnectionSettings.class);
      ReliableMessagingConnectionSettings clonedReliableMessaging =
          factory2.nextObject(ReliableMessagingConnectionSettings.class);

      assertThat(reliableMessaging, is(reliableMessaging));
      assertThat(clonedReliableMessaging, is(clonedReliableMessaging));
      assertThat(reliableMessaging, is(clonedReliableMessaging));
      assertThat(reliableMessaging.hashCode(), is(clonedReliableMessaging.hashCode()));
    }
  }

  @Test
  public void testReliableMessagingConnectionSettingsNotEquals() {
    EasyRandomParameters parameters = new EasyRandomParameters();
    parameters.collectionSizeRange(1, 3);
    parameters.excludeField(field -> field.getName().equals("wsrmStore"));
    EasyRandom factory = new EasyRandom(parameters);

    ReliableMessagingConnectionSettings reliableMessaging = factory.nextObject(ReliableMessagingConnectionSettings.class);
    assertThat(reliableMessaging, is(notNullValue()));
    assertThat(reliableMessaging, not(equalTo(new Object())));
  }

}
