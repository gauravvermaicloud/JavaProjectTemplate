package com.boilerplate.database.interfaces;

import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.entities.ExternalFacingUser;

/**
 * This has interfaces for user management
 * @author gaurav
 *
 */
public interface IUser {
	
	/**
	 * This method creates a user in the database.
	 * @param user This is the user to be created.
	 * @throws ConflictException This is thrown if the user already exists in the database for
	 * the given provider.
	 * @return The created user.
	 */
	public ExternalFacingUser create(ExternalFacingUser user) throws ConflictException;

	/**
	 * This method updates a user in the database.
	 * @param user This is the user to be created.
	 * @throws ConflictException This is thrown if there is a DB constraint violation
	 * @return The updated user.
	 */
	public ExternalFacingUser update(ExternalFacingUser user) throws ConflictException;

	/**
	 * This method is used to get a user from database given the userId
	 * @param userId
	 * @return an instance of the user.
	 * @throws NotFoundException if the user doesnt exist
	 */
	public ExternalFacingUser getUser(String userId) throws NotFoundException ;

	/**
	 * This method deletes the given user
	 * @param user The user to be deleted
	 */
	public void deleteUser(ExternalFacingUser user); 
	
}
