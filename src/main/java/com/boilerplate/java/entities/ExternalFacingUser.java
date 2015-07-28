package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Encryption;
import com.boilerplate.java.collections.BoilerplateMap;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * This is the user entity expcted as an input.
 * @author gaurav.verma.icloud
 *
 */
@ApiModel(value="A User", description="This is a user", parent=UpdateUserEntity.class)
public class ExternalFacingUser extends UpdateUserEntity implements Serializable{
	
	@ApiModelProperty(value="This is the id of the user."
			,required=true,notes="The id of the user is unique in the system, it is analogous to user name"
			)
	/**
	 * This is the user's Id, this is not the system generated Id, it is the id created
	 * by the user.
	 */
	private String userId;

	@ApiModelProperty(value="This is the authenication provider of the user. This value is set to Default if not specified"
			,required=true,notes="The legal values include Default")
	/**
	 * This is the authentication provider. Default means that the user is authenticated by the 
	 * user name and password. A user may use SSO and other authentication providers like facebook,\
	 * google and others.
	 */
	private String authenticationProvider;
	
	@ApiModelProperty(value="This is the id as in external system, "
			+ "if the provider is Default then id and external system id are same"
			,required=true)
	/**
	 * This is the id of the user in external system, 
	 * it is defaulted to the id if the authentication provider
	 * is Default
	 */
	private String externalSystemId;
	
	

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
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		super.validate();
		//The idea is that user name or password should not be null
		if(this.isNullOrEmpty(this.getUserId())) throw new ValidationFailedException(
				"User","UserId is null/Empty",null);
		if(this.getUserId() == null) throw new ValidationFailedException(
				"User","UserId is null/Empty",null);
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
