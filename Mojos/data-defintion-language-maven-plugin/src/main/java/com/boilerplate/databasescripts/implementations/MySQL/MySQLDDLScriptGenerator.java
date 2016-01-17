/**
 * 
 */
package com.boilerplate.databasescripts.implementations.MySQL;

import com.boilerplate.databasescripts.interfaces.DDLScriptGenerator;

/**
 * This class is a MySQL specific implementation of the {@link DDLScriptGenerator} class
 * This contains the methods which would pick up a configuration representing MySQL entity and generate MySQL specific 
 * DDL statements which could be run against the MySQL database.  
 * @author shrivb
 *
 */
public class MySQLDDLScriptGenerator extends DDLScriptGenerator {

	/* (non-Javadoc)
	 * @see com.boilerplate.databasescripts.interfaces.DDLScriptGenerator#generateDDLScripts(java.lang.String, java.lang.String)
	 */
	@Override
	public void generateDDLScripts(String sourceFilePath,
			String destinationFilePath) {
		

	}

}
