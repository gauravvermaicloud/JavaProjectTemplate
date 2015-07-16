package com.boilerplate.database.mysql.implementations;

import java.util.List;

import javax.xml.crypto.dsig.keyinfo.KeyValue;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.HibernateUtility;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ExternalFacingUser;

/**
 * This method is a base of data access layer
 * @author gaurav.verma.icloud
 *
 */
public class MySQLBaseDataAccessLayer {
	
	/**
	 * This method creates an object in the database
	 * @param t The object to be created
	 * @return The object as it stands after creation
	 */
	public <T> T create (T t){
		Session session =null;	
		try{
			//open a session
			session = HibernateUtility.getSessionFactory().openSession();
			//get all the configurations from the DB as a list
			Transaction transaction = session.beginTransaction();
			session.save(t);
			//commit the transaction
			transaction.commit();
			return t;
		}
		finally{
			if(session !=null && session.isOpen()){
				session.close();
			}
		}
		
	}
	
	/**
	 * This method is used to update an item in the database
	 * @param t The item to be updated
	 * @return The item after update
	 */
	public <T> T update (T t){
		Session session =null;	
		try{
			//open a session
			session = HibernateUtility.getSessionFactory().openSession();
			//get all the configurations from the DB as a list
			Transaction transaction = session.beginTransaction();
			session.saveOrUpdate(t);
			//commit the transaction
			transaction.commit();
			return t;
		}
		finally{
			if(session !=null && session.isOpen()){
				session.close();
			}
		}
		
	}	
	
	/**
	 * This api deletes the object from database
	 * @param t The item to be deleted
	 */
	public <T> void delete(T t) {
		Session session =null;
		try{
			//open a session
			session = HibernateUtility.getSessionFactory().openSession();
			Transaction transaction = session.beginTransaction();
			//delete the object
			session.delete(t);
			//commit
			transaction.commit();
		}
		finally
		{
			if(session !=null && session.isOpen()){
				session.close();
			}
		}//end finally
	}
	
	/**
	 * This method executes a given query
	 * @param hSQLQuery The query to be executed
	 * @param queryParameters The query parameters
	 * @return result of query execution
	 */
	public <T> List<T> executeSelect(
			String hSQLQuery,
			BoilerplateMap<String, Object> queryParameters
			){
		Session session =null;
		try{
			//open a session
			session = HibernateUtility.getSessionFactory().openSession();
			//begin a transaction
			Transaction transaction = session.beginTransaction();
			//get the user using a hsql query
			Query query = session.createQuery(hSQLQuery);
			
			for(String key : queryParameters.keySet()){
				query.setParameter(key, queryParameters.get(key));
			}
			List<T> ts = query.list();						
			transaction.commit();
			return ts;
		}
		finally
		{
			if(session !=null && session.isOpen()){
				session.close();
			}
		}//end finally
	}//end method
	
	public int executeUpdate(
			String hSQLQuery,
			BoilerplateMap<String, Object> queryParameters
			){
		Session session =null;
		try{
			//open a session
			session = HibernateUtility.getSessionFactory().openSession();
			//begin a transaction
			Transaction transaction = session.beginTransaction();
			//get the user using a hsql query
			Query query = session.createQuery(hSQLQuery);
			
			for(String key : queryParameters.keySet()){
				query.setParameter(key, queryParameters.get(key));
			}
			int rowsEffected = query.executeUpdate();
			transaction.commit();
			return rowsEffected;
		}
		finally
		{
			if(session !=null && session.isOpen()){
				session.close();
			}
		}//end finally
	}//end method
}
