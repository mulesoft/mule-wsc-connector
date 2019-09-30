/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.metadata;

import static org.mule.runtime.core.api.util.IOUtils.toByteArray;

import org.mule.runtime.api.metadata.MetadataCache;
import org.mule.wsdl.parser.locator.ResourceLocator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link ResourceLocator} implementation that tries to fetch a resource from the {@link MetadataCache} first, if not there, then
 * delegates the fetching to another locator.
 *
 * @since 1.2
 */
public class MetadataCacheResourceLocatorDecorator implements ResourceLocator {

  private static final Logger LOGGER = LoggerFactory.getLogger(MetadataCacheResourceLocatorDecorator.class.getName());

  private final ResourceLocator delegate;
  private final MetadataCache cache;

  MetadataCacheResourceLocatorDecorator(MetadataCache cache, ResourceLocator resourceLocator) {
    this.delegate = resourceLocator;
    this.cache = cache;
  }

  @Override
  public boolean handles(String url) {
    return delegate.handles(url);
  }

  @Override
  public InputStream getResource(String url) {
    try {
      Optional<String> optionalCacheKey = getCacheKey(url);

      byte[] resource;

      if (optionalCacheKey.isPresent()) {
        String cacheKey = optionalCacheKey.get();
        resource = (byte[]) cache.get(cacheKey).orElseGet(() -> {
          byte[] bytes = fetchResource(url);
          cache.put(cacheKey, bytes);
          return bytes;
        });
      } else {
        resource = fetchResource(url);
      }

      return new ByteArrayInputStream(resource);
    } catch (Exception e) {
      throw new RuntimeException("Error while obtaining resource [" + url + "]", e);
    }
  }

  private byte[] fetchResource(String url) {
    return toByteArray(delegate.getResource(url));
  }

  // TODO: Remove once MULE-17388 is done, this will create a cache key with the file name, but wont be necessary if the
  //  relative path of the file is used instead, by trimming the file path we avoid caching the same WSDL multiple times because
  //  Tooling Client generates a new app for every metadata resolution creating a new copy of the file (changing it's path)
  //  that ends up in a different the key for the same file content.
  private Optional<String> getCacheKey(String url) {
    File file = null;
    try {
      // check for files paths, not file URLs.
      file = new File(url);
      if (file.exists()) {
        return Optional.of(file.getName());
      }
      // check for URLs
      URL urlInstance = new URL(url);
      File urlFile = new File(urlInstance.getFile());
      return Optional.of(urlFile.exists() ? urlFile.getName() : url);
    } catch (Exception e) {
      if (file == null) {
        return Optional.empty();
      }
      String fileName = file.getName();
      LOGGER.debug("{} is not an absolute path for an existing file or a valid URL. Using cache key {}", url, fileName);
      return Optional.of(fileName);
    }
  }
}
