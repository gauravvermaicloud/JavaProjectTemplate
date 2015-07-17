package com.boilerplate.asyncWork;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.Logger;

/**
 * This observer deletes users. User deletion is a long process 
 * hence it is done async
 * @author gaurav.verma.icloud
 *
 */
public class DeleteUserObserver implements IAsyncWorkObserver{

	/**
	 * This is the logger
	 */
	Logger logger = Logger.getInstance(DeleteUserObserver.class);
	
	/**
	 * This is the user service
	 */
	@Autowired
	com.boilerplate.service.implemetations.UserService userService;
	/**
	 * This method sets the user service
	 * @param userService This method sets user service
	 */
	public void setUserService(com.boilerplate.service.implemetations.UserService 
			userService){
		this.userService=userService;
	}
	
	/**
	 * This method deletes a user asynchronously
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) {
		try {
			userService.delete((String)asyncWorkItem.getPayload());
		} catch (Exception ex) {
			logger.logException("DeleteUserObserver", "observe", "catchBlock", ex.toString(),ex);
		}
		
	}

}
