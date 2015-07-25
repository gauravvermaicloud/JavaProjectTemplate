/**
 * 
 */
package com.boilerplate.dbscript.impl.MySQL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.plugin.logging.Log;

import com.boilerplate.dbscript.interfaces.DBInstanceInfo;

/**
 * @author Vignesh
 *
 */
public class MySQLUtilities {

	public static final String MYSQLADMINCOMMAND = "MYSQLADMINCOMMAND";
	public static final String MYSQLDBCOMMAND = "MYSQLDBCOMMAND";

	public static final Map<String, String> MySqlCommands;
	static {
		MySqlCommands = new HashMap<String, String>();
		MySqlCommands.put(MYSQLADMINCOMMAND, "mysqladmin");
		MySqlCommands.put(MYSQLDBCOMMAND, "mysql");
	}
	
	private static Log mvnLogger;
	/**
	 * @return the mvnLogger
	 */
	public static Log getMvnLogger() {
		return mvnLogger;
	}

	/**
	 * @param mvnLogger the mvnLogger to set
	 */
	public static void initializeMvnLogger(Log _mvnLogger) {		
		if(mvnLogger == null)
			mvnLogger = _mvnLogger;		
	}
	

	public static boolean Ping(DBInstanceInfo dbInstanceInfo) throws IOException, InterruptedException {
		boolean isAlive = true;
		// Check if mysql is running
		String mySqlAliveStatusMessage = "mysqld is alive";

		String pingCmd = dbInstanceInfo.getBinDirectoryPath()
				+ "\\"
				+ MySQLUtilities.MySqlCommands
						.get(MySQLUtilities.MYSQLADMINCOMMAND) + " ping "
				+ " -u" + dbInstanceInfo.getAdminUserName() + " -p"
				+ dbInstanceInfo.getAdminUserPassword();
		Process mySQLAdminProcess = Runtime.getRuntime().exec(pingCmd);
		if(mvnLogger!=null) mvnLogger.info("Command being executed is" + pingCmd);
		int pingExitCode = mySQLAdminProcess.waitFor();
		if(mvnLogger!=null) mvnLogger.info("Exit value for ping of sql server is:" + pingExitCode);

		StringBuffer output = new StringBuffer();
		String line = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				mySQLAdminProcess.getInputStream()));
		while ((line = reader.readLine()) != null) {
			output.append(line);
		}
		if(mvnLogger!=null) mvnLogger.info("Ping response:" + output.toString());
		if (pingExitCode != 0
				&& !output.toString().equalsIgnoreCase(mySqlAliveStatusMessage))
			isAlive = false;
		
		return isAlive;

	}
	//credits: http://stackoverflow.com/questions/228477/how-do-i-programmatically-determine-operating-system-in-java
	public static boolean isWindows()
	{
		return System.getProperty("os.name").startsWith("Windows");		
	}
	
	public enum DB {
		MYSQL, ORACLE, CASSANDRA, MONGODB

	}
}


