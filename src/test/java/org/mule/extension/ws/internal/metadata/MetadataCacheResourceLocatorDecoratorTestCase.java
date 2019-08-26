/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.metadata;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.hamcrest.core.Is.is;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataCache;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.wsdl.parser.locator.ResourceLocator;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class MetadataCacheResourceLocatorDecoratorTestCase {

  private static final String REMOTE = "http://tshirt-service.cloudhub.io/?wsdl";
  private static final String RELATIVE_PATH = "wsdl/document.wsdl";
  private static final URL FULL_PATH_URL = Thread.currentThread().getContextClassLoader().getResource(RELATIVE_PATH);

  @Test
  public void remoteKey() {
    TestCache cache = new TestCache();
    MetadataCacheResourceLocatorDecorator locator = new MetadataCacheResourceLocatorDecorator(cache, new TestLocator());
    locator.getResource(REMOTE);

    assertThat(cache.delegate.size(), is(1));
    assertThat(cache.delegate, hasKey(REMOTE));
  }

  @Test
  public void relativePathKey() {
    TestCache cache = new TestCache();
    MetadataCacheResourceLocatorDecorator locator = new MetadataCacheResourceLocatorDecorator(cache, new TestLocator());
    locator.getResource(RELATIVE_PATH);

    assertThat(cache.delegate.size(), is(0));
  }

  @Test
  public void fullPathKey() {
    TestCache cache = new TestCache();
    MetadataCacheResourceLocatorDecorator locator = new MetadataCacheResourceLocatorDecorator(cache, new TestLocator());
    locator.getResource(FULL_PATH_URL.getPath());

    assertThat(cache.delegate.size(), is(1));
    assertThat(cache.delegate, hasKey("document.wsdl"));
  }

  @Test
  public void fullPathAsURLKey() {
    TestCache cache = new TestCache();
    MetadataCacheResourceLocatorDecorator locator = new MetadataCacheResourceLocatorDecorator(cache, new TestLocator());
    locator.getResource(FULL_PATH_URL.toString());

    assertThat(cache.delegate.size(), is(1));
    assertThat(cache.delegate, hasKey("document.wsdl"));
  }

  private class TestCache implements MetadataCache {

    private Map<Serializable, Serializable> delegate = new HashMap<>();

    @Override
    public void put(Serializable s1, Serializable s2) {
      delegate.put(s1, s2);
    }

    @Override
    public void putAll(Map<? extends Serializable, ? extends Serializable> map) {
      delegate.putAll(map);
    }

    @Override
    public Optional<Serializable> get(Serializable serializable) {
      return Optional.ofNullable(delegate.get(serializable));
    }

    @Override
    public Serializable computeIfAbsent(Serializable serializable, MetadataCacheValueResolver cacheValueResolver) {
      return delegate.computeIfAbsent(serializable, s -> {
        try {
          return cacheValueResolver.compute(s);
        } catch (MetadataResolvingException | ConnectionException e) {
          throw new RuntimeException(e);
        }
      });
    }
  }

  private class TestLocator implements ResourceLocator {

    @Override
    public boolean handles(@NotNull String s) {
      return true;
    }

    @Override
    public InputStream getResource(@NotNull String s) {
      return new ByteArrayInputStream(new byte[10]);
    }
  }
}
