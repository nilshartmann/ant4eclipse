/**********************************************************************
 * Copyright (c) 2005-2009 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.testframework;

import org.ant4eclipse.lib.core.DefaultConfigurator;
import org.ant4eclipse.lib.core.service.ServiceRegistry;
import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.ant4eclipse.lib.core.util.StringMap;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

/**
 * <p>
 * Base class for all AntEclipse test cases that require a configured {@link ServiceRegistry} and are not executed
 * within an Ant environment.
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class ConfigurableAnt4EclipseTestCase {

  /**
   * This setup method sets up the {@link Ant4EclipseConfigurationProperties} and the {@link ServiceRegistry}.
   * 
   * <p>
   * The creation of both objects can be customized for a test by overwriting
   * {@link #setupAnt4EclipseConfigurationProperties()} resp. {@link #setupServiceRegistry()}
   */
  @Before
  public void setup() {
    StringMap properties = customAnt4EclipseConfiguration(new StringMap());
    if (properties == null) {
      DefaultConfigurator.configureAnt4Eclipse();
    } else {
      DefaultConfigurator.configureAnt4Eclipse(properties);
    }
  }

  @After
  public void dispose() {
    try {
      if (ServiceRegistryAccess.isConfigured()) {
        ServiceRegistryAccess.reset();
      }
    } catch (Exception ex) {
      System.err.println("[Ant4EclipseTestCase] Could not reset ServiceRegistry: " + ex);
      ex.printStackTrace();
      Assert.fail(ex.getMessage());
    }
  }

  /**
   * Provides a set of properties used for the configuration. The supplied set is supposed to be altered and returned.
   * If a <code>null</code> value is returned the default configuration takes place.
   * 
   * @param properties
   *          The current set of properties which is supposed to be altered. Not <code>null</code>.
   * 
   * @return The altered properties or <code>null</code>.
   */
  protected StringMap customAnt4EclipseConfiguration(StringMap properties) {
    return null;
  }

} /* ENDCLASS */
