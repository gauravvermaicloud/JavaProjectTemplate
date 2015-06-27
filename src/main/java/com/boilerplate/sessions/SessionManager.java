package com.boilerplate.sessions;

import java.util.UUID;

import com.boilerplate.cache.CacheFactory;
import com.boilerplate.java.Constants;
import com.boilerplate.java.entities.ExternalFacingUser;

public class SessionManager {


	/**
	 * The time out of session	
	 */
	int sessionTimeOut = Integer.parseInt(configurationManager.get("SessionTimeOutInMinutes"))*60;
	
	
	public static Session getSession(String sessionId){
		//TODO - validate session id from cache or DB and deserialize / construct it
		return null;
	}
	
	public static Session createNewSession(ExternalFacingUser externalFacingUser){
		Session session = Session(externalFacingUser);
		
		//Save session on DB
		
		//Save session on cache
		try{
		CacheFactory.getInstance().add(Constants.SESSION+session.getSessionId()
				, session, sessionTimeOut);
		}
		catch(Exception ex){
			//TODO - figure the exception part
		}
	}
}
