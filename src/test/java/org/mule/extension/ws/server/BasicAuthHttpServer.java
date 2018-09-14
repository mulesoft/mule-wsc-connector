/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.server;

import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.UserStore;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;

import static org.eclipse.jetty.servlet.ServletContextHandler.SESSIONS;
import static org.eclipse.jetty.util.security.Constraint.__BASIC_AUTH;
import static org.eclipse.jetty.util.security.Credential.getCredential;

public class BasicAuthHttpServer extends HttpServer {

  public static final String USERNAME = "juani";
  public static final String PASSWORD = "changeIt";

  public BasicAuthHttpServer(int port, Interceptor in, Interceptor out, Object serviceInstance) {
    super(port, in, out, serviceInstance);
  }

  @Override
  protected void init() {
    try {
      CXFNonSpringServlet cxf = new CXFNonSpringServlet();
      ServletHolder servlet = new ServletHolder(cxf);
      servlet.setName("server");
      servlet.setForcedPath("/");
      ServletContextHandler context = new ServletContextHandler(SESSIONS);
      context.setSecurityHandler(getBasicAuth());
      context.setContextPath("/");
      context.addServlet(servlet, "/*");
      httpServer.setHandler(context);
      httpServer.start();
      initializeServer(cxf);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private SecurityHandler getBasicAuth() {
    UserStore userStore = new UserStore();
    userStore.addUser(USERNAME, getCredential(PASSWORD), new String[] {"user"});

    HashLoginService l = new HashLoginService();
    l.setUserStore(userStore);
    l.setName("private");

    Constraint constraint = new Constraint();
    constraint.setName(__BASIC_AUTH);
    constraint.setRoles(new String[] {"user"});
    constraint.setAuthenticate(true);

    ConstraintMapping cm = new ConstraintMapping();
    cm.setConstraint(constraint);
    cm.setPathSpec("/*");

    ConstraintSecurityHandler csh = new ConstraintSecurityHandler();
    csh.setAuthenticator(new BasicAuthenticator());
    csh.setRealmName("testRealm");
    csh.addConstraintMapping(cm);
    csh.setLoginService(l);
    return csh;
  }

}
