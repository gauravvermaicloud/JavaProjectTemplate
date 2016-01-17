package com.boilerplate.test.MySQL;



import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.boilerplate.databasescripts.implementations.MySQL.MySQLScriptServiceFactory;
import com.boilerplate.databasescripts.interfaces.DBDeployer;
import com.boilerplate.databasescripts.interfaces.DBInstanceInfo;
import com.boilerplate.databasescripts.interfaces.DBScriptService;
import com.boilerplate.databasescripts.interfaces.DBScriptServiceFactory;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 * This class specifically tests the database deployment functionality which is used by the 
 * deployDB goal of the maven plugin
 * @author shrivb
 *
 */
public class MySQLDBDeployTest {

	DBScriptServiceFactory mySQLFactory;
	DBScriptService mySQLScriptService;
	DBDeployer mySQLDBDeployer;
	DBInstanceInfo dbInfo;

	
	private String scriptFilePath = "";
	private String scriptFileGuid = "";
	private String dbName = "TestingDB";	
	private int dbServerPort = 3306;
	private String MySQLServerUrl = "jdbc:mysql://%s:%s";
	private String sqlStmts = "DROP Database If exists `%s`;" + System.lineSeparator() 
			+ "CREATE DATABASE  IF NOT EXISTS %s ;" +  System.lineSeparator() 
			+ "USE %s;" + System.lineSeparator()
			+ "DROP TABLE IF EXISTS `Configurations`;" + System.lineSeparator()
			+ "CREATE TABLE `Configurations` ( " + " `Id` bigint(10) NOT NULL,"
			+ "`ConfigurationKey` varchar(64) NOT NULL,"
			+ "`ConfigurationValue` varchar(512) DEFAULT NULL,"
			+ "`Version` varchar(64) NOT NULL,"
			+ "`Enviornment` varchar(64) NOT NULL,  " + " PRIMARY KEY (`Id`)"
			+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";

	StringBuilder sqlBuilder = new StringBuilder();
	
	/**
	 * @throws java.lang.Exception             
	 * 
	 */
	@Before
	public void setUp() throws Exception {	
		
		if (mySQLFactory == null)
			mySQLFactory = new MySQLScriptServiceFactory();
		if (mySQLScriptService == null)
			mySQLScriptService = mySQLFactory.createDBScriptService();
		if (mySQLDBDeployer == null)
			mySQLDBDeployer = mySQLFactory.createDBDeployer();
		scriptFileGuid = UUID.randomUUID().toString();
		dbName = UUID.randomUUID().toString().replace("-", "_");

		JAXBContext jaxbContext = JAXBContext.newInstance(DBInstanceInfo.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		dbInfo = (DBInstanceInfo) unmarshaller
				.unmarshal(new File(
						"src/test/resources/unit/data-definition-language-mojo/local.connection"));

		File scriptFile = new File(new File("").getCanonicalPath() + "//"
				+ scriptFileGuid + ".sql");
		if (!scriptFile.exists())
			scriptFile.createNewFile();
		FileWriter scriptFileWriter = new FileWriter(scriptFile);
		scriptFileWriter.write(String.format(sqlStmts, dbName, dbName, dbName));
		scriptFileWriter.close();
		scriptFilePath =  scriptFile.getAbsolutePath();		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {		
		if(Files.exists(Paths.get(scriptFilePath), new LinkOption[]{ LinkOption.NOFOLLOW_LINKS}))
			Files.delete(Paths.get(scriptFilePath));
	}

	@Test
	public void testDeployDBSuccess() {
		Connection connection = null;
		Statement stmt = null;

		try {
			assertTrue(mySQLDBDeployer.deployDB(dbInfo, scriptFilePath));
		} catch (Exception e) {
			e.printStackTrace();
			fail("testDeployDBSuccess failed");
		}

		// check if the DB has actually been created
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(
					String.format(MySQLServerUrl, dbInfo.getDatabaseHost(), dbServerPort),
					dbInfo.getAdminUserName(), dbInfo.getAdminUserPassword());
			ResultSet catalogs = connection.getMetaData().getCatalogs();
			boolean isDBCreated = false;
			while (catalogs.next()) {

				String schemaName = catalogs.getString(1);
				if (schemaName.toLowerCase().equals(dbName.toLowerCase())) {
					isDBCreated = true;
				}
			}
			catalogs.close();
			assertTrue(isDBCreated);

			// delete DB
			stmt = connection.createStatement();
			String deleteDBSQL = "DROP DATABASE " + dbName;
			stmt.executeUpdate(deleteDBSQL);

		} catch (SQLException |ClassNotFoundException e) {			
			e.printStackTrace();
			//TODO - check for class and fail with either CNFE or SQLE message
			fail("JDBC failure");
		}  finally {
			try {
				if (stmt != null)
					stmt.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {				
				e.printStackTrace();
			}
		}

	}

	@Test
	public void testDeployDBFailureWithWrongAdminUserName() throws Exception {
		dbInfo.setAdminUserName(UUID.randomUUID().toString());		
		assertFalse(mySQLDBDeployer.deployDB(dbInfo, ""));	

	}

	@Test
	public void testDeployDBFailureWithWrongAdminPassword() throws Exception {
		dbInfo.setAdminUserPassword(UUID.randomUUID().toString());		
		assertFalse(mySQLDBDeployer.deployDB(dbInfo, ""));	
	}	
	
	@Test
	public void testDeployDBFailureWhenDBInstanceNotRunning() {		
		// TODO - need to stop instance and run this test case, and start again
	}

}
