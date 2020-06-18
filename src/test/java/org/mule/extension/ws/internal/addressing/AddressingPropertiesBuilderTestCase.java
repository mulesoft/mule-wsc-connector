/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.addressing;

import org.mule.extension.ws.internal.addressing.properties.AddressingProperties;
import org.mule.extension.ws.internal.addressing.properties.AddressingPropertiesBuilder;
import org.mule.extension.ws.internal.connection.WsdlConnectionInfo;

import java.util.UUID;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddressingPropertiesBuilderTestCase {

  private static final String ADDRESS = "ADDRESS";
  private static final String NAMESPACE = "NAMESPACE";
  private static final String ACTION = "ACTION";
  private static final String OPERATION = "OPERATION";
  private static final String PORT = "PORT";
  private static final String TO = "TO";
  private static final String BASEPATH = "BASEPATH";
  private static final String REPLYTO = "REPLYTO";
  private static final String REPLYTO_WITH_SLASH = "/REPLYTO";
  private static final String FAULTTO = "FAULTTO";
  private static final String FAULTTO_WITH_SLASH = "/FAULTTO";

  private WsdlConnectionInfo wsdlInfo;
  private AddressingPropertiesBuilder builder;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void before() {
    wsdlInfo = mock(WsdlConnectionInfo.class);
    builder = new AddressingPropertiesBuilder(wsdlInfo, OPERATION);
  }

  @Test
  public void testBasicScenario() {
    AddressingProperties sut = builder
        .withNamespace(NAMESPACE)
        .withAction(ACTION)
        .withTo(TO)
        .build();

    assertThat(sut.getNamespaceURI(), is(NAMESPACE));
    assertThat(sut.getAction(), notNullValue());
    assertThat(sut.getAction().getValue(), is(ACTION));
    assertThat(sut.getTo(), notNullValue());
    assertThat(sut.getTo().getValue(), is(TO));
  }

  @Test
  public void testImplicitToParameter() {
    when(wsdlInfo.getAddress()).thenReturn(ADDRESS);

    AddressingProperties sut = builder
        .withNamespace(NAMESPACE)
        .withAction(ACTION)
        .build();

    assertThat(sut.getTo(), notNullValue());
    assertThat(sut.getTo().getValue(), is(ADDRESS));
  }

  @Test
  public void testImplicitActionParameter() {
    when(wsdlInfo.getAddress()).thenReturn(ADDRESS);
    when(wsdlInfo.getPort()).thenReturn(PORT);

    AddressingProperties sut = builder
        .withNamespace(NAMESPACE)
        .withTo(TO)
        .build();

    assertThat(sut.getAction(), notNullValue());
    assertThat(sut.getAction().getValue(), is(ADDRESS + "/" + PORT + "/" + OPERATION + "Request"));
  }

  @Test
  public void testImplicitMessageIDParameter() {
    AddressingProperties sut = builder
        .withNamespace(NAMESPACE)
        .withTo(TO)
        .withAction(ACTION)
        .build();

    assertThat(sut.getMessageID().isPresent(), is(true));
    assertThat(sut.getMessageID().get().getValue(), isUuid());
  }

  @Test
  public void testReplyToParameter() {
    AddressingProperties sut = builder
        .withNamespace(NAMESPACE)
        .withAction(ACTION)
        .withTo(TO)
        .withReplyTo(BASEPATH, REPLYTO, null)
        .build();

    assertThat(sut.getReplyTo().isPresent(), is(true));
    assertThat(sut.getReplyTo().get().getAddress().getValue(), is(BASEPATH + "/" + REPLYTO));
  }

  @Test
  public void testReplyToParameterWithSlash() {
    AddressingProperties sut = builder
        .withNamespace(NAMESPACE)
        .withAction(ACTION)
        .withTo(TO)
        .withReplyTo(BASEPATH, REPLYTO_WITH_SLASH, null)
        .build();

    assertThat(sut.getReplyTo().isPresent(), is(true));
    assertThat(sut.getReplyTo().get().getAddress().getValue(), is(BASEPATH + REPLYTO_WITH_SLASH));
  }

  @Test
  public void testFaultToParameter() {
    AddressingProperties sut = builder
        .withNamespace(NAMESPACE)
        .withAction(ACTION)
        .withTo(TO)
        .withReplyTo(BASEPATH, null, FAULTTO)
        .build();

    assertThat(sut.getFaultTo().isPresent(), is(true));
    assertThat(sut.getFaultTo().get().getAddress().getValue(), is(BASEPATH + "/" + FAULTTO));
  }

  @Test
  public void testFaultToParameterWithSlash() {
    AddressingProperties sut = builder
        .withNamespace(NAMESPACE)
        .withAction(ACTION)
        .withTo(TO)
        .withReplyTo(BASEPATH, null, FAULTTO_WITH_SLASH)
        .build();

    assertThat(sut.getFaultTo().isPresent(), is(true));
    assertThat(sut.getFaultTo().get().getAddress().getValue(), is(BASEPATH + FAULTTO_WITH_SLASH));
  }

  @Test
  public void failWithNoNamespace() {
    expectedException.expectMessage("Namespace URI cannot be null");
    builder.build();
  }

  @Test
  public void failWithNoTo() {
    expectedException.expectMessage("'To' cannot be null");
    builder.withNamespace(NAMESPACE).build();
  }

  @Test
  public void failWithNoAction() {
    expectedException.expectMessage("'Action' cannot be null");
    builder.withNamespace(NAMESPACE).withTo(TO).build();
  }

  private Matcher isUuid() {
    return new IsUuid();
  }

  private class IsUuid extends BaseMatcher<Boolean> {

    @Override
    public boolean matches(Object o) {
      if (o instanceof UUID) {
        return true;
      }
      if (o instanceof String) {
        try {
          UUID.fromString((String) o);
          return true;
        } catch (Exception e) {
        }
      }
      return false;
    }

    @Override
    public void describeTo(Description description) {

    }
  }
}
