/**
 * 
 */
package com.boilerplate.dbscript.interfaces;

/**
 * @author Admin
 *
 */
public abstract class DBDeployer {
	
	
	public abstract boolean deployDB(DBInstanceInfo dbInstanceInfo,String scriptFile) throws Exception;

	

}
