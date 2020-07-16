/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.internal.util;

import org.mule.runtime.http.api.server.HttpServer;

import static org.mule.runtime.api.util.Preconditions.checkArgument;

/**
 * Utility methods
 *
 * @since 2.0
 */
public final class PathUtils {

  public static Path getFullPath(String path, String basePath) {
    checkArgument(path.startsWith("/"), "path must start with /");
    return new Path(basePath, path);
  }

  public static String sanitizePathWithStartSlash(String path) {
    if (path == null) {
      return null;
    }
    return path.startsWith("/") ? path : "/" + path;
  }

  public static String pathWithoutEndSlash(String path) {
    if (path.endsWith("/")) {
      return path.substring(0, path.length() - 1);
    } else {
      return path;
    }
  }

  /**
   * Concatenates basePath and path with the expected /basePath/path format.
   * If base path is empty or null basePath is returned.
   */
  public static String resolveFullPath(String basePath, String path) {
    if (basePath == null || basePath.isEmpty()) {
      return basePath;
    }

    String sanitizedBasePath = sanitizePathWithStartSlash(basePath);

    sanitizedBasePath = pathWithoutEndSlash(sanitizedBasePath);

    if (path == null || path.isEmpty()) {
      return sanitizedBasePath;
    }

    String resourcePath = sanitizePathWithStartSlash(path);

    return sanitizedBasePath + resourcePath;
  }

  public static String resolveServerPath(HttpServer server) {
    checkArgument(server != null, "server cannot be null");
    return server.getProtocol().getScheme() + "://" + server.getServerAddress().getIp() + ":"
        + server.getServerAddress().getPort();
  }

  public static String resolveAbsolutePath(HttpServer server, String relativePath) {
    return resolveAbsolutePath(resolveServerPath(server), relativePath);
  }

  public static String resolveAbsolutePath(String serverPath, String relativePath) {
    return pathWithoutEndSlash(serverPath) + sanitizePathWithStartSlash(relativePath);
  }

  private PathUtils() {}
}
