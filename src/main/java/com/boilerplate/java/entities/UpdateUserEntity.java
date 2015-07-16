package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Encryption;
import com.boilerplate.java.collections.BoilerplateMap;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="A User", description="This is a user entity for update", parent=BaseEntity.class)
public class UpdateUserEntity extends BaseEntity{
	@ApiModelProperty(value="This contains the list of properties to extend the user model"
			,required=true,notes="The keys should be unique in this system.")
	/**
	 * This is the dictionary of the user meta data.
	 */
	private BoilerplateMap<String,String> userMetaData = new BoilerplateMap<String, String>(); 
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
		if(this.getPassword() ==null) throw new ValidationFailedException(
				"User","Password is null/Empty",null);
		if(this.getPassword().equals("")) throw new ValidationFailedException(
				"User","Password is null/Empty",null);
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
