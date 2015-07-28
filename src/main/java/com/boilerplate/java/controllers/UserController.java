package com.boilerplate.java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.Constants;
import com.boilerplate.java.entities.AuthenticationRequest;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.UpdateUserEntity;
import com.boilerplate.service.interfaces.IUserService;
import com.boilerplate.sessions.Session;
import com.boilerplate.sessions.SessionManager;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This class has methods to operate on users. These inlcude
 * Creation, update and get user along with authenticate operation
 * @author gaurav
 *
 */
@Api(description="This api has controllers for User CRUD operations" ,value="User API's",basePath="/user")
@Controller
public class UserController extends BaseController{

	/**
	 * This is the instance of the user service
	 */
	@Autowired
	IUserService userService;
	
	@Autowired
	SessionManager sessionManager;
	
	/**
	 * This is the instance of configuration manager
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;
	
	/**
	 * This method is used to create a user.
	 * @externalFacingUser This is the user to be created.
	 * @throws ValidationFailedException This exception is thrown if the user name or password is blank
	 * @throws ConflictException This exception is thrown if the user already exists in the system
	 */
	@ApiOperation(	value="Creates a new User entity in the system"
					,notes="The user is unique in the system, The creation date and updated "
							+ "date are automatically filled."
				 )
	@ApiResponses(value={
							@ApiResponse(code=200, message="Ok")
						,	@ApiResponse(code=404, message="Not Found")
						,	@ApiResponse(code=400, message="Bad request, User name or password is empty")
						,	@ApiResponse(code=409, message="The user already exists in the system for the provider")	
						})
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public @ResponseBody ExternalFacingUser createUser(@RequestBody ExternalFacingUser externalFacingUser)
			throws ValidationFailedException,ConflictException{
		//call the business layer
		return userService.create(externalFacingUser);
	}
	
	/**
	 * This method authenticates a user with a given user name and password
	 * @param authenticationRequest The request for authenticate
	 * @return This returns a session entity of the user. This method also
	 * sets a header and cookie for session id.
	 * @throws UnauthorizedException This excption is thrown if the user name password
	 * is incorrect
	 */
	@ApiOperation(	value="Authenticates a user by passing user name, password for default provider"
			,notes="The user is unique in the system for a given provider."
					+ "The user may passed as a user id or as DEFAULT:userId"
		 )
@ApiResponses(value={
					@ApiResponse(code=200, message="Ok")
				,	@ApiResponse(code=404, message="Not Found")
				})
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public @ResponseBody Session authenticate(@RequestBody AuthenticationRequest 
			authenticationRequest) throws UnauthorizedException{
	
		//Call authentication service to check if user name and password are valid
		Session session = this.userService.authenticate(authenticationRequest);
		
		//now put sessionId in a cookie, header and also as response back
		super.addHeader(Constants.AuthTokenHeaderKey, session.getSessionId());
				
		super.addCookie(Constants.AuthTokenCookieKey, session.getSessionId()
				,sessionManager.getSessionTimeout());
		
		//add the user id for logging, we have to explictly do it here only in this case all other cases
		//are handeled by HttpRequestInterseptor
		super.addHeader(Constants.X_User_Id, session.getExternalFacingUser().getUserId());
		RequestThreadLocal.setRequest(
				RequestThreadLocal.getRequestId(),
				RequestThreadLocal.getHttpRequest(), 
				RequestThreadLocal.getHttpResponse(),
				session); 
		return session;
	}
	
	/**
	 * This method returns a user with given id. The id is expected to be in form
	 * Provider:userId. If no provider is specified then it is defaulted to DEFAULT
	 * @param userId This is the id of the user in the format Provider:user id
	 * @return The user
	 * @throws NotFoundException If user is not found
	 */
	@ApiOperation(	value="This api retruns a user with given id. The id is expected to be"
			+ "in form Provider:userId, if no provider is specified then provider is "
			+ "defaulted to DEFAULT"
		 )
	@ApiResponses(value={
						@ApiResponse(code=200, message="Ok")
					,	@ApiResponse(code=404, message="Not Found")
					})
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
	public @ResponseBody ExternalFacingReturnedUser getById(
			@ApiParam(value="This is the id of the user to be fetched",required=true
			,name="userId",allowMultiple=false)@PathVariable String userId) throws NotFoundException{
		return this.userService.get(userId);
	}
	
	/**
	 * This method updates a user with given id. The id is expected to be in form
	 * Provider:userId. If no provider is specified then it is defaulted to DEFAULT.
	 * If the password is set to blank then it is not updated.
	 * @param userId This is the id of the user in the format Provider:user id
	 * @param updateUserEntity The user entity to be updated. 
	 * @return The user
	 * @throws NotFoundException If user is not found
	 */
	@ApiOperation(	value="This api updates a user with given id. The id is expected to be"
			+ "in form Provider:userId, if no provider is specified then provider is "
			+ "defaulted to DEFAULT. The metadata is replaced as is. If the password is set to blank"
			+ "then it is not updated"
		 )
	@ApiResponses(value={
						@ApiResponse(code=200, message="Ok")
					,	@ApiResponse(code=404, message="Not Found")
					})
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.PUT)
	public @ResponseBody ExternalFacingReturnedUser update(
			@ApiParam(value="This is the id of the user to be updated",required=true
			,name="userId",allowMultiple=false)@PathVariable String userId,
			@RequestBody UpdateUserEntity updateUserEntity)
					throws ValidationFailedException,ConflictException,NotFoundException{
		return this.userService.update(userId,updateUserEntity);
	}
	
	/**
	 * This method deletes a user with given id. The id is expected to be in form
	 * Provider:userId. If no provider is specified then it is defaulted to DEFAULT.
	 * This API doesnt delete user, it marks user for deletion as deletion is an 
	 * expensive operation.
	 * @param userId This is the id of the user in the format Provider:user id
	 * @throws NotFoundException If user is not found
	 * @throws ConflictException If there is a conflict in marking user for deletion
	 * @throws ValidationFailedException If any validation falis
	 */
	@ApiOperation(	value="This api deletes a user with given id. The id is expected to be"
			+ "in form Provider:userId, if no provider is specified then provider is "
			+ "defaulted to DEFAULT." 
			,notes="The deletion happens async"
		 )
	@ApiResponses(value={
						@ApiResponse(code=200, message="Ok")
					,	@ApiResponse(code=404, message="Not Found")
					})
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.DELETE)
	public @ResponseBody void delete(
			@ApiParam(value="This is the id of the user to be updated",required=true
			,name="userId",allowMultiple=false)@PathVariable String userId)
					throws NotFoundException, ValidationFailedException, ConflictException{
		this.userService.markUserForDeletion(userId);
	}
	
	
}
