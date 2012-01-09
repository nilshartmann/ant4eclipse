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
package org.ant4eclipse.lib.core.xquery;

import org.ant4eclipse.lib.core.CoreExceptionCode;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.SAXParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Vector;

/**
 * <p>
 * A SAX handler allowing to collect XML related data using simple queries (similar to XPath but currently not providing
 * it's complete functionality).
 * </p>
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class XQueryHandler extends DefaultHandler {

  private StringBuffer            _buffer;

  private int                     _depth;

  private Vector<XQuery>          _queries;

  private String                  _fileName;

  private static SAXParserFactory factory;

  /**
   * Initialises this handler.
   * 
   * @param fileName
   *          The name of the file that has to be parsed.
   */
  public XQueryHandler( String fileName ) {
    super();
    _buffer = new StringBuffer();
    _queries = new Vector<XQuery>();
    _depth = 0;
    _fileName = fileName;
  }

  /**
   * Initialises this handler.
   */
  public XQueryHandler() {
    this( "unknown" );
  }

  /**
   * Creates a XQuery instance for the supplied query string.
   * 
   * @param query
   *          A XPath like query string.
   * 
   * @return A query instance allowing to retrieve the results.
   */
  public XQuery createQuery( String query ) {
    XQuery result = new XQuery( _fileName, query );
    _queries.add( result );
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void startDocument() throws SAXException {
    _depth = 0;
    for( int i = 0; i < _queries.size(); i++ ) {
      XQuery query = _queries.get( i );
      query.reset();
    }
    if( _buffer.length() > 0 ) {
      _buffer.delete( 0, _buffer.length() );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void startElement( String uri, String localname, String qname, Attributes attributes ) throws SAXException {

    for( int i = 0; i < _queries.size(); i++ ) {
      XQuery query = _queries.get( i );
      query.visit( _depth, qname, attributes );
    }

    _depth++;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void endElement( String uri, String localname, String qname ) throws SAXException {

    _depth--;

    String str = _buffer.toString().trim();
    for( int i = 0; i < _queries.size(); i++ ) {
      XQuery query = _queries.get( i );
      query.endVisit( _depth, str );
    }

    if( _buffer.length() > 0 ) {
      _buffer.delete( 0, _buffer.length() );
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void characters( char[] ch, int start, int length ) throws SAXException {
    _buffer.append( ch, start, length );
  }

  /**
   * Runs the queries against the supplied XML file.
   * 
   * @param xmlfile
   *          The XML file that shall be queried.
   * @param handler
   *          The handler which provides all queries.
   */
  // Assure.isFile( "xmlfile", xmlfile );
  // Assure.notNull( "handler", handler );
  public static void queryFile( File xmlfile, XQueryHandler handler ) {
    try {
      SAXParserFactory factory = getSAXParserFactory();
      factory.newSAXParser().parse( new FileInputStream( xmlfile ), handler );
    } catch( Exception ex ) {
      A4ELogging.error( ex.getMessage() );
      throw(new Ant4EclipseException( ex, CoreExceptionCode.X_QUERY_PARSE_EXCEPTION ));
    }
  }

  /**
   * Runs the queries against the supplied XML inputstream.
   * 
   * @param inputStream
   *          The XML inputstream that shall be queried.
   * @param handler
   *          The handler which provides all queries.
   */
  // Assure.notNull( "inputStream", inputStream );
  // Assure.notNull( "handler", handler );
  public static void queryInputStream( InputStream inputStream, XQueryHandler handler ) {
    try {
      SAXParserFactory factory = getSAXParserFactory();
      factory.newSAXParser().parse( inputStream, handler );
    } catch( Exception ex ) {
      A4ELogging.error( ex.getMessage() );
      throw(new Ant4EclipseException( ex, CoreExceptionCode.X_QUERY_PARSE_EXCEPTION ));
    }
  }

  private static SAXParserFactory getSAXParserFactory() throws FactoryConfigurationError {
    if( factory == null ) {
      factory = SAXParserFactory.newInstance();
      // factory.setFeature("http://xml.org/sax/features/string-interning", true);
      factory.setValidating( false );
    }
    return factory;
  }

} /* ENDCLASS */
