/**
 * 
 */
package com.boilerplate.databasescripts.utilities;


import com.boilerplate.databasescripts.interfaces.DBInstanceInfo;
import org.apache.maven.plugin.logging.Log;
/**
 * This class helps to serve as a centralized logging sequence repository instead of having repetitive logger sequences across the code. 
 * @author shrivb
 *
 */
public class LogHelper {
	
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
	public static void initializeMvnLogger(Log _mavenLogger) {		
		if(mvnLogger == null)
			mvnLogger = _mavenLogger;		
	}

	/**
	 * Logs the database instance specific information through the maven logger
	 * @param databaseInstanceInfo - the object which encapsulates all the information about a database instance
	 * @param log the default maven logger
	 */
	public static void LogDatabaseInstanceInfo(DBInstanceInfo databaseInstanceInfo, Log log)
	{
		log.info("DatabaseHost:" + databaseInstanceInfo.getDatabaseHost());
		log.info("Port:" + databaseInstanceInfo.getDatabasePortNumber());
		log.info("DBType:" + databaseInstanceInfo.getDBType());
		log.info("AdminUserName:" + databaseInstanceInfo.getAdminUserName());
		log.info("AdminUserPassword:" + databaseInstanceInfo.getAdminUserPassword());
		log.info("ScriptsFolder:" + databaseInstanceInfo.getScriptsFolder());
	}
	
}
