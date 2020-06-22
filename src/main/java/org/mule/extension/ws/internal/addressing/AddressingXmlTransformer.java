/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.addressing;

import org.mule.extension.ws.internal.addressing.properties.AddressingProperties;
import org.mule.runtime.api.el.BindingContext;
import org.mule.runtime.api.el.MuleExpressionLanguage;
import org.mule.extension.ws.internal.addressing.properties.EndpointReferenceType;
import org.mule.extension.ws.internal.addressing.properties.RelatesToType;
import org.mule.extension.ws.internal.addressing.properties.URIType;
import org.mule.runtime.api.metadata.TypedValue;

import javax.xml.namespace.QName;

import static org.mule.runtime.api.metadata.DataType.BOOLEAN;
import static org.mule.runtime.api.metadata.DataType.STRING;

/**
 * Transform {@link AddressingProperties} model's types into xml {@link String}
 * based on dataweave expressions
 *
 * @since 2.0
 */
public class AddressingXmlTransformer implements AddressingTransformer {

  private final String simpleExpression = "%dw 2.0\n" +
      "fun element(ns0: Namespace, tag: String, mustUnderstand: Boolean, obj: Any) = \n" +
      "if (mustUnderstand)\n" +
      "{ ns0#\"$(tag)\" @(mustUnderstand:true): obj }\n" +
      "else \n" +
      "{ ns0#\"$(tag)\": obj }\n" +
      "fun namespace(uri: String, prefix: String) = {uri: uri, prefix: prefix} as Namespace\n" +
      "var wsa = namespace(namespaceUri, namespacePrefix)\n" +
      "output application/xml\n" +
      "---\n" +
      "element(wsa, tagName, mustUnderstand, value)";

  private final String endpointReferenceExpression = "%dw 2.0\n" +
      "fun element(ns0: Namespace, tag: String, mustUnderstand: Boolean, obj: Any) = \n" +
      "if (mustUnderstand)\n" +
      "{ ns0#\"$(tag)\" @(mustUnderstand:true): obj }\n" +
      "else \n" +
      "{ ns0#\"$(tag)\": obj }\n" +
      "fun namespace(uri: String, prefix: String) = {uri: uri, prefix: prefix} as Namespace\n" +
      "var wsa = namespace(namespaceUri, namespacePrefix)\n" +
      "output application/xml\n" +
      "---\n" +
      "element(wsa, tagName, mustUnderstand, element(wsa, \"Address\", false, address))";

  private final String relatesToWithRelationshipExpression = "%dw 2.0\n" +
      "fun element(ns0: Namespace, tag: String, mustUnderstand: Boolean, relationship: String, obj: Any) = \n" +
      "if (mustUnderstand)\n" +
      "{ ns0#\"$(tag)\" @(mustUnderstand:true, RelationshipType:relationship) : obj }\n" +
      "else \n" +
      "{ ns0#\"$(tag)\" @(RelationshipType:relationship): obj }\n" +
      "fun namespace(uri: String, prefix: String) = {uri: uri, prefix: prefix} as Namespace\n" +
      "var wsa = namespace(namespaceUri, namespacePrefix)\n" +
      "output application/xml\n" +
      "---\n" +
      "element(wsa, tagName, mustUnderstand, relationship, value)";

  private final MuleExpressionLanguage expressionExecutor;

  public AddressingXmlTransformer(MuleExpressionLanguage expressionExecutor) {
    this.expressionExecutor = expressionExecutor;
  }

  @Override
  public String transform(EndpointReferenceType endpoint, QName qname, boolean mustUnderstand) {
    BindingContext context = getDefaultBindingContext(qname, mustUnderstand)
        .addBinding("address", new TypedValue(endpoint.getAddress().getValue(), STRING))
        .build();
    return execute(endpointReferenceExpression, context);
  }

  @Override
  public String transform(URIType uri, QName qname, boolean mustUnderstand) {
    return transform(uri.getValue(), qname, mustUnderstand);
  }

  @Override
  public String transform(RelatesToType relatesTo, QName qname, boolean mustUnderstand) {
    if (!relatesTo.getRelationShip().isPresent()) {
      return transform(relatesTo.getValue(), qname, mustUnderstand);
    }

    BindingContext context = getDefaultBindingContext(qname, mustUnderstand)
        .addBinding("value", new TypedValue(relatesTo.getValue(), STRING))
        .addBinding("relationship", new TypedValue(relatesTo.getRelationShip().get(), STRING))
        .build();

    return expressionExecutor.evaluate(relatesToWithRelationshipExpression, context).getValue().toString();
  }

  public String transform(String value, QName qname, boolean mustUnderstand) {
    BindingContext context = getDefaultBindingContext(qname, mustUnderstand)
        .addBinding("value", new TypedValue(value, STRING))
        .build();
    return execute(simpleExpression, context);
  }

  private BindingContext.Builder getDefaultBindingContext(QName qname, boolean mustUnderstand) {
    return BindingContext.builder()
        .addBinding("namespaceUri", new TypedValue(qname.getNamespaceURI(), STRING))
        .addBinding("namespacePrefix", new TypedValue(qname.getPrefix(), STRING))
        .addBinding("tagName", new TypedValue(qname.getLocalPart(), STRING))
        .addBinding("mustUnderstand", new TypedValue(mustUnderstand, BOOLEAN));
  }

  private String execute(String expression, BindingContext context) {
    return expressionExecutor.evaluate(expression, context).getValue().toString();
  }
}
