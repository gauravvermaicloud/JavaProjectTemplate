/**
 * 
 */
package com.boilerplate.test.MySQL;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.boilerplate.dbscript.impl.MySQL.MySQLScriptServiceFactory;
import com.boilerplate.dbscript.interfaces.DBDeployer;
import com.boilerplate.dbscript.interfaces.DBInstanceInfo;
import com.boilerplate.dbscript.interfaces.DBScriptService;
import com.boilerplate.dbscript.interfaces.DBScriptServiceFactory;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 * @author Vignesh
 *
 */
public class MySQLDBDeployTest {

	DBScriptServiceFactory mySQLFactory;
	DBScriptService mySQLScriptService;
	DBDeployer mySQLDBCreator;
	DBInstanceInfo dbInfo;

	
	private String scriptFilePath = "";
	private String scriptFileGuid = "";
	private String dbName = "TestingDB";	
	private int dbServerPort = 3306;
	private String MySQLServerUrl = "jdbc:mysql://%s:%s";
	private String sqlStmts = "DROP Database If exists `%s`;"
			+ "CREATE DATABASE  IF NOT EXISTS %s ;" + "USE %s;"
			+ "DROP TABLE IF EXISTS `Configurations`;"
			+ "CREATE TABLE `Configurations` ( " + " `Id` bigint(10) NOT NULL,"
			+ "`ConfigurationKey` varchar(64) NOT NULL,"
			+ "`ConfigurationValue` varchar(512) DEFAULT NULL,"
			+ "`Version` varchar(64) NOT NULL,"
			+ "`Enviornment` varchar(64) NOT NULL,  " + " PRIMARY KEY (`Id`)"
			+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";

	/**
	 * @throws java.lang.Exception
	 *             ;
	 * 
	 */
	@Before
	public void setUp() throws Exception {
		if (mySQLFactory == null)
			mySQLFactory = new MySQLScriptServiceFactory();
		if (mySQLScriptService == null)
			mySQLScriptService = mySQLFactory.createDBScriptService();
		if (mySQLDBCreator == null)
			mySQLDBCreator = mySQLFactory.createDBDeployer();
		scriptFileGuid = UUID.randomUUID().toString();
		dbName = UUID.randomUUID().toString().replace("-", "_");

		JAXBContext jaxbContext = JAXBContext.newInstance(DBInstanceInfo.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		dbInfo = (DBInstanceInfo) unmarshaller
				.unmarshal(new File(
						"src/test/resources/unit/data-definition-language-mojo/local.connection"));

		// credits:http://stackoverflow.com/questions/3153337/how-do-i-get-my-current-working-directory-in-java
		File scriptFile = new File(new File("").getCanonicalPath() + "\\"
				+ scriptFileGuid + ".sql");
		if (!scriptFile.exists())
			scriptFile.createNewFile();
		FileWriter scriptFileWriter = new FileWriter(scriptFile);
		scriptFileWriter.write(String.format(sqlStmts, dbName, dbName, dbName));
		scriptFileWriter.close();
		scriptFilePath = scriptFile.getAbsolutePath();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		// mySQLFactory = null;
		// mySQLScriptService = null;
		// mySQLDBCreator = null;
		Files.delete(Paths.get(scriptFilePath));

	}

	@Test
	public void testDeployDBSuccess() {
		Connection _connection = null;
		Statement _stmt = null;

		try {
			assertTrue(mySQLDBCreator.deployDB(dbInfo, scriptFilePath));
		} catch (Exception e) {
			fail("testDeployDBSuccess failed "+e.toString());
		}

		// check if the DB has actually been created
		try {
			Class.forName("com.mysql.jdbc.Driver");
			_connection = DriverManager.getConnection(
					String.format(MySQLServerUrl, dbInfo.getDatabaseHost(), dbServerPort),
					dbInfo.getAdminUserName(), dbInfo.getAdminUserPassword());
			ResultSet catalogs = _connection.getMetaData().getCatalogs();
			boolean isDBCreated = false;
			while (catalogs.next()) {

				String _dbName = catalogs.getString(1);
				if (_dbName.equals(dbName)) {
					isDBCreated = true;
				}
			}
			catalogs.close();
			assertTrue(isDBCreated);

			// delete DB
			_stmt = _connection.createStatement();
			String deleteDBSQL = "DROP DATABASE " + dbName;
			_stmt.executeUpdate(deleteDBSQL);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("JDBC failure");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("JDBC failure - ClassNotFoundException");
		} finally {
			try {
				if (_stmt != null)
					_stmt.close();

				if (_connection != null)
					_connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Test
	public void testDeployDBFailureWithWrongAdminUserName() {

		dbInfo.setAdminUserName(UUID.randomUUID().toString());

		try {
			assertFalse(mySQLDBCreator.deployDB(dbInfo, scriptFilePath));
		} catch (Exception e) {
			fail("testDeployDBFailureWithWrongAdminUserName failed");
		}

	}

	@Test
	public void testDeployDBFailureWithWrongAdminPassword() {

		dbInfo.setAdminUserPassword(UUID.randomUUID().toString());
		try {
			assertFalse(mySQLDBCreator.deployDB(dbInfo, scriptFilePath));
		} catch (Exception e) {
			fail("testDeployDBFailureWithWrongAdminPassword failed");
		}

	}

	@Test
	public void testDeployDBFailureWithWrongDBBinPath() {

		dbInfo.setBinDirectoryPath(dbInfo.getBinDirectoryPath() + "\\123\\");
		try {
			assertFalse(mySQLDBCreator.deployDB(dbInfo, scriptFilePath));
		} catch (Exception e) {
			assertTrue("testDeployDBFailureWithWrongDBBinPath failed", true);
		}
	}

	@Test
	public void testDeployDBFailureWhenDBAlreadyExists() {
		Connection _connection = null;
		Statement _stmt = null;

		try {
			// assertTrue(mySQLDBCreator.deployDB(dbInfo,scriptFilePath));
			assertTrue(mySQLDBCreator.deployDB(dbInfo, scriptFilePath));

			Class.forName("com.mysql.jdbc.Driver");
			_connection = DriverManager.getConnection(
					String.format(MySQLServerUrl, dbInfo.getDatabaseHost(), dbServerPort),
					dbInfo.getAdminUserName(), dbInfo.getAdminUserPassword());

			_stmt = _connection.createStatement();
			String deleteDBSQL = "DROP DATABASE " + dbName;
			_stmt.executeUpdate(deleteDBSQL);

		} catch (Exception e) {
			fail("testDeployDBFailureWhenDBAlreadyExists failed");
		}

	}

	// TODO - need to stop instance and run this test case, and start again
	@Test
	public void testDeployDBFailureWhenDBInstanceNotRunning() {
		dbInfo = new DBInstanceInfo();

	}

}
