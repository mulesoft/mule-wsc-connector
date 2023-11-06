/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.security;

import org.mule.soap.api.security.SecurityStrategy;
import org.mule.soap.api.security.WssGlobalOutgoingSecurityStrategy;

/**
 * Adds global configurations to the outgoing SOAP message.
 *
 * @since 1.5
 */
public class WssOutgoingGlobalSecurityStrategyAdapter implements SecurityStrategyAdapter {

  /**
   * A SOAP message may travel from a sender to a receiver by passing different endpoints along the message path. The SOAP actor
   * attribute is used to address the {@code wsse:Security} element to a specific endpoint.
   * <p>
   * This parameter values must be a URI.
   */
  private String actor;

  /**
   * Whether mustUnderstand attribute in {@code wsse:Security} header is true or false.
   * <p>
   * Defaults to {@code true}
   */
  private boolean mustUnderstand;

  public WssOutgoingGlobalSecurityStrategyAdapter() {
  }

  public WssOutgoingGlobalSecurityStrategyAdapter(String actor, boolean mustUnderstand) {
    this.actor = actor;
    this.mustUnderstand = mustUnderstand;
  }

  @Override
  public SecurityStrategy getSecurityStrategy() {
    return new WssGlobalOutgoingSecurityStrategy(actor, mustUnderstand);
  }

  public String getActor() {
    return actor;
  }

  public void setActor(String actor) {
    this.actor = actor;
  }

  public boolean isMustUnderstand() {
    return mustUnderstand;
  }

  public void setMustUnderstand(boolean mustUnderstand) {
    this.mustUnderstand = mustUnderstand;
  }
}
