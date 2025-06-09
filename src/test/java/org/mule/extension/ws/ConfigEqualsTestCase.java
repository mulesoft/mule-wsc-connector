/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Ignore;
import org.junit.Test;
import org.mule.extension.ws.api.WebServiceSecurity;
import org.mule.extension.ws.api.security.*;
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
        || field.getName().equals("customTransportConfiguration") || field.getName().equals("LOGGER"));
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
  public void testWebServiceSecurityEquals() {
    WebServiceSecurity webServiceSecurity = new WebServiceSecurity();
    WebServiceSecurity anotherInstance = new WebServiceSecurity();
    WssSignSecurityStrategyAdapter mockSignSecurityStrategy = mock(WssSignSecurityStrategyAdapter.class);
    WssVerifySignatureSecurityStrategyAdapter mockVerifySignatureSecurityStrategy =
        mock(WssVerifySignatureSecurityStrategyAdapter.class);
    WssUsernameTokenSecurityStrategyAdapter mockUsernameTokenSecurityStrategy =
        mock(WssUsernameTokenSecurityStrategyAdapter.class);
    WssTimestampSecurityStrategyAdapter mockTimestampSecurityStrategy = mock(WssTimestampSecurityStrategyAdapter.class);
    WssDecryptSecurityStrategyAdapter mockDecryptSecurityStrategy = mock(WssDecryptSecurityStrategyAdapter.class);
    WssEncryptSecurityStrategyAdapter mockEncryptSecurityStrategy = mock(WssEncryptSecurityStrategyAdapter.class);
    WssIncomingTimestampSecurityStrategyAdapter mockIncomingTimestampSecurityStrategy =
        mock(WssIncomingTimestampSecurityStrategyAdapter.class);

    webServiceSecurity.setSignSecurityStrategy(mockSignSecurityStrategy);
    webServiceSecurity.setVerifySignatureSecurityStrategy(mockVerifySignatureSecurityStrategy);
    webServiceSecurity.setUsernameTokenSecurityStrategy(mockUsernameTokenSecurityStrategy);
    webServiceSecurity.setTimestampSecurityStrategy(mockTimestampSecurityStrategy);
    webServiceSecurity.setDecryptSecurityStrategy(mockDecryptSecurityStrategy);
    webServiceSecurity.setEncryptSecurityStrategy(mockEncryptSecurityStrategy);
    webServiceSecurity.setIncomingTimestampSecurityStrategy(mockIncomingTimestampSecurityStrategy);
    webServiceSecurity.setActor("http://example.com");
    webServiceSecurity.setMustUnderstand(true);
    anotherInstance.setSignSecurityStrategy(mockSignSecurityStrategy);
    anotherInstance.setVerifySignatureSecurityStrategy(mockVerifySignatureSecurityStrategy);
    anotherInstance.setUsernameTokenSecurityStrategy(mockUsernameTokenSecurityStrategy);
    anotherInstance.setTimestampSecurityStrategy(mockTimestampSecurityStrategy);
    anotherInstance.setDecryptSecurityStrategy(mockDecryptSecurityStrategy);
    anotherInstance.setEncryptSecurityStrategy(mockEncryptSecurityStrategy);
    anotherInstance.setIncomingTimestampSecurityStrategy(mockIncomingTimestampSecurityStrategy);
    anotherInstance.setActor("http://example.com");
    anotherInstance.setMustUnderstand(true);

    assertTrue(webServiceSecurity.equals(anotherInstance));
    assertEquals(webServiceSecurity.hashCode(), anotherInstance.hashCode());

    anotherInstance.setActor("http://changed.com");
    assertNotEquals(webServiceSecurity.hashCode(), anotherInstance.hashCode());
  }
}
