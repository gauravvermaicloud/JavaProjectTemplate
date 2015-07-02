package com.boilerplate.database.mysql.implementations;

import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.boilerplate.database.interfaces.ISession;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.framework.HibernateUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.sessions.Session;

/**
 * This class implements session store on MySQL Server
 * @author gaurav.verma.icloud
 *
 */
public class MySQLSession implements ISession{
	/**
	 * This is the logger
	 */
	private Logger logger = Logger.getInstance(MySQLSession.class);

	/**
	 * @see ISession.create
	 */
	@Override
	public Session create(Session session) {
		org.hibernate.Session hibernateSession =null;	
		try{
			//open a session
			hibernateSession = HibernateUtility.getSessionFactory().openSession();
			//get all the configurations from the DB as a list
			Transaction transaction = hibernateSession.beginTransaction();
			hibernateSession.save(session);
			//commit the transaction
			transaction.commit();
		}
		finally{
			if(hibernateSession !=null && hibernateSession.isOpen()){
				hibernateSession.close();
			}
		}
		return session;
	}

}
