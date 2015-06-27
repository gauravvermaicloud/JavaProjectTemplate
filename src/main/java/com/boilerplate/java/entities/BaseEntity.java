package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.Date;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.Base;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * This is the base entity of the system. All entities in the 
 * system must be derived from this. All entities are expected to be
 * serializable
 * @author gaurav
 */
public  abstract class BaseEntity extends Base implements Serializable{
	@ApiModelProperty(value="This is the id of the entity"
			,required=true,notes="The id is a String")
	/**
	 * This is the id of the entity.
	 */
	private String id;
	
	/**
	 * Gets the creation date of the entity.
	 * @return The creation date.
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * Sets the creation date of the entity.
	 * @param creationDate The creation date.
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Gets the last updation date of the entity.
	 * @return The updation date.
	 */
	public Date getUpdationDate() {
		return updationDate;
	}
	
	/**
	 * Sets the updation date of the entity.
	 * @param updationDate The updation date.
	 */
	public void setUpdationDate(Date updationDate) {
		this.updationDate = updationDate;
	}

	@ApiModelProperty(value="This is the creation date of the entity"
			,required=true)
	/**
	 * The creation date of the entity.
	 */
	private Date creationDate;
	
	@ApiModelProperty(value="This is the last update date of the entity"
			,required=true,notes="This is always greater than or equal to the creation date")
	/**
	 * The updation date of the entity.
	 */
	private Date updationDate;
	
	/**
	 * Gets the id of the entity.
	 * @return The id of the entity
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id of the entity. 
	 * @param id The id of the entity
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * This validates the entity
	 * @return
	 */
	public abstract boolean validate() throws ValidationFailedException;

	/**
	 * Transforms an entity into an internal entity which will
	 * be used by service layer
	 * @return An internal entity
	 */
	public abstract BaseEntity transformToInternal();
	

	/**
	 * Transforms an entity into an external entity which will
	 * be used by Controller layer
	 * @return An external entity
	 */
	public abstract BaseEntity transformToExternal();

	/**
	 * Checks if a string is null or empty
	 * @param string The string to be checked
	 * @return true if the string is null or empty else false
	 */
	public static boolean isNullOrEmpty(String string){
		return string !=null && string.isEmpty();
	}
}


