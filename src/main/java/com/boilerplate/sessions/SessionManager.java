package com.boilerplate.sessions;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

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
	 * The time out of session	
	 */
	int sessionTimeOut = Integer.parseInt(configurationManager.get("SessionTimeOutInMinutes"))*60;
	
	
	public static Session getSession(String sessionId){
		//TODO - validate session id from cache or DB and deserialize / construct it
		return null;
	}
	
	public Session createNewSession(ExternalFacingUser externalFacingUser){
		Session session = new Session(externalFacingUser);
		
		//Save session on DB
		
		//Save session on cache
		try{
		CacheFactory.getInstance().add(Constants.SESSION+session.getId()
				, session, sessionTimeOut);
		}
		catch(Exception ex){
			//TODO - Log that cache is not wokring
			//also disable cache if the cache disable count is greater than expected
			//If the cache is not working it is okey, we just log and move forward
		}
		return session;
	}
}
