/**
 * 
 */
package com.boilerplate.dbscript.impl.MySQL;

import com.boilerplate.dbscript.interfaces.PostScriptGenerator;

/**
 * @author Vignesh
 * Creates script which would be run post creation of the database tables. 
 */
public class MySQLPostScriptGenerator extends PostScriptGenerator {

	/* (non-Javadoc)
	 * @see com.boilerplate.dbscript.interfaces.PostScriptGenerator#GeneratePostScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void generatePostScript(String sourceFilePath,
			String destinationFilePath) {
		
	}

}
