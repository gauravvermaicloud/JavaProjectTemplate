/**
 * 
 */
package com.boilerplate.databasescripts.interfaces;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.boilerplate.databasescripts.interfaces.Constants.DB;



/**
 * This class represents an xml which specifies the details regarding a database. 
 * Sample xml 
 * <pre>{@code
 * <connection>
 *	    <DBType>MYSQL</DBType>                       
 *      <DatabaseHost>localhost</DatabaseHost> 
 *      <Port>3306</Port> 
 *      <AdminUserName>root</AdminUserName> 
 *      <AdminUserPassword>root</AdminUserPassword>       
 *    </connection>  
 * }</pre>
 * @author shrivb 
 */
@XmlRootElement(name="connection")
@XmlAccessorType(XmlAccessType.FIELD)
public class DBInstanceInfo {	
	@XmlElement(name="DatabaseHost")
	private String databaseHost;
	@XmlElement(name="Port")
	private int databasePortNumber;
	@XmlElement(name="AdminUserName")
	private String adminUserName;
	@XmlElement(name="AdminUserPassword")
	private String adminUserPassword;
	@XmlElement(name="DBType")
	private DB DBType;
	
	private String scriptsFolder;
	
	/** 
	 * @param databaseHost - the host name of the server running the database. 
	 * @param databasePortNumber - the port number used by the server for the current database instance
	 * @param adminUserName - the user name of a user with admin privileges
	 * @param adminUserPassword - password of the user specified in adminUserName
	 */
	public DBInstanceInfo(String databaseHost, int databasePortNumber,
			String adminUserName, String adminUserPassword) {		
		this.databaseHost = databaseHost;
		this.databasePortNumber = databasePortNumber;
		this.adminUserName = adminUserName;
		this.adminUserPassword = adminUserPassword;
	}

	//need to have this public constructor for xml serialization
	public DBInstanceInfo() {
		
	}


	/**
	 * @return the databaseName
	 */
	public String getDatabaseHost() {
		return databaseHost;
	}
	/**
	 * @param databaseHost the databaseName to set
	 */
	public void setDatabaseHost(String databaseHost) {
		this.databaseHost = databaseHost;
	}
	
	/**
	 * @return the port number used on the host by the Database server
	 */
	public int getDatabasePortNumber() {
		return databasePortNumber;
	}

	/**
	 * set the port number used on the host by the Database server
	 * @param databasePortNumber the port number to set
	 */
	public void setDatabasePortNumber(int databasePortNumber) {
		this.databasePortNumber = databasePortNumber;
	}
	
	/**
	 * @return the adminUserName
	 */
	public String getAdminUserName() {
		return adminUserName;
	}
	/**
	 * @param adminUserName the adminUserName to set
	 */
	public void setAdminUserName(String adminUserName) {
		this.adminUserName = adminUserName;
	}
	/**
	 * @return the adminUserPassword
	 */
	public String getAdminUserPassword() {
		return adminUserPassword;
	}
	/**
	 * @param adminUserPassword the adminUserPassword to set
	 */
	public void setAdminUserPassword(String adminUserPassword) {
		this.adminUserPassword = adminUserPassword;
	}

	/**
	 * @return the dBType
	 */
	public DB getDBType() {
		return DBType;
	}

	/**
	 * @param dBType the dBType to set
	 */
	public void setDBType(DB dBType) {
		DBType = dBType;
	}

	/**
	 * @return the scriptsFolder
	 */
	public String getScriptsFolder() {
		return scriptsFolder;
	}

	/**
	 * @param scriptsFolder the scriptsFolder to set
	 */
	public void setScriptsFolder(String scriptsFolder) {
		this.scriptsFolder = scriptsFolder;
	}
	
	
	
}
