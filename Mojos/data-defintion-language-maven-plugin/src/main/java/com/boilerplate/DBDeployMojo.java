package com.boilerplate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.boilerplate.databasescripts.implementations.MySQL.*;
import com.boilerplate.databasescripts.interfaces.*;
import com.boilerplate.databasescripts.interfaces.Constants.DB;
import com.boilerplate.databasescripts.utilities.LogHelper;
import com.boilerplate.databasescripts.utilities.PropertyFileManager;

import javax.xml.bind.*;

/**
 * This class is a maven goal which creates a database and runs data
 * definition statements against a target database. This plugin assumes that the
 * mojo which would generate the data definition statements has already been
 * run. The end result is a database which is set up with boilerplate tables and
 * some seed data.
 * 
 * @author shrivb
 */
@Mojo(name = "deployDB", defaultPhase = LifecyclePhase.GENERATE_TEST_RESOURCES)
public class DBDeployMojo extends AbstractMojo {

	/**
	 * This refers to the base directory of the maven project during the maven cycle of which, this plugin would be run.
	 * All folder structures used by this plugin would be in reference to this base directory.
	 */
	@Parameter(defaultValue = "${basedir}")
	private File projectDir;
	
	/**
	 * This is the maven logger which is used to display the messages on the console during plugin execution.  
	 */
	private Log log;

	/**
	 * This method is invoked by the maven runtime to execute this goal
	 * @throws	MojoExecutionException
	 */
	public void execute() throws MojoExecutionException {
		try {
			if (log == null)
				log = getLog();

			log.info("Java Boiler Plate - Database Deploy Plugin");
			log.info("Base directory is:" + projectDir);

			if (projectDir == null) {
				//we shall exit if the mojo is not able to get the project base directory
				log.info("Project directory is null. Database deploy goal exiting");
				return;
			}
			deployDatabases(createDatabaseRepresentations(projectDir));
		} catch (JAXBException jaxException) {
			log.error("Error serializing one of the connection xml");
			jaxException.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method reads all the .connection files present under the
	 * /src/main/databases directory of the project and then creates the
	 * corresponding representations of each .connection file. At the end of
	 * this method, all the .connection files in the project would be
	 * represented as objects.
	 * 
	 * @param projectDirectory
	 *            The root directory of the project under which the maven plugin
	 *            is configured to run
	 * @return a collection of DBInstancInfo objects each which represent a
	 *         .connection file under the /src/main/databases directory
	 * @throws IOException, JAXBException 
	 */
	private List<DBInstanceInfo> createDatabaseRepresentations(File projectDirectory) throws JAXBException, IOException {
		List<DBInstanceInfo> dbInstances = new ArrayList<DBInstanceInfo>();
		String databaseFolderPath = "";
		String postScriptsFolderPath = "";
		PropertyFileManager propFileManager = null;
		try {
			propFileManager = new PropertyFileManager();
			propFileManager.initialize();			
		} catch (Exception e) {
			//log it
			log.info("There was some error in reading the properties file - datadefintitionplugin.properties");
		}	
		databaseFolderPath = propFileManager.getPropertyValue("DatabaseFolder");
		postScriptsFolderPath = propFileManager.getPropertyValue("PostScriptFolder");
		
		if(databaseFolderPath.isEmpty()) databaseFolderPath = "/src/main/databases/";
		if(postScriptsFolderPath.isEmpty()) postScriptsFolderPath = "/Scripts/PostScripts/";	
		
		log.info("Project directory is :" + projectDirectory);
		File[] directories = new File(projectDirectory.getCanonicalPath() + databaseFolderPath ).listFiles();	
		for (File dir : directories) {
			if (dir.isDirectory()) {
				log.info("Sub directory is:" + dir);
				File[] connectionFiles = dir.listFiles((file) -> file.isFile() && file.getName().toLowerCase().endsWith(".connection") && file.length() > 0);
				// process all the connection files, and create
				// corresponding representation as DBInstanceInfo
				for (File connection : connectionFiles) {
					JAXBContext jaxbContext = JAXBContext.newInstance(DBInstanceInfo.class);
					Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
					DBInstanceInfo dbInfo = (DBInstanceInfo) unmarshaller.unmarshal(connection);
					//dont add an instance for processing if any of the following is empty or null 
					//db host, admin username, admin password, port number, DBType
					if (!(StringUtils.isBlank(dbInfo.getDatabaseHost()) || StringUtils.isBlank( Integer.toString(dbInfo.getDatabasePortNumber())) || 
							StringUtils.isBlank(dbInfo.getAdminUserName()) || StringUtils.isBlank(dbInfo.getAdminUserPassword()) || 
							StringUtils.isBlank(dbInfo.getDBType().toString())))
					{
						dbInfo.setScriptsFolder(dir.getPath() + postScriptsFolderPath);						
						dbInstances.add(dbInfo);
					}
				}
			}
		}
		return dbInstances;
	}

	/**
	 * Accepts a collection of representation of database connection and creates
	 * some boilerplate tables with some seed data in them
	 * 
	 * @param dbInstances
	 *            a collection of representation of database connection
	 * @throws Exception
	 */
	private void deployDatabases(List<DBInstanceInfo> dbInstances) throws Exception {
		DBScriptServiceFactory dbScriptsFactory;		
		DBDeployer dbDeployer;
		// execute the mojos for each database representations
		// TODO - all this runs in a single thread. Need to parallelize.
		for (DBInstanceInfo dbInfo : dbInstances) {
			log.info("Mojo params");			
			LogHelper.initializeMvnLogger(log);
			LogHelper.LogDatabaseInstanceInfo(dbInfo, log);
			switch ((DB) dbInfo.getDBType()) {
			case CASSANDRA:
				break;
			case MONGODB:
				break;
			case MYSQL:				
				// TODO - make singleton initialization for factory
				dbScriptsFactory = new MySQLScriptServiceFactory();
				dbDeployer = dbScriptsFactory.createDBDeployer();
				// filter out the sql file which contains _CREATE_ in its
				// file name because the script generator mojo would have
				// created the scripts file as per this pattern.
				File[] createScriptFiles = new File(dbInfo.getScriptsFolder()).listFiles((File file) ->  file.isFile() && file.getName().indexOf(Constants.createWord) != -1);
				if (createScriptFiles.length > 0)
					//By design there would be only one file adhering to the above pattern in that scripts folder. Its picked up and run.
					//TODO - Re-visit this design after script generation to see if there would be more than one file
					dbDeployer.deployDB(dbInfo, createScriptFiles[0].getAbsolutePath());
				else
					log.error("Error deploying database. Could not find the expected create scripts file");
				break;
			case ORACLE:
				break;
			default:
				break;
			}
		}
	}
}
