/**
 * 
 */
package com.boilerplate.databasescripts.interfaces;

/**  
 * This class is an abstraction for the concrete database specific implementation which would generate various database statements
 * to be run against the specific database before the data definition statements have been run against it.
 * This class would generate scripts which would create the database, set up security privileges, users, etc   
 * @author shrivb
 *
 */
public abstract class PreScriptGenerator {
	/**
	 * This method generates the scripts which would be run to set up the database, basic entities like users, security, etc
	 * @param sourceFilePath the path to the configuration file based on which the prescript statements would be generated 
	 * @param destinationFilePath the path where the generated prescript file would reside. If a prescript file already exists, content would be appended, else created.
	 */
	public abstract void generatePreScripts(String sourceFilePath,String destinationFilePath);
}
