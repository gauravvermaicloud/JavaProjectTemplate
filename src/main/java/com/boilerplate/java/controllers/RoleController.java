package com.boilerplate.java.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.configurations.IConfiguratonManager;
import com.boilerplate.database.interfaces.IConfigurations;
import com.boilerplate.database.mysql.implementations.MySQLConfigurations;
import com.boilerplate.exceptions.BaseBoilerplateException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.BaseEntity;
import com.boilerplate.java.entities.Configuration;
import com.boilerplate.java.entities.GenericListEncapsulationEntity;
import com.boilerplate.java.entities.IdEntity;
import com.boilerplate.java.entities.Role;
import com.boilerplate.java.entities.Widget;
import com.boilerplate.service.interfaces.IWidgetService;
import com.boilerplate.sessions.Session;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 *This class implements the role controller
 * @author gaurav
 */
@Api(description="This api allows methods for roles" ,value="Role API's",basePath="/role")
@Controller
public class RoleController extends BaseController{
	Logger logger = Logger.getInstance(RoleController.class);

	@Autowired
	com.boilerplate.service.interfaces.IRoleService roleService;
	
	/**
	 * This method gets all the roles
	 * @return a list of roles
	 */
	@ApiOperation(	value="Gets all the roles in the system"
				 )
	@ApiResponses(value={
							@ApiResponse(code=200, message="Ok")
						,	@ApiResponse(code=404, message="Not Found")
						})
	@RequestMapping(value = "/role", method = RequestMethod.GET)
	public @ResponseBody GenericListEncapsulationEntity<Role> get() throws BaseBoilerplateException{		
		return roleService.getRoles();
	}
	
	/**
	 * This method reloads all the roles from the database
	 * @return a list of roles
	 */
	@ApiOperation(	value="Reloads the roles from the database"
				 )
	@ApiResponses(value={
							@ApiResponse(code=200, message="Ok")
						})
	@RequestMapping(value = "/role", method = RequestMethod.GET)
	public @ResponseBody void reloadRoles() {		
		roleService.reloadRoles();
	}
	
	

}
