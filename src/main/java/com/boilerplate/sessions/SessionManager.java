package com.boilerplate.sessions;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import com.boilerplate.cache.CacheFactory;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
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
	private int sessionTimeOut =20*60*1000;
	
	/**
	 * This method gets a session with the given id.
	 * The method first checks if the session is on cache, if not then the method gets it from database
	 * @param sessionId The id of the session
	 * @return A session
	 * @throws ValidationFailedException This exception will not be thrown as it is required by validation failed exception
	 * which in this case is made to false
	 */
	public Session getSession(String sessionId) throws ValidationFailedException{
		
		//first check if the session exists in cache
		Session session =getSessionFromCache(sessionId);
		
		//if not then check session exists in DB
		if(session == null){
			session = this.session.getSession(sessionId);
		}
		if(session != null){
 			//if session has expired
 			if(session.validate()){
 				session = null;
 			}
 			else{
 				//if the session has not expired update the last update date 
 				//to increase the life of session
 				session.setUpdationDate(new Date());
 			}
 		}
		
		//put the session back on cache with new expiry
		putSessionOnCache(session);
		
		//the queue job will put it back  in database we dont write back to DB
		//from here itself because it will cause performance issue
		//and we cant just rely upon cache because during a memeory preassure 
		//the cache may be evicted
		
		//TODO - put code to put the session into the database using queue
		
		//return the session or return null
		return session;
	}
	
	/**
	 * This method gets a session if available from cache
	 * @param sessionId This is the id of the session
	 * @return A session
	 */
	private Session getSessionFromCache(String sessionId){
		Session session = null;
		try {
			if(CacheFactory.getInstance().isCacheEnabled()){
				session = CacheFactory.getInstance().get(Constants.SESSION+sessionId.toUpperCase(), Session.class);
			}
		} catch (Exception ex) {
			logger.logException("SessionManager", "getSession"
					, "Get Session From cache",ex.toString(),ex);
		}
		return session;
	}
	
	/**
	 * This method puts session on cache
	 * @param session The session
	 */
	private void putSessionOnCache(Session session){
		try {
			if(CacheFactory.getInstance().isCacheEnabled()){
				CacheFactory.getInstance().add(Constants.SESSION+session.getSessionId()
						, session, sessionTimeOut);
			}
		} catch (Exception ex) {
			//although there is an error in accessing cache
			//but we will move ahead as the DB is getting saved
			//So someone who will be doing monitoring will know
			//cache issue from here or from the ping
			//which is expected to be run 
			logger.logException("SessionManager", "getSession"
					, "Get Session From cache",ex.toString(),ex);
		}
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
		putSessionOnCache(session);		
		return session;
	}
	
	/**
	 * Default constructor
	 */
	public SessionManager(){
	
	}
	
	/**
	 * Initializes configuration after bean creation
	 */
	public void initialize(){
		this.sessionTimeOut = Integer.parseInt(configurationManager.get("SessionTimeOutInMinutes"))*60;
	}
	
	/**
	 * This method returns the session time out.
	 * @return
	 */
	public int getSessionTimeout(){
		return this.sessionTimeOut;
	}
	
}
