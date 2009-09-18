/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.jdt.ecj.internal.tools;

import org.ant4eclipse.core.logging.A4ELogging;

import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Implements the call-back interface {@link ICompilerRequestor} for receiving compilation results. The
 * {@link CompilerRequestorImpl} writes the compiled class files to disc or reports the errors in case the compilation
 * was not successful.
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class CompilerRequestorImpl implements ICompilerRequestor {

  /** indicates whether the compilation was successful or not */
  protected boolean                  _compilationSuccessful;

  /** the list of categorized problems */
  protected List<CategorizedProblem> _categorizedProblems;

  /**
   * <p>
   * Creates a new instance of type {@link CompilerRequestorImpl}.
   * </p>
   */
  public CompilerRequestorImpl() {
    this._compilationSuccessful = true;
    this._categorizedProblems = new LinkedList<CategorizedProblem>();
  }

  /**
   * {@inheritDoc}
   */
  public void acceptResult(CompilationResult result) {

    CompilationUnitImpl compilationUnitImpl = (CompilationUnitImpl) result.getCompilationUnit();
    File destinationDirectory = compilationUnitImpl.getSourceFile().getDestinationFolder();

    if (!result.hasErrors()) {
      ClassFile[] classFiles = result.getClassFiles();
      for (ClassFile classFile2 : classFiles) {
        char[][] compoundName = classFile2.getCompoundName();
        StringBuffer classFileName = new StringBuffer();
        for (int j = 0; j < compoundName.length; j++) {
          classFileName.append(compoundName[j]);
          if (j < compoundName.length - 1) {
            classFileName.append('/');
          }
        }
        try {
          classFileName.append(".class");
          File classFile = new File(destinationDirectory, classFileName.toString());
          File classDir = classFile.getParentFile();
          if (!classDir.exists()) {
            classDir.mkdirs();
          }
          A4ELogging.debug("writing class file: '%s'", classFile);
          if (!classFile.exists()) {
            classFile.createNewFile();
          }

          FileOutputStream fileOutputStream = new FileOutputStream(classFile);
          fileOutputStream.write(classFile2.getBytes());
          fileOutputStream.flush();
          fileOutputStream.close();
        } catch (IOException ioe) {
          A4ELogging.error("Could not write classfile '%s': %s", classFileName.toString(), ioe.toString());
          ioe.printStackTrace();
          this._compilationSuccessful = false;
        }
      }
    } else {
      this._compilationSuccessful = false;
    }

    if (result.getAllProblems() != null) {
      this._categorizedProblems.addAll(Arrays.asList(result.getAllProblems()));
    }
  }

  /**
   * @return
   */
  public boolean isCompilationSuccessful() {
    return this._compilationSuccessful;
  }

  /**
   * @return
   */
  public CategorizedProblem[] getCategorizedProblems() {
    return this._categorizedProblems.toArray(new CategorizedProblem[0]);
  }
}
