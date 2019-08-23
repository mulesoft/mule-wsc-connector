/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.metadata;

import org.mule.extension.ws.api.SoapVersionAdapter;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.values.ValueBuilder;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.extension.api.values.ValueResolvingException;

import java.util.Set;

public class SoapActorValueProvider implements ValueProvider {

  @Parameter
  SoapVersionAdapter soapVersion;

  @Override
  public Set<Value> resolve() throws ValueResolvingException {
    return SoapVersionAdapter.SOAP12.equals(soapVersion) ? ValueBuilder
        .getValuesFor("http://www.w3.org/2003/05/soap-envelope/role/next", "http://www.w3.org/2003/05/soap-envelope/role/none",
                      "http://www.w3.org/2003/05/soap-envelope/role/ultimateReceiver")
        : ValueBuilder.getValuesFor("http://schemas.xmlsoap.org/soap/actor/next");
  }

}
