package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.java.collections.BoilerplateList;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * This is the user entity returned.
 * @author gaurav.verma.icloud
 *
 */
@ApiModel(value="A User", description="This is a user", parent=ExternalFacingUser.class)
public class ExternalFacingReturnedUser extends ExternalFacingUser implements Serializable{

	public ExternalFacingReturnedUser(){
		
	}
	
	public ExternalFacingReturnedUser(ExternalFacingUser user){
		super.setAuthenticationProvider(user.getAuthenticationProvider());
		super.setCreationDate(user.getCreationDate());
		super.setExternalSystemId(user.getExternalSystemId());
		super.setId(user.getId());
		super.setPassword(user.getPassword());
		super.setUpdationDate(user.getUpdationDate());
		super.setUserId(user.getUserId());
		super.setUserMetaData(user.getUserMetaData());
	}
	/**
	 * The roles of the user
	 */
	@ApiModelProperty(value="This roles of the user")
	private BoilerplateList<Role> roles;

	/**
	 * Gets the roles of the user
	 * @return The roles of the user
	 */
	public BoilerplateList<Role> getRoles() {
		return roles;
	}

	/**
	 * Sets the roles of the user
	 * @param roles The roles of the user
	 */
	public void setRoles(BoilerplateList<Role> roles) {
		this.roles = roles;
	}
	
}
