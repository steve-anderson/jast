/*
  File: JAXActivator

  Copyright 2018, Steven W. Anderson
*/
package net.swahome.jast.front.server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Enable JAX-RS and define the root path.
 */
@ApplicationPath("api")
public class JAXActivator extends Application {
}
