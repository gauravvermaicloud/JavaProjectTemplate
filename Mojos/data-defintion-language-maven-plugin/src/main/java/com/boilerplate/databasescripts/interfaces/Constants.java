/**
 * 
 */
package com.boilerplate.databasescripts.interfaces;

/**
 * This class contains the constants used by the maven plugin
 * @author shrivb
 *
 */
public class Constants {

	/**
	 * This naming pattern is used as part of the filename by the database script generator mojo. 
	 * This pattern is further used by the database deploy mojo to select which file to be used while provisioning the database. 
	 */
	 public static final String createWord = "_CREATE_";
	 
	 /**
	  * An enumeration for various database types
	  */
	 public enum DB {
			MYSQL, ORACLE, CASSANDRA, MONGODB
		}
}

