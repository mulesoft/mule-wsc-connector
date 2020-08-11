/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.addressing;

import org.mule.extension.ws.internal.addressing.properties.AddressingPropertiesImpl;

import java.util.UUID;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.junit.rules.ExpectedException;

public class AddressingPropertiesBuilderTestCase {

  private static final String NAMESPACE = "NAMESPACE";
  private static final String ACTION = "ACTION";
  private static final String TO = "TO";
  private static final String MESSAGE_ID = "MESSAGE_ID";
  private static final String FROM = "FROM";
  private static final String RELATES_TO = "RELATES_TO";

  private AddressingPropertiesImpl.AddressingPropertiesBuilder builder;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void before() {
    builder = AddressingPropertiesImpl.builder();
  }

  @Test
  public void testMinimumScenario() {
    AddressingPropertiesImpl sut = builder.build();
    assertThat(sut.isRequired(), is(false));
  }

  @Test
  public void testBasicScenario() {
    AddressingPropertiesImpl sut = builder
        .namespace(NAMESPACE)
        .action(ACTION)
        .to(TO)
        .build();

    assertThat(sut.isRequired(), is(true));
    assertThat(sut.getNamespace(), is(NAMESPACE));
    assertThat(sut.getAction(), is(ACTION));
    assertThat(sut.getTo(), is(TO));
  }

  @Test
  public void testImplicitMessageIDParameter() {
    AddressingPropertiesImpl sut = builder
        .namespace(NAMESPACE)
        .to(TO)
        .action(ACTION)
        .build();

    assertThat(sut.isRequired(), is(true));
    assertThat(sut.getMessageId().isPresent(), is(true));
    assertThat(sut.getMessageId().get(), isUuid());
  }

  @Test
  public void testExplicitMessageIDParameter() {
    AddressingPropertiesImpl sut = builder
        .namespace(NAMESPACE)
        .to(TO)
        .action(ACTION)
        .messageId(MESSAGE_ID)
        .build();

    assertThat(sut.isRequired(), is(true));
    assertThat(sut.getMessageId().isPresent(), is(true));
    assertThat(sut.getMessageId().get(), is(MESSAGE_ID));
  }

  @Test
  public void testExplicitFromParameter() {
    AddressingPropertiesImpl sut = builder
        .namespace(NAMESPACE)
        .to(TO)
        .action(ACTION)
        .from(FROM)
        .build();

    assertThat(sut.isRequired(), is(true));
    assertThat(sut.getFrom().isPresent(), is(true));
    assertThat(sut.getFrom().get(), is(FROM));
  }

  @Test
  public void testExplicitRelatesToParameter() {
    AddressingPropertiesImpl sut = builder
        .namespace(NAMESPACE)
        .to(TO)
        .action(ACTION)
        .relatesTo(RELATES_TO)
        .build();

    assertThat(sut.isRequired(), is(true));
    assertThat(sut.getRelatesTo().isPresent(), is(true));
    assertThat(sut.getRelatesTo().get(), is(RELATES_TO));
  }

  @Test
  public void failWithNoNamespace() {
    expectedException.expectMessage("Namespace URI cannot be null");
    builder.action(ACTION).build();
  }

  @Test
  public void failWithNoTo() {
    expectedException.expectMessage("'To' cannot be null");
    builder.namespace(NAMESPACE).action(ACTION).build();
  }

  @Test
  public void failWithNoAction() {
    expectedException.expectMessage("'Action' cannot be null");
    builder.namespace(NAMESPACE).to(TO).build();
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
