package com.boilerplate.databasescripts.interfaces;

/**  
 * This class is an abstraction for the concrete database specific implementation which would generate various Data Defintion Language statements
 * to be run against the specific database to set up boilerplate database entities. 
 * This class would help generate mainly table creation scripts
 * @author shrivb
 */
public abstract class DDLScriptGenerator {
	/**
	 * This method generates DDL statements for a given database entity configuration
	 * @param sourceFilePath the file path of the database entity configuration which would be represented by the DDL statements
	 * @param destinationFilePath the path where the generated DDL file would reside. If a DDL file already exists, content would be appended, else created.
	 */
	public abstract void generateDDLScripts(String sourceFilePath,String destinationFilePath);
}
