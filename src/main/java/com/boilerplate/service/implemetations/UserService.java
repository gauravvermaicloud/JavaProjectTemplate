package com.boilerplate.service.implemetations;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.cache.CacheFactory;
import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Encryption;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.Constants;
import com.boilerplate.java.entities.AuthenticationRequest;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.service.interfaces.IUserService;
import com.boilerplate.sessions.Session;
import com.boilerplate.sessions.SessionManager;

public class UserService implements IUserService {


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
	 * The autowired instance of session manager
	 */
	@Autowired
	com.boilerplate.sessions.SessionManager sessionManager;
	
	/**
	 * This sets the session manager
	 * @param sessionManager The session manager
	 */
	public void setSessionManager(com.boilerplate.sessions.SessionManager sessionManager){
		this.sessionManager = sessionManager;
	}

	/**
	 * The autowired instance of user data access
	 */
	@Autowired
	IUser userDataAccess;
	
	/**
	 * This is the setter for user data acess
	 * @param iUser
	 */
	public void setUserDataAccess(IUser iUser){
		this.userDataAccess = iUser;
	}
	
	/**
	 * @see IUserService.create
	 */
	@Override
	public ExternalFacingUser create(ExternalFacingUser externalFacingUser) throws ValidationFailedException,
		ConflictException{
		
		externalFacingUser.validate();
		
		if(externalFacingUser.getAuthenticationProvider() ==null){
			externalFacingUser.setAuthenticationProvider(
					this.configurationManager.get("DefaultAuthenticationProvider"));
		}
		//set external system id if the authentication provider is default
		if(externalFacingUser.getAuthenticationProvider().equalsIgnoreCase(
				this.configurationManager.get("DefaultAuthenticationProvider"))){
			externalFacingUser.setExternalSystemId(externalFacingUser.getUserId());
		}
		
		externalFacingUser.setUserId(externalFacingUser.getAuthenticationProvider()
				+":"+externalFacingUser.getUserId());
		//before save lets hash the password
		externalFacingUser.hashPassword();
		//set create and update date
		externalFacingUser.setCreationDate(new Date());
		externalFacingUser.setUpdationDate(externalFacingUser.getCreationDate());
		//call the database to save the user
		externalFacingUser =  (ExternalFacingUser) userDataAccess.create(externalFacingUser).transformToExternal();
		//we dont want to share the hash hence sending bacj the text
		externalFacingUser.setPassword("Password Encrypted");
		return externalFacingUser;
	}

	/** 
	 * @see IUserService.authenticate
	 */
	@Override
	public Session authenticate(AuthenticationRequest authenitcationRequest) throws UnauthorizedException{
		
		//check if the user starts with DEFAULT:, if not then put in Default: before it
		if(!authenitcationRequest.getUserId().startsWith(
					this.configurationManager.get("DefaultAuthenticationProvider").toUpperCase()+":")){
			authenitcationRequest.setUserId(
					this.configurationManager.get("DefaultAuthenticationProvider")+":"+
							authenitcationRequest.getUserId().toUpperCase()
					);
		}
		ExternalFacingUser user =null;
		//Call the database and check if the user is
		//we store everything in upper case hence chanhing it to upper
		try{
			user = userDataAccess.getUser(
					authenitcationRequest.getUserId().toUpperCase());
		String hashedPassword = String.valueOf(Encryption.getHashCode(authenitcationRequest.getPassword()));
		if(!user.getPassword().equals(hashedPassword)){
			throw new UnauthorizedException("USER",
					"User name or password incorrect", null);
		}
		user.setPassword("Password Encrypted");
		
		//getthe roles, ACL and ther details of this user
		
		//if the user is valid create a new session, in the session add details 
		Session session = sessionManager.createNewSession(user);
		
		return session;
		}
		catch(NotFoundException nfe){
			logger.logException("UserService", "authenticate", "External Facing User not found"
					, "Converting this exception to Unauthorized for security", nfe);
			throw new UnauthorizedException("USER",
					"User name or password incorrect", null);
		}
	}
}
