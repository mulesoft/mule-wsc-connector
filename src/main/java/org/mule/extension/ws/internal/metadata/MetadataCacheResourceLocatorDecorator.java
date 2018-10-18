/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.metadata;

import static org.mule.runtime.core.api.util.IOUtils.toByteArray;

import org.mule.runtime.api.metadata.MetadataCache;
import org.mule.wsdl.parser.locator.GlobalResourceLocator;
import org.mule.wsdl.parser.locator.ResourceLocator;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * {@link ResourceLocator} implementation that tries to fetch a resource from the {@link MetadataCache} first, if not there, then
 * delegates the fetching to another locator.
 *
 * @since 1.2
 */
public class MetadataCacheResourceLocatorDecorator implements ResourceLocator {

  private final ResourceLocator delegate;
  private final MetadataCache cache;

  MetadataCacheResourceLocatorDecorator(MetadataCache cache) {
    this.delegate = new GlobalResourceLocator();
    this.cache = cache;
  }

  @Override
  public boolean handles(String url) {
    return delegate.handles(url);
  }

  @Override
  public InputStream getResource(String url) {
    try {
      byte[] resource = (byte[]) cache.get(url).orElseGet(() -> {
        byte[] bytes = toByteArray(delegate.getResource(url));
        cache.put(url, bytes);
        return bytes;
      });
      return new ByteArrayInputStream(resource);
    } catch (Exception e) {
      throw new RuntimeException("Error while obtaining resource [" + url + "]", e);
    }
  }
}
