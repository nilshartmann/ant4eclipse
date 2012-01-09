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
package org.ant4eclipse.lib.pde.model.featureproject;

import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.pde.PdeExceptionCode;
import org.ant4eclipse.lib.pde.internal.model.featureproject.FeatureProjectRoleImpl;
import org.ant4eclipse.lib.pde.model.buildproperties.BuildPropertiesParser;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.role.AbstractProjectRoleIdentifier;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * <p>
 * Identifier for the feature project role.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class FeatureProjectRoleIdentifier extends AbstractProjectRoleIdentifier {

  public FeatureProjectRoleIdentifier() {
    super( FeatureProjectRole.FEATURE_NATURE, "pde" );
  }

  /**
   * {@inheritDoc}
   */
  // Assure.notNull( "project", project );
  @Override
  public ProjectRole createRole( EclipseProject project ) {
    A4ELogging.debug( "FeatureProjectRoleIdentifier.applyRole(%s)", project );

    FeatureProjectRoleImpl featureProjectRole = new FeatureProjectRoleImpl( project );
    File featureDescription = featureProjectRole.getFeatureXml();

    try {
      FeatureManifest feature = FeatureManifestParser.parseFeature( new FileInputStream( featureDescription ) );
      featureProjectRole.setFeature( feature );
    } catch( FileNotFoundException e ) {
      throw new Ant4EclipseException( PdeExceptionCode.FEATURE_MANIFEST_FILE_NOT_FOUND, project.getFolder()
          .getAbsolutePath() );
    }

    // parse build properties
    if( project.hasChild( BuildPropertiesParser.BUILD_PROPERTIES ) ) {
      BuildPropertiesParser.parseFeatureBuildProperties( featureProjectRole );
    }

    return featureProjectRole;
  }

} /* ENDCLASS */
