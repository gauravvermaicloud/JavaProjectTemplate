/**
 * 
 */
package com.boilerplate.databasescripts.implementations.MySQL;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.maven.plugin.logging.Log;

import com.boilerplate.databasescripts.interfaces.DBDeployer;
import com.boilerplate.databasescripts.interfaces.DBInstanceInfo;
import com.boilerplate.databasescripts.utilities.LogHelper;


/**
 * This class is a MySQL specific implementation of the {@link DBDeployer} class
 * This contains the methods which would deploy or provision a database, run scripts on it to create some boilerplate entities
 * and some seed data on the target MySQL database server 
 * @author shrivb
 *
 */
public class MySQLDBDeployer extends DBDeployer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.boilerplate.databasescripts.interfaces.DBDeployer#deployDB(com.boilerplate
	 * .dbscript.interfaces.DBInstanceInfo,String)
	 */
	/**
	 * This method provisions a MySQL database instance by running a pre-generated script file. 
	 * This uses a fork of iBatis ScriptRunner code to execute scripts against the MySQL Server  - https://github.com/BenoitDuffez/ScriptRunner
	 * Also @see {@link DBDeployer}
	 */
	@Override
	public boolean deployDB(DBInstanceInfo dbInstanceInfo,String scriptFile) throws Exception {
		Connection connection = null;		
		boolean isDeploySuccess = false;
		try {
		    Class.forName("com.mysql.jdbc.Driver");
		    connection = DriverManager.getConnection("jdbc:mysql://" + dbInstanceInfo.getDatabaseHost() + ":" + dbInstanceInfo.getDatabasePortNumber() , dbInstanceInfo.getAdminUserName(), dbInstanceInfo.getAdminUserPassword());
		} catch (ClassNotFoundException | SQLException e) {
		   if(e.getClass().equals(ClassNotFoundException.class))
			   System.err.println("Unable to get mysql driver: " + e);
		   if(e.getClass().equals(SQLException.class))
			   System.err.println("Error connecting to mysql server: " + e);
		    return isDeploySuccess;
		} 
		
		try(Reader bufferedScriptReader = new BufferedReader(new FileReader(scriptFile))) {
			ScriptRunner runner = new ScriptRunner(connection, false, true);			
			runner.runScript(bufferedScriptReader);	
			isDeploySuccess = true;
		} catch (Exception e) {
			System.err.println("Error occurred in trying to run the script against the database" + e.toString());
			e.printStackTrace();			
		}		
		return isDeploySuccess;

	}

}
