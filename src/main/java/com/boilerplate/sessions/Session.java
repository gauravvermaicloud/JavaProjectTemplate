package com.boilerplate.sessions;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.BaseEntity;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is used to implement a session.
 * It contains the session id along with a number of 
 * session objects
 * @author gaurav
 *
 */
public class Session extends BaseEntity implements Serializable{
	
	/**
	 * This is the session Id.
	 */
	private String sessionId;
	
	
	private String userId;
	
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
		this.setSessionId(UUID.randomUUID().toString());
		this.user = externalFacingUser;
		//The primary key Id of the user
		this.setUserId(externalFacingUser.getId());
		super.setCreationDate(new Date());
		super.setUpdationDate(new Date());
	}

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// TODO Auto-generated method stub
		return true;
	}
	
	/**
	 * @see BaseEntity.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		return this;
	}

	/**
	 * @see BaseEntity.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		return this;
	}

	//TODO - The session entity should not be sent back as it is just the entire session
	/**
	 * This returns a session entity as a JSON.
	 * @return A JSON for session
	 */
	public String getSessionEntity(){
		return this.toJSON();
	}
	
	public void setSessionEntity(String sessionEntity){
		
	}
	
	/**
	 * This methods returns the session id
	 * @return Returns a session id
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * This method sets a session id
	 * @param sessionId The session id 
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
