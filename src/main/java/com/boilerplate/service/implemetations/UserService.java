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
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.AuthenticationRequest;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.UpdateUserEntity;
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
	 * This is an instance of the queue job, to save the session
	 * back on to the database async
	 */
	@Autowired
	com.boilerplate.jobs.QueueReaderJob queueReaderJob;
	
	/**
	 * This sets the queue reader jon
	 * @param queueReaderJob The queue reader jon
	 */
	public void setQueueReaderJob(com.boilerplate.jobs.QueueReaderJob queueReaderJob){
		this.queueReaderJob = queueReaderJob;
	}
	
	BoilerplateList<String> subjects = new BoilerplateList();
	
	/**
	 * Initializes the bean
	 */
	public void initilize(){
		subjects.add("DeleteUser");
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

	/**
	 * @see IUserService.get
	 */
	@Override
	public ExternalFacingUser get(String userId) throws NotFoundException {
		//retrun the user with password as a string
		return get(userId,true);
		
	}

	/**
	 * This method returns a user with given id
	 * @param userId The id of the user
	 * @param encryptPasswordString True if the user password to be 
	 * encryptied into a message string
	 * false if the password is to be sent as is
	 * @return The user entity
	 * @throws NotFoundException If the user is not found
	 */
	public ExternalFacingUser get(String userId
			, boolean encryptPasswordString) throws NotFoundException {
		//convert user names to upper
		userId = userId.toUpperCase();
		
		//if no provider is given then add provider DEFAULT:
		if(!userId.startsWith(this.configurationManager.get(
				"DefaultAuthenticationProvider").toUpperCase()+":")){
			userId = this.configurationManager.get(
					"DefaultAuthenticationProvider").toUpperCase()+":"+userId;
		}
		//get the user from database
		ExternalFacingUser externalFacingUser = this.userDataAccess.getUser(userId);
		//if no user with given id was found then throw exception
		if(externalFacingUser == null) throw new NotFoundException("ExternalFacingUser"
				, "User with id "+userId+" doesnt exist", null);
		//set the password as encrypted
		if(encryptPasswordString){
			externalFacingUser.setPassword("Password Encrypted");
		}
		//return the user
		return externalFacingUser;
		
	} 
	/**
	 * @see IUserService.delete
	 */
	@Override
	public void delete(String userId) throws NotFoundException {
		ExternalFacingUser user = this.get(userId);
		this.userDataAccess.deleteUser(user);
	}
	
	
	/**
	 * @see IUserService.update
	 */
	@Override
	public ExternalFacingUser update(String userId, UpdateUserEntity updateUserEntity)
			throws ValidationFailedException, ConflictException,
			NotFoundException {
		//check if the user exists, if so get it
		ExternalFacingUser user = this.get(userId,false);
		//Update the user items from the incomming entity
		if(updateUserEntity.getPassword() !=null){
			if(updateUserEntity.getPassword().equals("") == false)
			{
				user.setPassword(updateUserEntity.getPassword());
				//and hash the password
				user.hashPassword();
			}
		}
		//for each key updte the metadata
		for(String key : updateUserEntity.getUserMetaData().keySet()){
			user.getUserMetaData().put(key, updateUserEntity.getUserMetaData().get(key));
		}
		
		user.setUpdationDate(new Date());
		user.setUserMetaData(updateUserEntity.getUserMetaData());
		//validate the entity
		user.validate();
		//update the user in the database
		this.userDataAccess.update(user);
		return this.get(user.getUserId());
	}

	/**
	 * @see IUserService.markUserForDeletion
	 */
	@Override
	public void markUserForDeletion(String userId) throws NotFoundException
		, ValidationFailedException, ConflictException {
		ExternalFacingUser user = this.get(userId);
		user.setPassword("0");//set password to 0 as it cant be hash of anything
		this.update(userId, user);
		try{
			queueReaderJob.requestBackroundWorkItem(userId, subjects
					, "UserService", "markUserForDeletion");
		}
		catch(Exception ex){
			//incase we cant put this on queue delete it now
			this.delete(userId);
		}
	}
}
