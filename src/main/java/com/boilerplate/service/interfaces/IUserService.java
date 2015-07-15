package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.AuthenticationRequest;
import com.boilerplate.java.entities.ExternalFacingUser;
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
	 * @return A user entity
	 * @throws NotFoundException If no user is found with given details
	 */
	public ExternalFacingUser get(String userId) throws NotFoundException;
}
