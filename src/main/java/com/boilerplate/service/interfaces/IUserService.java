package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.AuthenticationRequest;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.UpdateUserEntity;
import com.boilerplate.sessions.Session;

public interface IUserService {
	/**
	 * This method is used to create an external facing user.
	 * @param externalFacingUser The instance of the user to be created.
	 * @throws ValidationFailedException If the user name or password are empty
	 * @throws ConflictException This is thrown if the user already exists in the database
	 * for the given provider
	 * @return a user which has been created
	 */
	public ExternalFacingUser create(ExternalFacingUser externalFacingUser) 
			throws ValidationFailedException, ConflictException;
	
	/**
	 * This method takes an authentication request and generates a session 
	 * if the user is authenticated
	 * @param authenitcationRequest The authentication request
	 * @return A session object
	 * @throws UnauthorizedException if the user name password combination is incorrect
	 */
	public Session authenticate(AuthenticationRequest authenitcationRequest) throws UnauthorizedException;

	/**
	 * This method returns the user with given user id.
	 * @param userId The id of the user in format provider:userid, if no provider is
	 * specified it is defaulted to default
	 * @return A user id
	 * @throws NotFoundException If no user is found with given details
	 */
	public ExternalFacingReturnedUser get(String userId) throws NotFoundException;
	
	/**
	 * This method deletes the user with given user id.
	 * @param userId The id of the user in format provider:userid, if no provider is
	 * specified it is defaulted to default
	 * @return A user id
	 * @throws NotFoundException If no user is found with given details
	 */
	public void delete(String userId)throws NotFoundException;

	/**
	 * This method marks a user for deletion. As user deltion
	 * can be a lenthy process this uses queue to do the same in back ground
	 * @param userId The id of the user in format provider:userid, if no provider is
	 * specified it is defaulted to default
	 * @return A user id
	 * @throws ValidationFailedException If any validation fails
	 * @throws ConflictException If there is a conflict in updating entity due to a DB constraint
	 * @throws NotFoundException If no user is found with given details
	 */
	public void markUserForDeletion(String userId) throws NotFoundException,
	ValidationFailedException, ConflictException;
	
	/**
	 * This method is used to update a user.
	 * @param userId The id of the user to be updated
	 * @param updateUserEntity The entity of the user.
	 * @return The updated user entity
	 * @throws ValidationFailedException If any validation fails
	 * @throws ConflictException If there is a conflict in updating entity due to a DB constraint
	 * @throws NotFoundException If the user is not found.
	 */
	public ExternalFacingReturnedUser update(String userId,UpdateUserEntity updateUserEntity)throws 
		ValidationFailedException, ConflictException,NotFoundException;
	
	/**
	 * Checks if the user Id starts with a authentication provider or not, 
	 * if the user id doesnt then DEFAULT: is appended
	 * @param userId The id of the user
	 * @return user id in upper case with proper provider appended if needed.
	 */
	public  String normalizeUserId(String userId);
}
