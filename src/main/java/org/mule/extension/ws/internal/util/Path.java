/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.util;

import org.mule.runtime.http.api.server.HttpServer;

import java.util.Objects;

import static org.mule.extension.ws.internal.util.PathUtils.resolveAbsolutePath;
import static org.mule.extension.ws.internal.util.PathUtils.resolveFullPath;

/**
 * Represent a path
 *
 * @since 2.0
 */
public class Path {

  private String resolvedPath;

  /**
   * Creates a new instance
   *
   * @param basePath   the server's base path
   * @param path       the path
   */
  public Path(String basePath, String path) {
    this.resolvedPath = basePath == null ? path : resolveFullPath(basePath, path);
  }

  public String getAbsolutePath(HttpServer server) {
    return server == null ? resolvedPath : resolveAbsolutePath(server, resolvedPath);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Path path = (Path) o;
    return Objects.equals(resolvedPath, path.resolvedPath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resolvedPath);
  }
}
