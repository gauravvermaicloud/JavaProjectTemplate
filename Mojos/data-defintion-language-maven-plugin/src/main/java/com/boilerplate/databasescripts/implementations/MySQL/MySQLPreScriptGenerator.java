package com.boilerplate.databasescripts.implementations.MySQL;

import com.boilerplate.databasescripts.interfaces.PreScriptGenerator;

/**
 * This class is a MySQL specific implementation of the {@link PreScriptGenerator} class
 * This contains the methods which would pick up a pre script configuration and generate MySQL specific 
 * statements which could be run to set up or bootstrap a MySQL database.   
 * @author shrivb
 *
 */
public class MySQLPreScriptGenerator extends PreScriptGenerator {

	/* (non-Javadoc)
	 * @see com.boilerplate.databasescripts.interfaces.PreScriptGenerator#generatePreScripts(java.lang.String, java.lang.String)
	 */
	@Override
	public void generatePreScripts(String sourceFilePath,
			String destinationFilePath) {		

	}

}
