package com.boilerplate.sessions;

import java.io.Serializable;
import java.util.UUID;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.BaseEntity;
import com.boilerplate.java.entities.ExternalFacingUser;

/**
 * This class is used to implement a session.
 * It contains the session id along with a number of 
 * session objects
 * @author gaurav
 *
 */
public class Session extends BaseEntity implements Serializable{
	
	
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
		super.setId(UUID.randomUUID().toString());
		this.user = externalFacingUser;
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
}
