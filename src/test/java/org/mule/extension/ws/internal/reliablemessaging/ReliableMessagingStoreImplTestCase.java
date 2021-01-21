/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.reliablemessaging;

import org.mule.runtime.api.lock.LockFactory;
import org.mule.runtime.api.store.ObjectStore;
import org.mule.runtime.api.store.ObjectStoreException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class ReliableMessagingStoreImplTestCase {

  private final String CONFIG_NAME = "configName";
  private final String KEY = "key";

  private Serializable serializable;
  private ReliableMessagingStoreImpl store;

  @Before
  public void before() {
    LockFactory lockFactory = mock(LockFactory.class);
    store = new ReliableMessagingStoreImpl(new InMemoryObjectStore(), lockFactory, CONFIG_NAME);
    serializable = mock(Serializable.class);
  }

  @Test
  public void storeAndRetrieve() throws Exception {
    store.store(KEY, serializable);
    assertThat(store.retrieve(KEY), is(serializable));
  }

  @Test
  public void storeAndRemove() throws Exception {
    store.store(KEY, serializable);
    assertThat(store.retrieveAll().size(), is(1));
    store.remove(KEY);
    assertThat(store.retrieveAll().size(), is(0));
  }

  @Test
  public void storeAndRetrieveAll() throws Exception {
    List<String> keys = asList("B", "H", "A", "C");
    for (String key : keys) {
      store.store(key, serializable);
    }
    assertThat(new ArrayList<String>(store.retrieveAll().keySet()), containsInAnyOrder(keys.toArray()));
  }

  private class InMemoryObjectStore implements ObjectStore<Serializable> {

    private final Map<String, Serializable> store = new HashMap<>();

    @Override
    public boolean contains(String s) throws ObjectStoreException {
      return store.containsKey(s);
    }

    @Override
    public void store(String s, Serializable serializable) throws ObjectStoreException {
      store.put(s, serializable);
    }

    @Override
    public Serializable retrieve(String s) throws ObjectStoreException {
      return store.get(s);
    }

    @Override
    public Serializable remove(String s) throws ObjectStoreException {
      return store.remove(s);
    }

    @Override
    public boolean isPersistent() {
      return false;
    }

    @Override
    public void clear() throws ObjectStoreException {

    }

    @Override
    public void open() throws ObjectStoreException {

    }

    @Override
    public void close() throws ObjectStoreException {

    }

    @Override
    public List<String> allKeys() throws ObjectStoreException {
      return new ArrayList<>(store.keySet());
    }

    @Override
    public Map<String, Serializable> retrieveAll() throws ObjectStoreException {
      return store;
    }
  }
}
