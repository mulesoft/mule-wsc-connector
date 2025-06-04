/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.metadata;


import static java.util.Arrays.asList;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MetadataCacheResourceLocatorDecoratorTestCase {

  private static final String REMOTE = "http://tshirt-service.cloudhub.io/?wsdl";
  private static final String RELATIVE_PATH = "wsdl/document.wsdl";
  private static final URL FULL_PATH_URL = Thread.currentThread().getContextClassLoader().getResource(RELATIVE_PATH);
  private static final String FILE_NAME = "document.wsdl";
  private static final int TIMES_TO_GET_RESOURCE = 10;

  @Parameter
  public String parameterizationName;

  @Parameter(1)
  public String url;

  @Parameter(2)
  public String cacheKey;

  @Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return asList(new Object[][] {
        {"Remote key", REMOTE, REMOTE},
        {"Relative path key", RELATIVE_PATH, FILE_NAME},
        {"Full path key", FULL_PATH_URL.getPath(), FILE_NAME},
        {"Full path as URL key", FULL_PATH_URL.toString(), FILE_NAME}
    });
  }

  @Test
  public void resourceIsCached() {
    TestCache cache = new TestCache();
    MetadataCacheResourceLocatorDecorator locator = new MetadataCacheResourceLocatorDecorator(cache, new TestLocator());
    locator.getResource(url);

    assertThat(cache.delegate.size(), is(1));
    assertThat(cache.delegate, hasKey(cacheKey));
  }

  @Test
  public void resourceIsCachedOnlyOnce() {
    TestCache cache = new TestCache();
    MetadataCacheResourceLocatorDecorator locator = new MetadataCacheResourceLocatorDecorator(cache, new TestLocator());
    for (int i = 0; i < TIMES_TO_GET_RESOURCE; i++) {
      locator.getResource(url);
    }
    assertThat(cache.getTimesValueIsCached(), is(1));
  }

  private class TestCache implements MetadataCache {

    private int timesValueIsCached = 0;
    private Map<Serializable, Serializable> delegate = new HashMap<>();

    @Override
    public void put(Serializable s1, Serializable s2) {
      timesValueIsCached++;
      delegate.put(s1, s2);
    }

    @Override
    public void putAll(Map<? extends Serializable, ? extends Serializable> map) {
      timesValueIsCached++;
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

    public int getTimesValueIsCached() {
      return timesValueIsCached;
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
