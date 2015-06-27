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
import com.boilerplate.database.implementations.MySQLConfigurations;
import com.boilerplate.database.interfaces.IConfigurations;
import com.boilerplate.exceptions.BaseBoilerplateException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.BaseEntity;
import com.boilerplate.java.entities.Configuration;
import com.boilerplate.java.entities.IdEntity;
import com.boilerplate.java.entities.Widget;
import com.boilerplate.service.interfaces.IWidgetService;
import com.boilerplate.sessions.Session;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This is the sample widget controller. This controller is used to do
 * CRUD operations on widgets
 * @author gaurav
 */
@Api(description="This api allows methods for widgets" ,value="Widget API's",basePath="/widget")
@Controller
public class WidgetController extends BaseController{
	Logger logger = Logger.getInstance(WidgetController.class);
	/**
	 * This is the autowired instance of the widget service.
	 * This service provides the bulk of business operations.
	 */
	@Autowired
	IWidgetService widgetService;

	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;
	
	@Autowired
	IConfigurations databaseConfiguration;
	
	/**
	 * This method is used to get a widget given its Id.
	 * The method is a GET method and returns a response as per content type.
	 */
	@ApiOperation(	value="Gets a widget with a given Id"
					,notes="The user must be authenticated to be able to access this api"
				 )
	@ApiResponses(value={
							@ApiResponse(code=200, message="Ok")
						,	@ApiResponse(code=404, message="Not Found")
						})
	@RequestMapping(value = "/widget/{id}", method = RequestMethod.GET)
	public @ResponseBody Widget getById(
			@ApiParam(value="This is the id of the widget to be fetched",required=true
			,name="id",allowMultiple=false)
			@PathVariable String id
			) throws BaseBoilerplateException{		
	 	BoilerplateList<BaseEntity> idEntities =
	 			new BoilerplateList<BaseEntity>();
	 	IdEntity idEntity = new IdEntity();
	 	idEntity.setId(id);
	 	idEntities.add(idEntity);
	 	BoilerplateList<Widget> baseEntityList = null;
	 	baseEntityList = widgetService.get(idEntities);
	 	Session s =(Session) super.getSession();
	 	return (Widget)baseEntityList.get(0);
	}

	@RequestMapping(value = "/widget/{id}", method = RequestMethod.DELETE)
	public @ResponseBody void deleteById(@PathVariable String id){
		System.out.println("Delete");
	}
}
