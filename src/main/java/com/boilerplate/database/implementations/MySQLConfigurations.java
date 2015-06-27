package com.boilerplate.database.implementations;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.boilerplate.database.interfaces.IConfigurations;
import com.boilerplate.framework.HibernateUtility;
import com.boilerplate.java.entities.Configuration;

/**
 * This class is used to fetch configurations from a MySQL database.
 * The end user must enable this class in the DI configuration to use it.
 * @author gaurav
 *
 */
public class MySQLConfigurations implements IConfigurations{

	public MySQLConfigurations(){
		System.out.print("Created");
	}
	/**
	 * @see IConfigurations.getConfigurations
	 */
	@Override
	public List<Configuration> getConfirguations(String version,String enviornment) {
		Session session =null;
		List<Configuration> configurations = null;
		try{
			//open a session
			session = HibernateUtility.getSessionFactory().openSession();
			//get all the configurations from the DB as a list
			Transaction transaction = session.beginTransaction();
			String hsql = "From Configuration C Where C.version = :Version AND C.enviornment= :Enviornment";
			Query query = session.createQuery(hsql);
			
			//Get all the configuration for the verson ALL
			query.setParameter("Version", "ALL");
			query.setParameter("Enviornment", "ALL");
			configurations = query.list();
			
			//Get all the configuration for this enviornments for this version
			//We are getting this version after ALL, so that we can override any version specific configuration
			query.setParameter("Version", "ALL");
			query.setParameter("Enviornment", enviornment);
			configurations.addAll(query.list());
			

			//Get all the configuration for this version for all enviornments
			//We are getting this version after ALL, so that we can override any version specific configuration
			query.setParameter("Version", version);
			query.setParameter("Enviornment", "ALL");
			configurations.addAll(query.list());
			
			//get the configuration for this enviornment and version
			query.setParameter("Version", version);
			query.setParameter("Enviornment", enviornment);
			configurations.addAll(query.list());
			//return the said list
			transaction.commit();
		}
		catch(Exception ex){
			System.out.println(ex.toString());
			throw ex;
		//Not catching the exception because we want the system to
		//crash if config is not working
		//further at this stage the logger is not setup, hence we cant log anything
		//the JVM will crash and the terminal will display the details of crash
		}
		finally{
			session.close();
		}
		return configurations;
	}
}
