/**
 * 
 */
package com.boilerplate.dbscript.interfaces;

/**
 * @author Vignesh
 *
 */
public abstract class DDLScriptGenerator {
	public abstract void generateDDLScripts(String sourceFilePath,String destinationFilePath);
}
