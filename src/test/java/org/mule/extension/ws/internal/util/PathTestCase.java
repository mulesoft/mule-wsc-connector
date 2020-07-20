/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.util;

import org.mule.runtime.http.api.HttpConstants;
import org.mule.runtime.http.api.server.HttpServer;
import org.mule.runtime.http.api.server.ServerAddress;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mule.runtime.http.api.HttpConstants.Protocol.HTTP;

public class PathTestCase {

  private static final String PATH_WITH_START_SLASH = "/path";

  private static final String ADDRESS = "0.0.0.0";
  private static final int PORT = 8000;
  private static final HttpConstants.Protocol PROTOCOL = HTTP;
  private static final String SERVER_PATH = PROTOCOL.getScheme() + "://" + ADDRESS + ":" + PORT;

  private HttpServer server;

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
  public void getAbsolutePathWithoutServerAndBasePathTest() {
    String basepath = null;
    HttpServer server = null;

    Path sut = new Path(basepath, PATH_WITH_START_SLASH);
    String absolutePath = sut.getAbsolutePath(server);

    assertThat(absolutePath, is(PATH_WITH_START_SLASH));
  }

  @Test
  public void getAbsolutePathWithoutServerTest() {
    HttpServer server = null;

    Path sut = new Path(PATH_WITH_START_SLASH, PATH_WITH_START_SLASH);
    String absolutePath = sut.getAbsolutePath(server);

    assertThat(absolutePath, is(PATH_WITH_START_SLASH + PATH_WITH_START_SLASH));
  }

  @Test
  public void getAbsolutePathTest() {
    Path sut = new Path(PATH_WITH_START_SLASH, PATH_WITH_START_SLASH);
    String absolutePath = sut.getAbsolutePath(server);

    assertThat(absolutePath, is(SERVER_PATH + PATH_WITH_START_SLASH + PATH_WITH_START_SLASH));
  }
}
