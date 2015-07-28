package com.boilerplate;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.boilerplate.dbscript.impl.MySQL.*;
import com.boilerplate.dbscript.impl.MySQL.MySQLUtilities.DB;
import com.boilerplate.dbscript.interfaces.*;

import javax.xml.bind.*;

/**
 * Goal which creates Data Definition Statements
 * 
 */
@Mojo(name = "deployDB", defaultPhase = LifecyclePhase.GENERATE_TEST_RESOURCES)
public class DBDeployMojo extends AbstractMojo {

	@Parameter(defaultValue = "${basedir}")
	private String projectDir;

	@Parameter(defaultValue = "${project}")
	private MavenProject project;

	DBScriptServiceFactory _dbScriptsFactory;
	DBScriptService _scriptService;
	DBDeployer _dbDeployer;

	@SuppressWarnings("restriction")
	public void execute() throws MojoExecutionException {
		try {

			Log log = getLog();

			log.info("Java Boiler Plate - DB Deploy Plugin");
			

			log.info("Base directory is:" + projectDir);

			List<DBInstanceInfo> dbinstances = new ArrayList<DBInstanceInfo>();

			if (projectDir == null) {
				log.info("Project directory is null. DB Deploy exitting");
				return;
				// if(project != null)
				// log.info(project.getBasedir().getPath());
				// if(project == null) return;
			}

			// TODO - all this runs in a single thread. Need to parallelize.
			// Miss .Net's Parallel construct for For
			File[] directories = new File(projectDir + "/src/main/databases/")
					.listFiles();
			for (File dir : directories) {
				if (dir.isDirectory()) {
					log.info("Sub directory is:" + dir);
					File[] connectionFiles = dir.listFiles(new FileFilter() {
						@Override
						public boolean accept(File file) {
							return file.isFile()
									&& file.getName().toLowerCase()
											.endsWith(".connection")
									&& file.length() > 0;
						}

					});

					// for all the connection files for a particular DB folder,
					// execute the mojo
					for (File connection : connectionFiles) {
						JAXBContext jaxbContext = JAXBContext
								.newInstance(DBInstanceInfo.class);
						Unmarshaller unmarshaller = jaxbContext
								.createUnmarshaller();
						DBInstanceInfo _dbInfo = (DBInstanceInfo) unmarshaller
								.unmarshal(connection);
						_dbInfo.setScriptsFolder(dir.getPath()
								+ "/Scripts/PostScripts/");
						dbinstances.add(_dbInfo);
					}
				}
			}

			for (DBInstanceInfo _dbInfo : dbinstances) {
				log.info("Mojo params");
				log.info("DBBinDirectoryPath:" + _dbInfo.getBinDirectoryPath());
				log.info("DBHost:" + _dbInfo.getDatabaseHost());
				log.info("DBType:" + _dbInfo.getDBType());
				log.info("adminUserName:" + _dbInfo.getAdminUserName());
				log.info("adminUserPassword:" + _dbInfo.getAdminUserPassword());
				log.info("scriptsFolder:" + _dbInfo.getScriptsFolder());

				// DBInstanceInfo _dbInfo = new
				// DBInstanceInfo(DBBinDirectoryPath,DBHost,adminUserName,adminUserPassword);

				// TODO - make singleton initialization for factory and family
				// classes

				switch ((DB) _dbInfo.getDBType()) {
				case CASSANDRA:
					break;
				case MONGODB:
					break;
				case MYSQL:
					MySQLUtilities.initializeMvnLogger(log);
					_dbScriptsFactory = new MySQLScriptServiceFactory();
					_dbDeployer = _dbScriptsFactory.createDBDeployer();
					// and file name would be the first sql in that folder
					_dbDeployer.deployDB(_dbInfo, _dbInfo.getScriptsFolder()
							+ "DBName_CreateScript.sql");
					// log.error(String.format("Creation of DB %s failed. So script generation is stopped",DBName));
					break;
				case ORACLE:

					break;
				default:
					break;
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
