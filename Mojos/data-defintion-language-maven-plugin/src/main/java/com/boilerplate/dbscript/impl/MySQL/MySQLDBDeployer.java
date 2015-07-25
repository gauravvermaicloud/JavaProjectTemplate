/**
 * 
 */
package com.boilerplate.dbscript.impl.MySQL;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.maven.plugin.logging.Log;

import com.boilerplate.dbscript.interfaces.DBDeployer;
import com.boilerplate.dbscript.interfaces.DBInstanceInfo;

/**
 * @author Vignesh
 *
 */
public class MySQLDBDeployer extends DBDeployer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.boilerplate.dbscript.interfaces.DBCreator#CreateDB(com.boilerplate
	 * .dbscript.interfaces.DBInstanceInfo)
	 */
	@Override
	public boolean deployDB(DBInstanceInfo dbInstanceInfo,String scriptFile) throws Exception {

		if (!MySQLUtilities.Ping(dbInstanceInfo))
			throw new Exception(
					"MySQL server is not running. Or access is not given for the present user. Please check");
		
		Log mvnLogger = MySQLUtilities.getMvnLogger();
		
		//example C:\Users\Admin>mysql < "C:\Users\Admin\Desktop\test.sql" -uroot -padmin@123 -hlocalhost
		String deployDBCommand = "\"" + dbInstanceInfo.getBinDirectoryPath()
				+ "\\"
				+ MySQLUtilities.MySqlCommands
						.get(MySQLUtilities.MYSQLDBCOMMAND) + "\"  < "
				+ "\"" +  scriptFile
				+ "\"" + " -u" + dbInstanceInfo.getAdminUserName() + " -p" 	+ dbInstanceInfo.getAdminUserPassword() 
				+ " -h" + dbInstanceInfo.getDatabaseHost();

		// Assumption: logger is already initialized.
		if(mvnLogger!=null) 
			mvnLogger.info(
				"Deploy DB command being executed is:" + deployDBCommand);		
		
		//TODO:check on bash			
		String shell = "bash";
		if(MySQLUtilities.isWindows()) shell = "cmd";
		//credits: http://stackoverflow.com/questions/15464111/run-cmd-commands-through-java
		ProcessBuilder mySQLProcessBuilder = new ProcessBuilder(shell,"/c",deployDBCommand);		
		mySQLProcessBuilder.redirectErrorStream(true);
		
		Process mySQLProcess = mySQLProcessBuilder.start();
		BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(mySQLProcess.getInputStream()));
		String outputLine;
		while ((outputLine = processOutputReader.readLine()) != null)
			if(mvnLogger!=null) 
				mvnLogger.info("db deploy maven plug in output: " + outputLine);
			else				
				System.out.println("db deploy maven plug in output: " + outputLine);
		int createDBExitCode = mySQLProcess.waitFor();

		if(mvnLogger!=null) 
			mvnLogger.info(
				"Exit value for deploy database is:" + createDBExitCode);

		return createDBExitCode == 0 ? true : false;
		

	}

}
