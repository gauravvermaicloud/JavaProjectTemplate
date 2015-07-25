/**
 * 
 */
package com.boilerplate.dbscript.impl.MySQL;

import com.boilerplate.dbscript.interfaces.DBDeployer;
import com.boilerplate.dbscript.interfaces.DBScriptService;
import com.boilerplate.dbscript.interfaces.DBScriptServiceFactory;

/**
 * @author Vignesh
 *
 */
public class MySQLScriptServiceFactory extends DBScriptServiceFactory {

	/* (non-Javadoc)
	 * @see com.boilerplate.dbscript.interfaces.DBScriptServiceFactory#CreateDBScriptService()
	 */
	@Override
	public DBScriptService createDBScriptService() {
		
		return new MySQLScriptService();
	}

	@Override
	public DBDeployer createDBDeployer() {
		
		return new MySQLDBDeployer();
	}

}
