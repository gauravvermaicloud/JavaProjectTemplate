package com.boilerplate.sessions;

import java.io.Serializable;
import java.util.UUID;

import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ExternalFacingUser;

/**
 * This class is used to implement a session.
 * It contains the session id along with a number of 
 * session objects
 * @author gaurav
 *
 */
public class Session implements Serializable{
	
	/**
	 * This is the unique session id of the session.
	 */
	private String sessionId;
	
	/**
	 * This method returns the session id of the session
	 * @return The session id
	 */
	public String getSessionId(){
		return this.sessionId;
	}
	/**
	 * This is the user associated with the session.
	 */
	private ExternalFacingUser user;
	
	/**
	 * This method returns the external facing user
	 * @return The user for the session.
	 */
	public ExternalFacingUser getExternalFacingUser(){
		return this.user;
	}
	
	/**
	 * This is the map of session objects
	 */
	private BoilerplateMap<String, Object> sessionObjects;
	
	/**
	 * This returns a session object
	 * @param externalFacingUser The user whose session is being created
	 */
	public Session(ExternalFacingUser externalFacingUser){
		this.sessionId = UUID.randomUUID().toString();
		this.user = externalFacingUser;
	}
}
