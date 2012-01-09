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
package org.ant4eclipse.ant.platform.team;

import org.ant4eclipse.ant.platform.core.task.AbstractTeamProjectSetBasedTask;
import org.ant4eclipse.ant.platform.internal.team.VcsAdapter;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.platform.model.team.projectset.TeamProjectDescription;
import org.ant4eclipse.lib.platform.model.team.projectset.TeamProjectSet;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.EnumeratedAttribute;

import java.io.File;
import java.util.List;

public abstract class AbstractGetProjectSetTask extends AbstractTeamProjectSetBasedTask {

  /** Value for the 'command' parameter, that indicates that the projects should be checked out from version control */
  private static final String CHECKOUT                = "checkout";

  /** Value for the 'command' parameter, that indicates that the projects should be updated from version control */
  private static final String UPDATE                  = "update";

  /** Value for the 'command' parameter, that indicates that the projects should be exported from version control */
  private static final String EXPORT                  = "export";

  /**
   * Comment for <code>_command</code>
   */
  private VcsCommand          _command                = new VcsCommand( CHECKOUT );

  private VcsAdapter          _vcsAdapter;

  private String              _username;

  private String              _password;

  private File                _destination;

  private boolean             _deleteExistingProjects = true;

  public String getPassword() {
    return _password;
  }

  public File getDestination() {
    return _destination;
  }

  /**
   * Sets the destination directory.
   * 
   * @param destination
   */
  public void setDestination( File destination ) {
    _destination = destination;
  }

  public void setPassword( String password ) {
    _password = password;
  }

  public String getUsername() {
    return _username;
  }

  public void setUsername( String username ) {
    _username = username;
  }

  public boolean isDeleteExistingProjects() {
    return _deleteExistingProjects;
  }

  public void setDeleteExistingProjects( boolean deleteExistingProjects ) {
    _deleteExistingProjects = deleteExistingProjects;
  }

  /**
   * @return Returns the command.
   */
  public VcsCommand getCommand() {
    return _command;
  }

  /**
   * @param command
   *          The command to set.
   */
  // Assure.notNull( "command", command );
  public void setCommand( VcsCommand command ) {
    _command = command;
  }

  /**
   * Overwrite in subclasses to check additional prerequisits
   * 
   */
  protected abstract void checkPrereqs();

  /**
   * {@inheritDoc}
   */
  @Override
  public void doExecute() throws BuildException {
    // check mandatory attributes..
    requireDestinationSet();
    requireProjectSetSet();
    requireCommandSet();
    checkPrereqs();

    _vcsAdapter = createVcsAdapter();
    A4ELogging.debug( "using version control adapter = %s", _vcsAdapter );

    // set user and password
    getProjectSet().setUserAndPassword( getUsername(), getPassword() );

    if( getCommand().getValue().equals( CHECKOUT ) ) {
      checkoutProjectSet( getDestination(), getProjectSet(), isDeleteExistingProjects() );
    } else if( getCommand().getValue().equals( UPDATE ) ) {
      updateProjectSet( getDestination(), getProjectSet() );
    }
    if( getCommand().getValue().equals( EXPORT ) ) {
      exportProjectSet( getDestination(), getProjectSet(), isDeleteExistingProjects() );
    }
  }

  protected abstract VcsAdapter createVcsAdapter();

  /**
   * Ensures that the destination-Parameter has been set correctly
   */
  private void requireDestinationSet() {
    if( getDestination() == null || !getDestination().isDirectory() ) {
      throw new BuildException( "Parameter 'destination' must be set to an existing directory" );
    }
  }

  /**
   * 
   */
  private void requireCommandSet() {
    // check that command is set..
    if( getCommand() == null ) {
      throw new BuildException( "command has to be set!" );
    }
  }

  /**
   * @param workspace
   * @param projectSet
   * @param deleteExisting
   */
  // Assure.isDirectory( "destination", destination );
  // Assure.notNull( "projectSet", projectSet );
  public void checkoutProjectSet( File destination, TeamProjectSet projectSet, boolean deleteExisting ) throws Ant4EclipseException {
    A4ELogging.debug( "checkoutProjectSet(%s, %s, %s)", destination, projectSet, Boolean.valueOf( deleteExisting ) );
    List<TeamProjectDescription> _teamProjectDescription = projectSet.getTeamProjectDescriptions();
    for( TeamProjectDescription teamProjectDescription : _teamProjectDescription ) {
      _vcsAdapter.checkoutProject( destination, teamProjectDescription, deleteExisting );
    }
  }

  /**
   * @param workspace
   * @param projectSet
   * @param deleteExisting
   */
  // Assure.isDirectory( "destination", destination );
  // Assure.notNull( "projectSet", projectSet );
  public void exportProjectSet( File destination, TeamProjectSet projectSet, boolean deleteExisting )
      throws Ant4EclipseException {
    List<TeamProjectDescription> descriptions = projectSet.getTeamProjectDescriptions();
    for( TeamProjectDescription description : descriptions ) {
      _vcsAdapter.exportProject( destination, description, deleteExisting );
    }
  }

  /**
   * @param workspace
   * @param projectSet
   */
  // Assure.isDirectory( "destination", destination );
  // Assure.notNull( "projectSet", projectSet );
  public void updateProjectSet( File destination, TeamProjectSet projectSet ) throws Ant4EclipseException {
    List<TeamProjectDescription> descriptions = projectSet.getTeamProjectDescriptions();
    for( TeamProjectDescription description : descriptions ) {
      _vcsAdapter.updateProject( destination, description );
    }
  }

  /**
   * Represents allowed values for the 'command'-parameter of the task
   * 
   * @author Nils Hartmann <nils@nilshartmann.net>
   * @version $Revision$
   */
  public static class VcsCommand extends EnumeratedAttribute {

    public VcsCommand() {
      // needed by Ant to instantiate
    }

    public VcsCommand( String value ) {
      super();
      setValue( value );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getValues() {
      return new String[] { CHECKOUT, UPDATE, EXPORT };
    }
  }
  
} /* ENDCLASS */
