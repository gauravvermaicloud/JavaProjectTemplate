/**
 * 
 */
package com.boilerplate.dbscript.interfaces;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.boilerplate.dbscript.impl.MySQL.MySQLUtilities.DB;

/**
 * @author Vignesh
 *
 */
@XmlRootElement(name="connection")
@XmlAccessorType(XmlAccessType.FIELD)
public class DBInstanceInfo {
	@XmlElement(name="DBBinDirectoryPath")
	private String binDirectoryPath;
	@XmlElement(name="DBHost")
	private String databaseHost;
	@XmlElement(name="adminUserName")
	private String adminUserName;
	@XmlElement(name="adminUserPassword")
	private String adminUserPassword;
	@XmlElement(name="DBType")
	private DB DBType;
	
	private String scriptsFolder;
	
	/**
	 * @return the binDirectoryPath
	 */
	public String getBinDirectoryPath() {
		return binDirectoryPath;
	}
	
	/**
	 * @param binDirectoryPath
	 * @param databaseHost
	 * @param adminUserName
	 * @param adminUserPassword
	 */
	public DBInstanceInfo(String binDirectoryPath, String databaseHost,
			String adminUserName, String adminUserPassword) {
		this.binDirectoryPath = binDirectoryPath;
		this.databaseHost = databaseHost;
		this.adminUserName = adminUserName;
		this.adminUserPassword = adminUserPassword;
	}

	public DBInstanceInfo() {
		// TODO Auto-generated constructor stub
	}
	

	/**
	 * @param binDirectoryPath the binDirectoryPath to set
	 */
	public void setBinDirectoryPath(String binDirectoryPath) {
		this.binDirectoryPath = binDirectoryPath;
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
