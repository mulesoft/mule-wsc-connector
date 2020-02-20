/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.connection;

/**
 * Supplier that allows to throw checked exceptions.
 *
 * @since 1.6
 */
public interface ThrowingSupplier<T, E extends Exception> {

  /**
   * Gets a result.
   *
   * @return a result
   */
  T get() throws E;
}
