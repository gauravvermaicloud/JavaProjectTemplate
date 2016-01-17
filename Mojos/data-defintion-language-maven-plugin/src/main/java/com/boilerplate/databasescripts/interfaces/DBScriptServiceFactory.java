/**
 * 
 */
package com.boilerplate.databasescripts.interfaces;

/**
 * This class is an abstraction which would enable a database-specific factory implementation to create a set of related objects pertaining to a database
 * 
 * @author shrivb
 */
public abstract class DBScriptServiceFactory {
	/**
	 * This method creates and returns an instance of the implementation specific database script deployer. 
	 * @return a concrete instance of {@link DBDeployer}
	 */
	public abstract DBDeployer createDBDeployer();
	
	/**
	 * This method creates and returns an instance of a database specific service which used for individual database-specific operations
	 * @return a concrete instance of {@link DBScriptService}
	 */
	public abstract DBScriptService createDBScriptService();
	
}
