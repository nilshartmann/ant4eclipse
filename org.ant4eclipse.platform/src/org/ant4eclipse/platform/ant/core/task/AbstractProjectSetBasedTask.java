package org.ant4eclipse.platform.ant.core.task;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseTask;

import org.ant4eclipse.platform.ant.core.WorkspaceProjectSetComponent;
import org.ant4eclipse.platform.ant.core.delegate.WorkspaceProjectSetDelegate;
import org.ant4eclipse.platform.model.resource.Workspace;
import org.ant4eclipse.platform.model.team.projectset.TeamProjectSet;

import java.io.File;

/**
 * <p>
 * Abstract base class for project set based tasks.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractProjectSetBasedTask extends AbstractAnt4EclipseTask implements
    WorkspaceProjectSetComponent {

  /** the workspace project set delegate */
  private final WorkspaceProjectSetDelegate _workspaceProjectSetDelegate;

  /**
   * <p>
   * Create a new instance of type {@link AbstractProjectSetBasedTask}.
   * </p>
   */
  public AbstractProjectSetBasedTask() {

    // create the delegates
    this._workspaceProjectSetDelegate = new WorkspaceProjectSetDelegate(this);
  }

  /**
   * {@inheritDoc}
   */
  public String[] getProjectNames() {
    return this._workspaceProjectSetDelegate.getProjectNames();
  }

  /**
   * {@inheritDoc}
   */
  public TeamProjectSet getTeamProjectSet() {
    return this._workspaceProjectSetDelegate.getTeamProjectSet();
  }

  /**
   * {@inheritDoc}
   */
  public Workspace getWorkspace() {
    return this._workspaceProjectSetDelegate.getWorkspace();
  }

  /**
   * {@inheritDoc}
   */
  public File getWorkspaceDirectory() {
    return this._workspaceProjectSetDelegate.getWorkspaceDirectory();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isAllProjects() {
    return this._workspaceProjectSetDelegate.isAllProjects();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isProjectNamesSet() {
    return this._workspaceProjectSetDelegate.isProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isTeamProjectSetSet() {
    return this._workspaceProjectSetDelegate.isTeamProjectSetSet();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isWorkspaceDirectorySet() {
    return this._workspaceProjectSetDelegate.isWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  public void requireAllProjectsOrProjectSetOrProjectNamesSet() {
    this._workspaceProjectSetDelegate.requireAllProjectsOrProjectSetOrProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  public void requireProjectNamesSet() {
    this._workspaceProjectSetDelegate.requireProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  public void requireTeamProjectSetOrProjectNamesSet() {
    this._workspaceProjectSetDelegate.requireTeamProjectSetOrProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  public void requireTeamProjectSetSet() {
    this._workspaceProjectSetDelegate.requireTeamProjectSetSet();
  }

  /**
   * {@inheritDoc}
   */
  public void requireWorkspaceDirectorySet() {
    this._workspaceProjectSetDelegate.requireWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  public void setAllProjects(boolean allprojects) {
    this._workspaceProjectSetDelegate.setAllProjects(allprojects);
  }

  /**
   * {@inheritDoc}
   */
  public void setProjectNames(String projectNames) {
    this._workspaceProjectSetDelegate.setProjectNames(projectNames);
  }

  /**
   * {@inheritDoc}
   */
  public void setTeamProjectSet(File projectSetFile) {
    this._workspaceProjectSetDelegate.setTeamProjectSet(projectSetFile);
  }

  /**
   * {@inheritDoc}
   */
  @Deprecated
  public void setWorkspace(File workspace) {
    this._workspaceProjectSetDelegate.setWorkspace(workspace);
  }

  /**
   * {@inheritDoc}
   */
  public void setWorkspaceDirectory(File workspaceDirectory) {
    this._workspaceProjectSetDelegate.setWorkspaceDirectory(workspaceDirectory);
  }

}
