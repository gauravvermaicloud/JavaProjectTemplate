/**
 * 
 */
package com.boilerplate.databasescripts.implementations.MySQL;

import com.boilerplate.databasescripts.interfaces.PostScriptGenerator;

/**
 * This class is a MySQL specific implementation of the {@link PostScriptGenerator} class
 * This contains the methods which would pick up a post script configuration and generate MySQL specific 
 * statements which could be run against the MySQL database after the basic database set up has been done.  
 * @author shrivb
 *
 */
public class MySQLPostScriptGenerator extends PostScriptGenerator {

	/* (non-Javadoc)
	 * @see com.boilerplate.databasescripts.interfaces.PostScriptGenerator#generatePostScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void generatePostScript(String sourceFilePath,
			String destinationFilePath) {
		
	}

}
