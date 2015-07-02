package com.boilerplate.database.mysql.implementations;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.boilerplate.aspects.LogAndTraceExceptionAspect;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.HibernateUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.entities.Configuration;
import com.boilerplate.java.entities.ExternalFacingUser;

/**
 * This class is used to create a user in a MySQL database
 * @author gaurav
 *
 */
public class MySQLUsers implements IUser{

	
	/**
	 * This is the logger
	 */
	private Logger logger = Logger.getInstance(MySQLUsers.class);
	
	/**
	 * @see IUser.create
	 */
	@Override
	public ExternalFacingUser create(ExternalFacingUser user) throws ConflictException {
		Session session =null;	
		try{
			//open a session
			session = HibernateUtility.getSessionFactory().openSession();
			//get all the configurations from the DB as a list
			Transaction transaction = session.beginTransaction();
			session.save(user);
			//commit the transaction
			transaction.commit();
		}
		catch(ConstraintViolationException cve){
			logger.logException("MySQLUsers", "create", "ConstraintViolationException"
					, cve.toString(), cve);
			throw new ConflictException("User"
				,"The user name "+user.getUserId()
				+" already exists for the provider, details of inner exception not "
				+ "displayed for security reason, but are logged"
				,null);
		}
		finally{
			if(session !=null && session.isOpen()){
				session.close();
			}
		}
		return user;

	}
	
	/**
	 * @see IUser.getUser
	 */
	@Override
	public ExternalFacingUser getUser(String userId) throws NotFoundException{
		Session session =null;
		try{

			//open a session
			session = HibernateUtility.getSessionFactory().openSession();
			//get all the configurations from the DB as a list
			Transaction transaction = session.beginTransaction();
			String hsql = "From ExternalFacingUser U Where U.userId = :UserId";
			Query query = session.createQuery(hsql);
			query.setParameter("UserId", userId);
			@SuppressWarnings("unchecked")
			List<ExternalFacingUser> users = query.list();			
			if(users.isEmpty()){
				throw new NotFoundException("User","User Not found", null);
			}
			transaction.commit();
			return users.get(0);
		}
		finally
		{
			if(session !=null && session.isOpen()){
				session.close();
			}
		}//end finally
	}//end method

	
}
