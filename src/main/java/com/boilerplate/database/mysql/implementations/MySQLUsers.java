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
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.Configuration;
import com.boilerplate.java.entities.ExternalFacingUser;

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
		try{
			super.create(user);
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
			return users.get(0);
	}//end method

	/**
	 * @see IUser.deleteUser
	 */
	@Override
	public void deleteUser(ExternalFacingUser user) {
		super.delete(user);
	}

	/**
	 * @see IUser.update
	 */
	@Override
	public ExternalFacingUser update(ExternalFacingUser user)
			throws ConflictException {
		try{
			super.update(user);
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
		return user;

	}

	
}
