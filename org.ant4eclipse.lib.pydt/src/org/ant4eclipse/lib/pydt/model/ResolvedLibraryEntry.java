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
package org.ant4eclipse.lib.pydt.model;


/**
 * Resolved library record.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class ResolvedLibraryEntry implements ResolvedPathEntry {

  // the location is a position with filesystem which points to a project relative library when
  // it's a relative path. otherwise it's pointing to an absolute path.
  private String _location;

  private String _owningproject;

  /**
   * Sets up this entry with resolved location of a python library.
   * 
   * @param owningproject
   *          The name of the related eclipse project. Neither <code>null</code> nor empty.
   * @param location
   *          The location of the library. Neither <code>null</code> nor empty.
   */
  // Assure.nonEmpty( "location", location );
  // Assure.nonEmpty( "owningproject", owningproject );
  public ResolvedLibraryEntry( String owningproject, String location ) {
    _owningproject = owningproject;
    _location = location;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getOwningProjectname() {
    return _owningproject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ReferenceKind getKind() {
    return ReferenceKind.Library;
  }

  /**
   * Returns the location of the library.
   * 
   * @return The location of the library. Neither <code>null</code> nor empty.
   */
  public String getLocation() {
    return _location;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object object ) {
    if( this == object ) {
      return true;
    }
    if( object == null ) {
      return false;
    }
    if( object.getClass() != getClass() ) {
      return false;
    }
    ResolvedLibraryEntry other = (ResolvedLibraryEntry) object;
    if( !_owningproject.equals( other._owningproject ) ) {
      return false;
    }
    return _location.equals( other._location );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int result = _owningproject.hashCode();
    result = 31 * result + _location.hashCode();
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format( "[ResolvedLibraryEntry: _owningproject: %s, _location: %s]", _owningproject, _location );
  }

} /* ENDCLASS */
