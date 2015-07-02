package com.boilerplate.sessions;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import com.boilerplate.cache.CacheFactory;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.Constants;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.service.implemetations.UserService;

public class SessionManager {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(UserService.class);
	
	/**
	 * This is the instance of the configuration manager.
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;
	
	/**
	 * The setter to set the configuration manager
	 * @param configurationManager
	 */
	public void setConfigurationManager(
			com.boilerplate.configurations.ConfigurationManager 
			configurationManager){
		this.configurationManager = configurationManager;
	}
	
	/**
	 * This is the instance of the session database object
	 */
	@Autowired
	com.boilerplate.database.interfaces.ISession session;
	
	/**
	 * This method sets the instance of the session
	 * @param session The instance of the session.
	 */
	public void setSession(com.boilerplate.database.interfaces.ISession session){
		this.session = session;
	}
	
	/**
	 * The time out of session	
	 */
	int sessionTimeOut=1000; 
	
	
	public Session getSession(String sessionId){
		//TODO - validate session id from cache or DB and deserialize 
		//TODO - construct it
		return null;
	}
	
	/**
	 * This method creates a session for the given user
	 * and saves it to the database and cache. If the user is not saved to
	 * cache it will not throw an exception however if there is an issue
	 * saving the session to DB there will be an error
	 * @param externalFacingUser This is the user for whom session is
	 * created
	 * @return The session created
	 */
	public Session createNewSession(ExternalFacingUser externalFacingUser){
		Session session = new Session(externalFacingUser);
		
		//Save session on DB
		this.session.create(session);
		//Save session on cache
		try{
			CacheFactory.getInstance().add(Constants.SESSION+session.getId()
					, session, sessionTimeOut);
		}
		//although there is an error in accessing cache
		//but we will move ahead as the DB is getting saved
		//So someone who will be doing monitoring will know
		//cache issue from here or from the ping
		//which is expected to be run 
		catch(Exception ex){
			logger.logException("SessionManager", "createNewSession"
					, "Save Session to Cache",ex.toString(),ex);
		}
		return session;
	}
	
	public SessionManager(){
		//this.sessionTimeOut=Integer.parseInt(configurationManager.get("SessionTimeOutInMinutes"))*60;
	}
}
