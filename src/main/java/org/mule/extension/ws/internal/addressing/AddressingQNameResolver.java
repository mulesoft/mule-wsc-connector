/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.addressing;

import javax.xml.namespace.QName;

public interface AddressingQNameResolver {

  QName getMessageIDQName();

  QName getToQName();

  QName getFromQName();

  QName getActionQName();

  QName getReplyToQName();

  QName getFaultToQName();

  QName getRelatesToQName();

  QName getRelationshipTypeQName();

  QName getAddressQName();
}
