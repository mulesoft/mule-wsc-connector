/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.util;

import org.mule.runtime.http.api.server.HttpServer;
import org.mule.runtime.http.api.server.ServerAddress;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mule.runtime.http.api.HttpConstants.Protocol.HTTP;
import static org.mule.runtime.http.api.HttpConstants.Protocol;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PathUtilsTestCase {

  private static final String PATH = "path";
  private static final String PATH_WITH_END_SLASH = "path/";
  private static final String PATH_WITH_START_SLASH = "/path";

  private static final String ADDRESS = "0.0.0.0";
  private static final int PORT = 8000;
  private static final Protocol PROTOCOL = HTTP;
  private static final String SERVER_PATH = PROTOCOL.getScheme() + "://" + ADDRESS + ":" + PORT;
  private static final String ABSOLUTE_PATH = SERVER_PATH + PATH_WITH_START_SLASH;

  private HttpServer server;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void before() {
    server = mock(HttpServer.class);
    ServerAddress address = mock(ServerAddress.class);
    when(server.getServerAddress()).thenReturn(address);
    when(address.getIp()).thenReturn(ADDRESS);
    when(address.getPort()).thenReturn(PORT);
    when(server.getProtocol()).thenReturn(PROTOCOL);
  }

  @Test
  public void fullPathTest() {
    Path path = PathUtils.getFullPath(PATH_WITH_START_SLASH, PATH_WITH_START_SLASH);
    Path expected = new Path(PATH_WITH_START_SLASH, PATH_WITH_START_SLASH);
    assertThat(path.getAbsolutePath(null), is(expected.getAbsolutePath(null)));
  }

  @Test
  public void invalidFullPathTest() {
    expectedException.expectMessage("path must start with /");
    PathUtils.getFullPath(PATH, PATH_WITH_START_SLASH);
  }

  @Test
  public void sanitizePathWithStartSlashTest() {
    assertThat(PathUtils.sanitizePathWithStartSlash(PATH), is(PATH_WITH_START_SLASH));
    assertThat(PathUtils.sanitizePathWithStartSlash(PATH), is(PATH_WITH_START_SLASH));
  }

  @Test
  public void sanitizeNullPathWithStartSlashTest() {
    assertThat(PathUtils.sanitizePathWithStartSlash(null), is((String) null));
  }

  @Test
  public void pathWithoutEndSlashTest() {
    assertThat(PathUtils.pathWithoutEndSlash(PATH), is(PATH));
    assertThat(PathUtils.pathWithoutEndSlash(PATH_WITH_END_SLASH), is(PATH));
  }

  @Test
  public void resolveFullPathWithNullBasePathTest() {
    String basePath = null;
    assertThat(PathUtils.resolveFullPath(basePath, PATH), is(basePath));
  }

  @Test
  public void resolveFullPathWithEmptyBasePathTest() {
    String basePath = "";
    assertThat(PathUtils.resolveFullPath(basePath, PATH), is(basePath));
  }

  @Test
  public void resolveFullPathWithNullPathTest() {
    String basePath = PATH_WITH_START_SLASH;
    assertThat(PathUtils.resolveFullPath(basePath, null), is(basePath));
  }

  @Test
  public void resolveFullPathWithEmptyPathTest() {
    String basePath = PATH_WITH_START_SLASH;
    assertThat(PathUtils.resolveFullPath(basePath, ""), is(basePath));
  }

  @Test
  public void resolveFullPathTest() {
    String basePath = PATH_WITH_START_SLASH;
    String path = PATH_WITH_START_SLASH;
    assertThat(PathUtils.resolveFullPath(basePath, path), is(basePath + path));
  }

  @Test
  public void resolveServerPathTest() {
    String path = PathUtils.resolveServerPath(server);
    assertThat(path, is(SERVER_PATH));
  }

  @Test
  public void invalidResolveServerPathTest() {
    expectedException.expectMessage("server cannot be null");
    PathUtils.resolveServerPath(null);
  }

  @Test
  public void resolveAbsolutePathTest() {
    String path = PathUtils.resolveAbsolutePath(server, PATH);
    assertThat(path, is(ABSOLUTE_PATH));
  }
}
