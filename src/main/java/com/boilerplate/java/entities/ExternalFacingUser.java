package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Encryption;
import com.boilerplate.java.collections.BoilerplateMap;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class ExternalFacingUser extends BaseEntity implements Serializable{
	
	@ApiModelProperty(value="This is the id of the user."
			,required=true,notes="The id of the user is unique in the system, it is analogous to user name")
	/**
	 * This is the user's Id, this is not the system generated Id, it is the id created
	 * by the user.
	 */
	private String userId;

	@ApiModelProperty(value="This is the authenication provider of the user."
			,required=true,notes="The legal values include Default")
	/**
	 * This is the authentication provider. Default means that the user is authenticated by the 
	 * user name and password. A user may use SSO and other authentication providers like facebook,\
	 * google and others.
	 */
	private String authenticationProvider;
	
	@ApiModelProperty(value="This is the id as in external system, if the provider is Default then id and external system id are same"
			,required=true)
	/**
	 * This is the id of the user in external system, it is defaulted to the id if the authentication provider
	 * is Default
	 */
	private String externalSystemId;
	
	
	@ApiModelProperty(value="This contains the list of properties to extend the user model"
			,required=true,notes="The keys should be unique in this system.")
	/**
	 * This is the dictionary of the user meta data.
	 */
	private BoilerplateMap<String,String> userMetaData = new BoilerplateMap<String, String>(); 

	/**
	 * Gets the user Id
	 * @return The user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user id
	 * @param userId The user id
	 */
	public void setUserId(String userId) {
		this.userId = userId.toUpperCase();
	}
	
	/**
	 * This returns the autheinctaion provider.
	 * @return The authentication provider.
	 */
	public String getAuthenticationProvider() {
		return authenticationProvider;
	}

	/**
	 * This sets the authentication provider
	 * @param The authenticationProvider
	 */
	public void setAuthenticationProvider(String authenticationProvider) {
		this.authenticationProvider = authenticationProvider.toUpperCase();
	}

	/**
	 * returns the external system id
	 * @return The external system id
	 */
	public String getExternalSystemId() {
		return externalSystemId;
	}

	/**
	 * Theis sets the external system id
	 * @param externalSystemId
	 */
	public void setExternalSystemId(String externalSystemId) {
		this.externalSystemId = externalSystemId;
	}
	
	/**
	 * This method returns the user meta data
	 * @return
	 */
	public BoilerplateMap<String, String> getUserMetaData() {
		return userMetaData;
	}

	public void setUserMetaData(BoilerplateMap<String, String> userMetaData) {
		this.userMetaData = userMetaData;
	}

	@ApiModelProperty(value="This is the password of the user."
			,required=true,notes="This is not empty.")
	/**
	 * This is the password.
	 */
	private String password;
	/**
	 * This method gets the password
	 * @return The password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * This method sets the password
	 * @param password The password
	 */
	public void setPassword(String password){
		this.password= password;
	}
	
	/**
	 * This method hash's the password
	 */
	public void hashPassword(){
		this.password = String.valueOf(Encryption.getHashCode(this.password));
	}
	
	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		//The idea is that user name or password should not be null
		if(this.getUserId() == null) throw new ValidationFailedException("User","UserId is null/Empty",null);
		if(this.getPassword() ==null) throw new ValidationFailedException("User","Password is null/Empty",null);
		if(super.isNullOrEmpty(this.getUserId())) throw new ValidationFailedException("User","UserId is null/Empty",null);
		if(this.getPassword().equals("")) throw new ValidationFailedException("User","Password is null/Empty",null);
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
