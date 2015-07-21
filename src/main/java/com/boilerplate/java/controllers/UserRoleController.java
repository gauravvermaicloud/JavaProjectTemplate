package com.boilerplate.java.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiParam;

@Api(description="This api has controllers for User-Role CRUD operations"
,value="User Role API's",basePath="/user/{userId}/role")
@Controller
public class UserRoleController {

	/**
	 * Adds a user to given set of roles
	 */
	@RequestMapping(value = "/user/{userId}/role", method = RequestMethod.GET)
	public  @ResponseBody void addUserToRole(
			@ApiParam(value="This is the id of the user to which roles are to be added"
			,required=true,name="userId",allowMultiple=false)@PathVariable String userId
			,@RequestBody(required=true) List<String>roles){
		
	}
	
	/**
	 * Removes user from role
	 */
	public void removeUserRole(){
		
	}
	
}
