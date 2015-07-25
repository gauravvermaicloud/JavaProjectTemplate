package com.boilerplate;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.boilerplate.dbscript.impl.MySQL.MySQLScriptServiceFactory;
import com.boilerplate.dbscript.impl.MySQL.MySQLUtilities;
import com.boilerplate.dbscript.interfaces.DBDeployer;
import com.boilerplate.dbscript.interfaces.DBInstanceInfo;
import com.boilerplate.dbscript.interfaces.DBScriptService;
import com.boilerplate.dbscript.interfaces.DBScriptServiceFactory;

/**
 * Goal which creates Data Definition Statements
 * 
 */
@Mojo(name = "generateScripts", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class DDLGeneratorMojo extends AbstractMojo {

	/**
	 * Path to the bin folder of the DB. Needed to execute some DB specific
	 * commands
	 * 
	 */
	@Parameter(property = "DBBinDirectoryPath", required = true)
	private String DBBinDirectoryPath;

	// TODO - explore if you can tell to the user in an intuitive way what are
	// allowed DBs
	/**
	 * The type of the DB for which the plugin would be generating scripts for
	 * Permissible values: MYSQL,ORACLE,CASSANDRA,MONGODB
	 */
	@Parameter(property = "DBType", required = true, defaultValue = "MYSQL")
	private DB DBType;
	/**
	 * The name of the target DB. If it does not already exist, plugin would not
	 * create it.
	 */
	@Parameter(property = "DBName", required = true)
	private String DBName;
	/**
	 * Name of a user who had admin privileges on the DB instance. This user
	 * would be used to create DB, run some commands on the DB instance.
	 */
	@Parameter(property = "adminUserName", required = true)
	private String adminUserName;
	/**
	 * Password of a user who had admin privileges on the DB instance. This user
	 * would be used to create DB, run some commands on the DB instance.
	 */
	@Parameter(property = "adminUserPassword", required = true)
	private String adminUserPassword;
	
	/**
	 * Path for the folder which contains the xml schemas of the Tables for which DDL statements
	 * have to be generated for 
	 */
	@Parameter(property = "XMLFolderPath", required = true)
	private String XMLFolderPath;
	
	/**
	 * Path for the folder where the DDL statements have to be generated in 
	 */
	 
	@Parameter(property = "destinationFolderPath", required = true)
	private String destinationFolderPath;

	public enum DB {
		MYSQL, ORACLE, CASSANDRA, MONGODB

	}

	DBScriptServiceFactory _dbScriptsFactory;
	DBScriptService _scriptService;
	DBDeployer _dbCreator;

	public void execute() throws MojoExecutionException {
		try {

			getLog().info("Java Boiler Plate - DDL Plugin");
			getLog().info("Mojo params");
			getLog().info("DBBinDirectoryPath:" + DBBinDirectoryPath);
			getLog().info("DBName:" + DBName);
			getLog().info("DBType:" + DBType);
			getLog().info("adminUserName:" + adminUserName);
			getLog().info("adminUserPassword:" + adminUserPassword);

			DBInstanceInfo _dbinfo = new DBInstanceInfo(DBBinDirectoryPath,DBName,adminUserName,adminUserPassword);
			
			switch ((DB) DBType) {
			case CASSANDRA:
				break;
			case MONGODB:
				break;
			case MYSQL:
				MySQLUtilities.initializeMvnLogger(getLog());
				_dbScriptsFactory = new MySQLScriptServiceFactory();
				_dbCreator = _dbScriptsFactory.createDBDeployer();
				_scriptService = _dbScriptsFactory.createDBScriptService();			
				
				break;
			case ORACLE:

				break;
			default:
				break;
			}

			// output = new StringBuffer();
			// reader =
			// new BufferedReader(new InputStreamReader(p1.getInputStream()));
			// while ((line = reader.readLine())!= null) {
			// output.append(line);
			// }
			// getLog().info("Command line output is" + output.toString());
			//
			// String line1 = "";
			// output = new StringBuffer();
			// reader =
			// new BufferedReader(new InputStreamReader(p2.getInputStream()));
			// while ((line1 = reader.readLine())!= null) {
			// output.append(line1);
			// }
		}

		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
