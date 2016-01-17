package com.boilerplate.databasescripts.implementations.MySQL;

import com.boilerplate.databasescripts.interfaces.DBInstanceInfo;
import com.boilerplate.databasescripts.interfaces.DBScriptService;

/**
 * This class is a MySQL specific implementation of the service which would help 
 * in generation of all the boilerplate scripts which would be used to set up the boilerplate MySQL database
 * @author shrivb
 *
 */
public class MySQLScriptService extends DBScriptService {

	/**
	 * Constructor which constructs MySQL specific script generators. 
	 */
	public MySQLScriptService() {		
		preScriptGenerator = new MySQLPreScriptGenerator();
		postScriptGenerator = new MySQLPostScriptGenerator();
		ddlScriptGenerator = new MySQLDDLScriptGenerator();		
	}
	
	/* (non-Javadoc)
	 * @see com.boilerplate.databasescripts.interfaces.DBScriptService#generateScripts(java.lang.String, java.lang.String)
	 */
	@Override
	public void generateScripts(DBInstanceInfo dbInstanceInfo,
			String sourceXMLPath, String destinationFilePath) {
		//Create PreScript
		
		//Read each file corresponding to a table in the folder

		//Create/Append to the DDL file

		//Create/Append the Post script file



	}

}
