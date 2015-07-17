package com.boilerplate.database.mysql.implementations;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.boilerplate.aspects.LogAndTraceExceptionAspect;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.database.mysql.implementations.entities.UserMetaData;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.HibernateUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.Configuration;
import com.boilerplate.java.entities.ExternalFacingUser;

import org.springframework.transaction.annotation.Transactional;


/**
 * This class is used to create a user in a MySQL database
 * @author gaurav
 *
 */
public class MySQLUsers extends MySQLBaseDataAccessLayer implements IUser{

	
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
				//create metadata
				UserMetaData userMetaData = null;
				for(String key: user.getUserMetaData().keySet()){
					userMetaData = new UserMetaData();
					userMetaData.setUserId(Long.parseLong(user.getId()));
					userMetaData.setMetaDataKey(key);
					userMetaData.setMetaDataValue(user.getUserMetaData().get(key));
					session.save(userMetaData);
				}
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
			//get the user using a hsql query
			String hsql = "From ExternalFacingUser U Where U.userId = :UserId";
			BoilerplateMap<String, Object> queryParameterMap = new BoilerplateMap<String, Object>();
			queryParameterMap.put("UserId", userId);
			List<ExternalFacingUser> users = super.executeSelect(hsql, queryParameterMap);			
			if(users.isEmpty()){
				throw new NotFoundException("User","User Not found", null);
			}
			ExternalFacingUser user =  users.get(0);
			
			hsql = "From UserMetaData U Where U.userId = :UserId";
			queryParameterMap = new BoilerplateMap<String, Object>();
			queryParameterMap.put("UserId",Long.parseLong(user.getId()));
			List<UserMetaData> userMetaDatum = super.executeSelect(hsql, queryParameterMap);
			if(!userMetaDatum.isEmpty()){
				for(UserMetaData userMetaData : userMetaDatum){
					user.getUserMetaData().put(userMetaData.getMetaDataKey()
							, userMetaData.getMetaDataValue());
				}
			}
			return user;
	}//end method
	
	/**
	 * @see IUser.deleteUser
	 */
	@Override
	public void deleteUser(ExternalFacingUser user) {
		//get the metadata of user and delete it
		String hsql = "From UserMetaData U Where U.userId = :UserId";
		BoilerplateMap<String, Object> queryParameterMap = new BoilerplateMap<String, Object>();
		queryParameterMap.put("UserId",Long.parseLong(user.getId()));
		List<UserMetaData> userMetaDatum = super.executeSelect(hsql, queryParameterMap);
		BoilerplateList<Object> objects = new BoilerplateList<Object>();
		objects.addAll(userMetaDatum);
		objects.add(user);
		super.delete(objects);
	}

	/**
	 * @see IUser.update
	 */
	@Override
	public ExternalFacingUser update(ExternalFacingUser user)
			throws ConflictException {
		Session session =null;	
		try{
				//open a session
				session = HibernateUtility.getSessionFactory().openSession();
				//get all the configurations from the DB as a list
				Transaction transaction = session.beginTransaction();
				session.saveOrUpdate(user);
				//create metadata
				UserMetaData userMetaData = null;
				for(String key: user.getUserMetaData().keySet()){
					userMetaData = new UserMetaData();
					userMetaData.setUserId(Long.parseLong(user.getId()));
					userMetaData.setMetaDataKey(key);
					userMetaData.setMetaDataValue(user.getUserMetaData().get(key));
					session.saveOrUpdate(userMetaData);
				}
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
}
