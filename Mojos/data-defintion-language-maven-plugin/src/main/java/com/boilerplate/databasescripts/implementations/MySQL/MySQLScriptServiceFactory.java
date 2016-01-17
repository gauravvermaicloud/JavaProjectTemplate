/**
 * 
 */
package com.boilerplate.databasescripts.implementations.MySQL;

import com.boilerplate.databasescripts.interfaces.DBDeployer;
import com.boilerplate.databasescripts.interfaces.DBScriptService;
import com.boilerplate.databasescripts.interfaces.DBScriptServiceFactory;

/**
 * This class is a MySQL specific implementation of {@link DBScriptServiceFactory} and it creates instances of classes which perform MySQL specific operations. 
 * @author shrivb
 */
public class MySQLScriptServiceFactory extends DBScriptServiceFactory {


	/**
	 * This method creates and returns an instance of MySQL specific service which used for MySQL specific script generation operations
	 * @return a concrete MySQL specific instance of {@link DBScriptService}
	 */	
	@Override
	public DBScriptService createDBScriptService() {
		
		return new MySQLScriptService();
	}
	
	
	/**
	 * This method creates and returns a MySQL specific database deployer 
	 * @return a concrete MySQL specific instance of {@link DBDeployer}
	 */	
	@Override
	public DBDeployer createDBDeployer() {
		
		return new MySQLDBDeployer();
	}

}
