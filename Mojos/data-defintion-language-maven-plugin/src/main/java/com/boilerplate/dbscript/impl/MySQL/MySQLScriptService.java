/**
 * 
 */
package com.boilerplate.dbscript.impl.MySQL;

import com.boilerplate.dbscript.interfaces.DBInstanceInfo;
import com.boilerplate.dbscript.interfaces.DBScriptService;

/**
 * @author Vignesh
 *
 */
public class MySQLScriptService extends DBScriptService {

	/* (non-Javadoc)
	 * @see com.boilerplate.dbscript.interfaces.DBScriptService#GenerateScripts(java.lang.String, java.lang.String)
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
