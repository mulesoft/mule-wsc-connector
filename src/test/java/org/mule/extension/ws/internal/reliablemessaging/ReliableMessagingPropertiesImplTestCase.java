/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.reliablemessaging;

import org.mule.soap.api.message.ReliableMessagingProperties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import org.junit.Test;

public class ReliableMessagingPropertiesImplTestCase {

  private static String SEQUENCE_ID = "SEQUENCE_ID";

  @Test
  public void sequenceValueTest() {
    ReliableMessagingProperties reliableMessagingProperties = new ReliableMessagingPropertiesImpl(SEQUENCE_ID);
    assertThat(reliableMessagingProperties.getSequenceIdentifier(), is(SEQUENCE_ID));
  }

  @Test
  public void nullSequenceValueTest() {
    ReliableMessagingProperties reliableMessagingProperties = new ReliableMessagingPropertiesImpl(null);
    assertThat(reliableMessagingProperties.getSequenceIdentifier(), is((String) null));
  }
}
