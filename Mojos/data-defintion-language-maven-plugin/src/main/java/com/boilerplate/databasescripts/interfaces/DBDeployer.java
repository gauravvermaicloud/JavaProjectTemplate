package com.boilerplate.databasescripts.interfaces;

/**
 * This class is an abstraction for an implementation which would cater to individual database specific deployments. 
 * @author shrivb
 *
 */
public abstract class DBDeployer {
	
	/**
	 * This method essentially executes a script file against a target database server
	 * @param dbInstanceInfo an object which encapsulates all the database specific info needed by this method to deploy the script
	 * @param scriptFile path of the script file which would be run against the target database server
	 * @return a boolean indicating success or failure of the the deployment
	 * @throws Exception
	 */
	//Usage of abstract class based on this - http://www.dofactory.com/net/abstract-factory-design-pattern. Could well been an interface based one as well. 
	public abstract boolean deployDB(DBInstanceInfo dbInstanceInfo,String scriptFile) throws Exception;

	

}
