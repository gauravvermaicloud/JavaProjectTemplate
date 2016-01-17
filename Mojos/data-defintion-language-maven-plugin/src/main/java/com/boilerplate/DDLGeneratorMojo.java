package com.boilerplate;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.boilerplate.databasescripts.implementations.MySQL.MySQLScriptServiceFactory;
import com.boilerplate.databasescripts.interfaces.Constants.DB;
import com.boilerplate.databasescripts.interfaces.DBDeployer;
import com.boilerplate.databasescripts.interfaces.DBInstanceInfo;
import com.boilerplate.databasescripts.interfaces.DBScriptService;
import com.boilerplate.databasescripts.interfaces.DBScriptServiceFactory;
import com.boilerplate.databasescripts.utilities.LogHelper;

/**
 * This class is a maven goal which would generate all the database scripts 
 * pertaining to various databases. The scripts include a PreScript, Data Defintion Language script,
 * and a PostScript. At the end all these scripts would be combined into a single script, execution of which, 
 * would set up a complete boilerplate database. 
 *  
 * @author shrivb
 */
@Mojo(name = "generateScripts", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class DDLGeneratorMojo extends AbstractMojo {	

	// TODO - explore if you can tell to the user in an intuitive way what are
	// allowed DBs
	
	/**
	 * This is the maven logger which is used to display the messages on the console during plugin execution.  
	 */
	private Log log;
	
	/**
	 * The type of the DB for which the plugin would be generating scripts for
	 * Permissible values: MYSQL,ORACLE,CASSANDRA,MONGODB
	 */
	@Parameter(property = "DBType", required = true, defaultValue = "MYSQL")
	private DB DBType;
	/**
	 * The name of the target DB. If it does not already exist, plugin would
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
	
	/**
	 * The factory which facilitates creation of a set of related objects pertaining to a specific DB
	 * Since this is a maven plugin and used only during build phases, no DI is used here.  
	 */
	DBScriptServiceFactory dbScriptsFactory;
	
	/**
	 * The script service which is used generate scripts for specific databases
	 */
	DBScriptService scriptService;	

	//TODO - implementation
	public void execute() throws MojoExecutionException {
		try {
			if (log == null)
				log = getLog();
			LogHelper.initializeMvnLogger(log);			
			
			switch ((DB) DBType) {
			case CASSANDRA:
				break;
			case MONGODB:
				break;
			case MYSQL:				
				dbScriptsFactory = new MySQLScriptServiceFactory();				
				scriptService = dbScriptsFactory.createDBScriptService();				
				break;
			case ORACLE:

				break;
			default:
				break;
			}		
		}	
		catch (Exception e) {
			e.printStackTrace();
		}

	}
}
