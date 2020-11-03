/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.reliablemessaging;

import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;

import org.mule.runtime.api.lock.LockFactory;
import org.mule.runtime.api.store.ObjectStore;
import org.mule.soap.api.rm.ReliableMessagingStore;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;

/**
 * Implementation of {@link ReliableMessagingStore} that uses {@link ObjectStore} and distinguishes values stored from
 * different configuration using the configuration name.
 *
 * @since 1.6
 */
public class ReliableMessagingStoreImpl implements ReliableMessagingStore {

  private final ObjectStore<Serializable> objectStore;
  private final LockFactory lockFactory;
  private final String configName;
  private final String keyFormat = "%s-%s";

  public ReliableMessagingStoreImpl(ObjectStore<Serializable> objectStore, LockFactory lockFactory, String configName) {
    this.objectStore = objectStore;
    this.lockFactory = lockFactory;
    this.configName = configName;
  }

  @Override
  public void store(String key, Serializable value) throws Exception {
    objectStore.store(format(keyFormat, key, configName), value);
  }

  @Override
  public void update(String key, Serializable value) throws Exception {
    String keyName = format(keyFormat, key, configName);
    Lock lock = lockFactory.createLock(key + "-lock");
    lock.lock();
    try {
      objectStore.remove(keyName);
      objectStore.store(keyName, value);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public Serializable retrieve(String key) throws Exception {
    return objectStore.retrieve(format(keyFormat, key, configName));
  }

  @Override
  public Serializable remove(String key) throws Exception {
    return objectStore.remove(format(keyFormat, key, configName));
  }

  @Override
  public Map retrieveAll() throws Exception {
    return objectStore.retrieveAll()
        .entrySet()
        .stream()
        .filter(entry -> entry.getKey().endsWith(configName))
        .collect(toMap(Entry::getKey, Entry::getValue));
  }

}
