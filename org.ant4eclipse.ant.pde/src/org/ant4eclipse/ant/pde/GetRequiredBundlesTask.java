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
package org.ant4eclipse.ant.pde;

import org.ant4eclipse.ant.platform.core.GetPathComponent;
import org.ant4eclipse.ant.platform.core.delegate.GetPathDelegate;
import org.ant4eclipse.ant.platform.core.task.AbstractProjectPathTask;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.osgi.BundleLayoutResolver;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.pde.PdeExceptionCode;
import org.ant4eclipse.lib.pde.internal.tools.BundleDependenciesResolver;
import org.ant4eclipse.lib.pde.internal.tools.BundleDependenciesResolver.BundleDependency;
import org.ant4eclipse.lib.pde.internal.tools.TargetPlatformImpl;
import org.ant4eclipse.lib.pde.internal.tools.UnresolvedBundleException;
import org.ant4eclipse.lib.pde.model.pluginproject.BundleSource;
import org.apache.tools.ant.BuildException;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.osgi.framework.Version;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * The {@link GetRequiredBundlesTask} task can be used to resolve the required bundles for a given set of bundles.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GetRequiredBundlesTask extends AbstractProjectPathTask implements TargetPlatformAwareComponent, GetPathComponent {

  /** the target platform delegate */
  private TargetPlatformAwareDelegate    _targetPlatformAwareDelegate;

  /** */
  private GetPathComponent               _getPathComponent;

  // /** indicates if optional dependencies should be resolved */
  // private boolean _includeOptionalDependencies = true;

  // /** indicates if the specified bundles should be part of the result */
  // private boolean _includeSpecifiedBundles = true;

  /** indicates if workspace bundles should be part of the result */
  private boolean                        _includeWorkspaceBundles = true;

  /** indicates if the bundle class path should be resolved */
  private boolean                        _resolveBundleClasspath  = true;

  /** the bundle symbolic name */
  private String                         _bundleSymbolicName;

  /** the bundle version */
  private String                         _bundleVersion;

  /** the bundle specifications */
  private ArrayList<BundleSpecification> _bundleSpecifications;

  /** */
  private Set<BundleDescription>         _resolvedBundleDescriptions;

  /**
   * <p>
   * Creates a new instance of type GetRequiredBundles.
   * </p>
   */
  public GetRequiredBundlesTask() {

    // // create the delegates
    _getPathComponent = new GetPathDelegate( this );
    _targetPlatformAwareDelegate = new TargetPlatformAwareDelegate();

    _bundleSpecifications = new ArrayList<BundleSpecification>();
    _resolvedBundleDescriptions = new HashSet<BundleDescription>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String getTargetPlatformId() {
    return _targetPlatformAwareDelegate.getTargetPlatformId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isTargetPlatformIdSet() {
    return _targetPlatformAwareDelegate.isTargetPlatformIdSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void requireTargetPlatformIdSet() {
    _targetPlatformAwareDelegate.requireTargetPlatformIdSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setTargetPlatformId( String targetPlatformId ) {
    _targetPlatformAwareDelegate.setTargetPlatformId( targetPlatformId );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPlatformConfigurationId() {
    return _targetPlatformAwareDelegate.getPlatformConfigurationId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPlatformConfigurationIdSet() {
    return _targetPlatformAwareDelegate.isPlatformConfigurationIdSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPlatformConfigurationId( String platformConfigurationId ) {
    _targetPlatformAwareDelegate.setPlatformConfigurationId( platformConfigurationId );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPathId() {
    return _getPathComponent.getPathId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProperty() {
    return _getPathComponent.getProperty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<File> getResolvedPath() {
    return _getPathComponent.getResolvedPath();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPathIdSet() {
    return _getPathComponent.isPathIdSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPropertySet() {
    return _getPathComponent.isPropertySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRelative() {
    return _getPathComponent.isRelative();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void populatePathId() {
    _getPathComponent.populatePathId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void populateProperty() {
    _getPathComponent.populateProperty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requirePathIdOrPropertySet() {
    _getPathComponent.requirePathIdOrPropertySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPathId( String id ) {
    _getPathComponent.setPathId( id );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProperty( String property ) {
    _getPathComponent.setProperty( property );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRelative( boolean relative ) {
    _getPathComponent.setRelative( relative );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setResolvedPath( List<File> resolvedPath ) {
    _getPathComponent.setResolvedPath( resolvedPath );
  }

  /**
   * <p>
   * </p>
   * 
   * @return the includeWorkspaceBundles
   */
  public boolean isIncludeWorkspaceBundles() {
    return _includeWorkspaceBundles;
  }

  /**
   * <p>
   * </p>
   * 
   * @param includeWorkspaceBundles
   *          the includeWorkspaceBundles to set
   */
  public void setIncludeWorkspaceBundles( boolean includeWorkspaceBundles ) {
    _includeWorkspaceBundles = includeWorkspaceBundles;
  }

  /**
   * <p>
   * Returns the bundle symbolic name
   * </p>
   * 
   * @return the bundle symbolic name
   */
  public String getBundleSymbolicName() {
    return _bundleSymbolicName;
  }

  /**
   * <p>
   * Sets the bundle symbolic name
   * </p>
   * 
   * @param bundleSymbolicName
   *          the bundleSymbolicName to set
   */
  public void setBundleSymbolicName( String bundleSymbolicName ) {
    _bundleSymbolicName = bundleSymbolicName;
  }

  /**
   * <p>
   * Returns the bundle version
   * </p>
   * 
   * @return the bundleVersion
   */
  public String getBundleVersion() {
    return _bundleVersion;
  }

  /**
   * <p>
   * Sets the bundle version
   * </p>
   * 
   * @param bundleVersion
   *          the bundleVersion to set
   */
  public void setBundleVersion( String bundleVersion ) {
    _bundleVersion = bundleVersion;
  }

  /**
   * <p>
   * Adds the {@link BundleSpecification} to the list of root bundles.
   * </p>
   * 
   * @param specification
   *          the bundle specification
   */
  // Assure.notNull( "specification", specification );
  // assert symbolic name is set
  // if( Utilities.hasText( specification._symbolicName ) ) {
  //  throw new Ant4EclipseException( PdeExceptionCode.ANT_ATTRIBUTE_NOT_SET, "bundleSymbolicName" );
  // }
  public void addConfiguredBundle( BundleSpecification specification ) {
    try {
      specification.getVersion();
    } catch( Exception e ) {
      throw new Ant4EclipseException( PdeExceptionCode.INVALID_VERSION, "bundleSymbolicName", "bundle" );
    }
    // add specification
    _bundleSpecifications.add( specification );
  }

  /**
   * <p>
   * Creates a new {@link BundleSpecification} instance.
   * </p>
   * 
   * @return a new {@link BundleSpecification} instance.
   */
  public BundleSpecification createBundle() {
    return new BundleSpecification();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doExecute() {

    // step 1: clear the result list
    _resolvedBundleDescriptions.clear();

    // step 2: add the bundle specification attribute to the the list of
    // (root) bundle specifications
    if( Utilities.hasText( _bundleSymbolicName ) ) {
      _bundleSpecifications.add( new BundleSpecification( _bundleSymbolicName, _bundleVersion ) );
    }

    // step 3: resolve required bundles for all bundle specifications
    for( BundleSpecification bundleSpecification : _bundleSpecifications ) {

      // get the resolved bundle description from the target platform
      BundleDescription bundleDescription = _targetPlatformAwareDelegate.getTargetPlatform( getWorkspace() )
          .getResolvedBundle( bundleSpecification.getSymbolicName(), bundleSpecification.getVersion() );

      // if not resolved bundle description is found, throw an exception
      if( bundleDescription == null ) {
        throw new Ant4EclipseException( PdeExceptionCode.SPECIFIED_BUNDLE_NOT_FOUND,
            bundleSpecification.getSymbolicName(), bundleSpecification.getVersion() );
      }

      // resolve the required ones
      resolveReferencedBundles( bundleDescription );
    }

    // step 4: resolve the path
    List<File> result = new ArrayList<File>();

    for( BundleDescription bundleDescription : _resolvedBundleDescriptions ) {

      // don't add the bundle if bundle source is an eclipse project and
      // _includeWorkspaceBundles == false
      BundleSource bundleSource = (BundleSource) bundleDescription.getUserObject();
      if( _includeWorkspaceBundles || !(bundleSource.isEclipseProject()) ) {

        // get the layout resolver
        BundleLayoutResolver layoutResolver = BundleDependenciesResolver.getBundleLayoutResolver( bundleDescription );

        // add the files
        if( _resolveBundleClasspath ) {
          result.addAll( layoutResolver.resolveBundleClasspathEntries() );
        } else {
          result.add( layoutResolver.getLocation() );
        }
      }
    }

    // set the resolved path
    setResolvedPath( result );

    // set the path
    if( isPathIdSet() ) {
      populatePathId();
    }

    // set the property
    if( isPropertySet() ) {
      populateProperty();
    }
  }

  /**
   * <p>
   * Resolves the referenced bundles for the given bundle description.
   * </p>
   * 
   * @param bundleDescription
   *          the referenced bundles for the given bundle description.
   */
  private void resolveReferencedBundles( BundleDescription bundleDescription ) {

    // step 1: add the bundle description to the list of resolved descriptions or
    // return if the description already has been resolved
    // TODO: maybe we have to check if the bundle description has attached fragments (in case it is indirectly
    // referenced?)
    if( _resolvedBundleDescriptions.contains( bundleDescription ) /*
                                                                        * ||
                                                                        * _excludedBundles.contains(bundleDescription)
                                                                        */) {
      return;
    } else {
      _resolvedBundleDescriptions.add( bundleDescription );
    }

    // step 2: resolve bundle dependencies
    List<BundleDependency> bundleDependencies = null;

    try {
      bundleDependencies = new BundleDependenciesResolver().resolveBundleClasspath( bundleDescription );
    } catch( UnresolvedBundleException e ) {
      // throw a BUNDLE_NOT_RESOLVED_EXCEPTION
      throw new Ant4EclipseException( PdeExceptionCode.BUNDLE_NOT_RESOLVED_EXCEPTION,
          TargetPlatformImpl.dumpResolverErrors( bundleDescription, true ) );
    }

    // step 3: resolve the referenced bundles
    for( BundleDependency bundleDependency : bundleDependencies ) {

      // resolve the host
      resolveReferencedBundles( bundleDependency.getHost() );

      // resolve the fragments
      for( BundleDescription fragment : bundleDependency.getFragments() ) {
        resolveReferencedBundles( fragment );
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void preconditions() throws BuildException {

    // require fields
    requirePathIdOrPropertySet();
    requireTargetPlatformIdSet();

    // if attribute 'bundleSymbolicName' is set, no 'bundle' element is
    // allowed
    if( Utilities.hasText( _bundleSymbolicName ) && !_bundleSpecifications.isEmpty() ) {
      throw new Ant4EclipseException( PdeExceptionCode.ANT_ATTRIBUTE_X_OR_ELEMENT_Y, "bundleSymbolicName", "bundle" );
    }

    // if attribute 'bundleVersion' is set, 'bundleSymbolicName' must be
    // specified
    if( !Utilities.hasText( _bundleSymbolicName ) && Utilities.hasText( _bundleVersion ) ) {
      throw new Ant4EclipseException( PdeExceptionCode.ANT_ATTRIBUTE_X_WITHOUT_ATTRIBUTE_Y, "bundleVersion",
          "bundleSymbolicName" );
    }
  }

  /**
   * <p>
   * Encapsulates a bundle specification.
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class BundleSpecification {

    /** the symbolicName */
    private String _symbolicName;

    /** the version */
    private String _version;

    /**
     * <p>
     * Creates a new instance of type {@link BundleSpecification}.
     * </p>
     */
    public BundleSpecification() {
      // nothing to do here...
    }

    /**
     * <p>
     * Creates a new instance of type {@link BundleSpecification}.
     * </p>
     * 
     * @param symbolicName
     *          the symbolic name
     * @param version
     *          the version
     */
    public BundleSpecification( String symbolicName, String version ) {
      _symbolicName = symbolicName;
      _version = version;
    }

    /**
     * <p>
     * Returns the symbolic name.
     * </p>
     * 
     * @return the symbolicName
     */
    public String getSymbolicName() {
      return _symbolicName;
    }

    /**
     * <p>
     * Sets the symbolic name.
     * </p>
     * 
     * @param symbolicName
     *          the symbolicName to set
     */
    public void setSymbolicName( String symbolicName ) {
      _symbolicName = symbolicName;
    }

    /**
     * <p>
     * Returns the bundle version.
     * </p>
     * 
     * @return the version
     */
    public Version getVersion() {
      return _version != null ? new Version( _version ) : null;
    }

    /**
     * <p>
     * Sets the bundle version.
     * </p>
     * 
     * @param version
     *          the version to set
     */
    public void setVersion( String version ) {
      _version = version;
    }

    /**
     * <p>
     * Returns <code>true</code> if the bundle version is set.
     * </p>
     * 
     * @return <code>true</code> if the bundle version is set, <code>false</code> otherwise.
     */
    public boolean hasBundleVersion() {
      return _version != null;
    }
  }
  
} /* ENDCLASS */
