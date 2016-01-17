package com.boilerplate.databasescripts.interfaces;

/**
 * This class is an abstraction for a service implementation which would cater to individual database specific data-definition script generation methodologies.
 * 
 * @author shrivb
 */
public abstract class DBScriptService {	
	
	/**
	 * This is the handle to the implementation-specific pre scripts generator @see PreScriptGenerator
	 */
	protected PreScriptGenerator preScriptGenerator;
	
	/**
	 * This is the handle to the implementation-specific ddl scripts generator @see DDLScriptGenerator
	 */	
	protected DDLScriptGenerator ddlScriptGenerator;

	/**
	 * This is the handle to the implementation-specific post scripts generator @see PostScriptGenerator
	 */
	protected PostScriptGenerator postScriptGenerator;	
	
	
	/**
	 * This method generates all the scripts needed for setting up the boilerplate database and tables.
	 * This reads the various configurations for tables, data, security, etc and after execution of this method,
	 *  all the prescripts, postscripts and the intermediate data definition scripts would be generated and combined as one file
	 * @param dbInstanceInfo  an object which encapsulates all the database specific info needed by this method in creation of scripts
	 * @param sourceXMLPath the root path from which the various configurations are read from
	 * @param destinationFilePath the destination path into which the generated script files would be written into
	 */
	public abstract void generateScripts(DBInstanceInfo dbInstanceInfo,String sourceXMLPath, String destinationFilePath);	
	
}
