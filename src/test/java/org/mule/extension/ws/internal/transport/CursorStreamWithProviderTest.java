/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.extension.ws.internal.transport;

import org.junit.Before;
import org.junit.Test;
import org.mule.runtime.api.streaming.CursorProvider;
import org.mule.runtime.api.streaming.bytes.CursorStream;
import org.mule.runtime.api.streaming.bytes.CursorStreamProvider;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CursorStreamWithProviderTest {

  private CursorStream mockCursorStreamDelegate;
  private CursorStreamProvider mockCursorStreamProvider;
  private CursorStreamWithProvider cursorStreamWithProvider;

  @Before
  public void setUp() {
    mockCursorStreamDelegate = mock(CursorStream.class);
    mockCursorStreamProvider = mock(CursorStreamProvider.class);
    cursorStreamWithProvider = new CursorStreamWithProvider(mockCursorStreamDelegate, mockCursorStreamProvider);
  }

  @Test
  public void testCloseWithException() {
    try {
      doThrow(new IOException("Stream close error")).when(mockCursorStreamDelegate).close();
      cursorStreamWithProvider.close();
    } catch (IOException e) {
      assertEquals("Stream close error", e.getMessage());
    }
  }

  @Test
  public void testReadByteArray() throws IOException {
    byte[] buffer = new byte[10];
    when(mockCursorStreamDelegate.read(buffer)).thenReturn(5);

    int bytesRead = cursorStreamWithProvider.read(buffer);

    assertEquals(5, bytesRead);
    verify(mockCursorStreamDelegate).read(buffer);
  }

  @Test
  public void testReadByteArrayWithOffset() throws IOException {
    byte[] buffer = new byte[10];
    when(mockCursorStreamDelegate.read(buffer, 2, 5)).thenReturn(3);

    int bytesRead = cursorStreamWithProvider.read(buffer, 2, 5);

    assertEquals(3, bytesRead);
    verify(mockCursorStreamDelegate).read(buffer, 2, 5);
  }

  @Test
  public void testSkip() throws IOException {
    when(mockCursorStreamDelegate.skip(10)).thenReturn(10L);

    long skipped = cursorStreamWithProvider.skip(10);

    assertEquals(10, skipped);
    verify(mockCursorStreamDelegate).skip(10);
  }

  @Test
  public void testAvailable() throws IOException {
    when(mockCursorStreamDelegate.available()).thenReturn(100);

    int available = cursorStreamWithProvider.available();

    assertEquals(100, available);
    verify(mockCursorStreamDelegate).available();
  }

  @Test
  public void testClose() throws IOException {
    cursorStreamWithProvider.close();

    verify(mockCursorStreamDelegate).close();
    verify(mockCursorStreamProvider).close();
  }

  @Test
  public void testMark() {
    cursorStreamWithProvider.mark(50);

    verify(mockCursorStreamDelegate).mark(50);
  }

  @Test
  public void testReset() throws IOException {
    cursorStreamWithProvider.reset();

    verify(mockCursorStreamDelegate).reset();
  }

  @Test
  public void testMarkSupported() {
    when(mockCursorStreamDelegate.markSupported()).thenReturn(true);

    assertTrue(cursorStreamWithProvider.markSupported());
    verify(mockCursorStreamDelegate).markSupported();
  }

  @Test
  public void testReadSingleByte() throws IOException {
    when(mockCursorStreamDelegate.read()).thenReturn(42);

    int byteRead = cursorStreamWithProvider.read();

    assertEquals(42, byteRead);
    verify(mockCursorStreamDelegate).read();
  }

  @Test
  public void testGetPosition() {
    when(mockCursorStreamDelegate.getPosition()).thenReturn(123L);

    assertEquals(123L, cursorStreamWithProvider.getPosition());
    verify(mockCursorStreamDelegate).getPosition();
  }

  @Test
  public void testSeek() throws IOException {
    cursorStreamWithProvider.seek(200L);

    verify(mockCursorStreamDelegate).seek(200L);
  }

  @Test
  public void testRelease() {
    cursorStreamWithProvider.release();

    verify(mockCursorStreamDelegate).release();
  }

  @Test
  public void testIsReleased() {
    when(mockCursorStreamDelegate.isReleased()).thenReturn(true);

    assertTrue(cursorStreamWithProvider.isReleased());
    verify(mockCursorStreamDelegate).isReleased();
  }

  @Test
  public void testGetProvider() {
    CursorProvider mockCursorProvider = mock(CursorProvider.class);
    when(mockCursorStreamDelegate.getProvider()).thenReturn(mockCursorProvider);

    assertSame(mockCursorProvider, cursorStreamWithProvider.getProvider());
    verify(mockCursorStreamDelegate).getProvider();
  }

  @Test
  public void testMark_NullCursorStream_ThrowsException() {
    try {
      CursorStreamWithProvider cursorStreamWithProvider = new CursorStreamWithProvider(null, mock(CursorStreamProvider.class));
      cursorStreamWithProvider.mark(10); // Attempt to mark with null delegate
      fail("Expected NullPointerException, but no exception was thrown.");
    } catch (NullPointerException e) {
      // Expected exception thrown
    } catch (Exception e) {
      fail("Unexpected IOException thrown.");
    }
  }

  @Test
  public void testSkip_NullCursorStream_ThrowsException() {
    try {
      CursorStreamWithProvider cursorStreamWithProvider = new CursorStreamWithProvider(null, mock(CursorStreamProvider.class));
      cursorStreamWithProvider.skip(5); // Attempt to skip with null delegate
      fail("Expected NullPointerException, but no exception was thrown.");
    } catch (NullPointerException e) {
      // Expected exception thrown
    } catch (IOException e) {
      fail("Unexpected IOException thrown.");
    }
  }
}
