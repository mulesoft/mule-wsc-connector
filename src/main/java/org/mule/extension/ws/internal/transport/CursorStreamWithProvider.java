/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.extension.ws.internal.transport;

import org.mule.runtime.api.streaming.CursorProvider;
import org.mule.runtime.api.streaming.bytes.CursorStream;
import org.mule.runtime.api.streaming.bytes.CursorStreamProvider;
import org.mule.runtime.core.internal.streaming.bytes.InputStreamBuffer;

import java.io.IOException;
import java.io.InputStream;

import org.jetbrains.annotations.NotNull;

/**
 * {@link CursorStream} that delegates to a {@link CursorStream} delegate and maintains a reference of the delegate to its
 * {@link CursorStreamProvider}.
 * <p>
 * The purpose of this class is to avoid the runtime to close the {@link CursorStream} before it is consumed. We achieve this
 * maintaining a hard reference of the {@link CursorStream} delegate to its {@link CursorStreamProvider}.
 *
 * @since 1.4.2
 */
public class CursorStreamWithProvider extends CursorStream {

  private CursorStream cursorStreamDelegate;
  private CursorStreamProvider cursorStreamDelegateProvider;

  public CursorStreamWithProvider(CursorStream cursorStreamDelegate, CursorStreamProvider cursorStreamDelegateProvider) {
    this.cursorStreamDelegate = cursorStreamDelegate;
    this.cursorStreamDelegateProvider = cursorStreamDelegateProvider;
  }

  @Override
  public int read(@NotNull byte[] b) throws IOException {
    return cursorStreamDelegate.read(b);
  }

  @Override
  public int read(@NotNull byte[] b, int off, int len) throws IOException {
    return cursorStreamDelegate.read(b, off, len);
  }

  @Override
  public long skip(long n) throws IOException {
    return cursorStreamDelegate.skip(n);
  }

  @Override
  public int available() throws IOException {
    return cursorStreamDelegate.available();
  }

  @Override
  public void close() throws IOException {
    cursorStreamDelegate.close();
    cursorStreamDelegateProvider.close();
  }

  @Override
  public synchronized void mark(int readlimit) {
    cursorStreamDelegate.mark(readlimit);
  }

  @Override
  public synchronized void reset() throws IOException {
    cursorStreamDelegate.reset();
  }

  @Override
  public boolean markSupported() {
    return cursorStreamDelegate.markSupported();
  }

  @Override
  public int read() throws IOException {
    return cursorStreamDelegate.read();
  }

  @Override
  public long getPosition() {
    return cursorStreamDelegate.getPosition();
  }

  @Override
  public void seek(long position) throws IOException {
    cursorStreamDelegate.seek(position);
  }

  @Override
  public void release() {
    cursorStreamDelegate.release();
  }

  @Override
  public boolean isReleased() {
    return cursorStreamDelegate.isReleased();
  }

  @Override
  public CursorProvider getProvider() {
    return cursorStreamDelegate.getProvider();
  }
}
