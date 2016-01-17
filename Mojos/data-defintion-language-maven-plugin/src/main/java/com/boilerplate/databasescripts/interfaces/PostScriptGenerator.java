/**
 * 
 */
package com.boilerplate.databasescripts.interfaces;

/**  
 * This class is an abstraction for the concrete database specific implementation which would generate various database statements
 * to be run against the specific database after the data definition statements have been run against it.
 * This class would generate scripts which might contain some seed boilerplate data.   
 * @author shrivb
 *
 */
public abstract class PostScriptGenerator {
	/**
	 * This method generates the scripts which would be run after the database along with its basic entities like tables, users, etc have been set up
	 * @param sourceFilePath the path to the configuration file based on which the postscript statements would be generated 
	 * @param destinationFilePath the path where the generated post script file would reside. If a post script file already exists, content would be appended, else created.
	 */
	public abstract void generatePostScript(String sourceFilePath,String destinationFilePath);
}
