package com.boilerplate.java.controllers;

import java.net.UnknownHostException;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.configurations.IConfiguratonManager;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.Ping;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Api(value="Diagnostic API's"
	,basePath="/health",description="This has api's to check and diagnose"
			+ " the health of the system")
@Controller
public class HealthController extends BaseController{

	/**
	 * This is the instance of configuration manager
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;
	
	/**
	 * This is the instance of ping service
	 */
	@Autowired
	com.boilerplate.service.interfaces.IPingService iPingService;
	
	/**
	 * This method is used to ping a server. This method checks all the major components
	 * like cache, queue, db etc and sends back a status for the machine
	 * @return A response to ping which tells overall status, name of machine
	 * and details of each connecting server.
	 * @throws UnknownHostException 
	 */
	@ApiOperation(	value="Returns "
			+ "An overall status of the server health and the abillity to connect "
			+ "to other servers."
		 )
	@ApiResponses(value={
							@ApiResponse(code=200, message="Ok")
						,	@ApiResponse(code=404, message=
						"If one of the ciritical servers is not rechable")
					})
	@RequestMapping(value = "/health/ping", method = RequestMethod.GET)
		public @ResponseBody Ping ping() throws UnknownHostException{
		Ping ping =  new Ping();
		iPingService.setPingStatus(ping);
		if(ping.getOverallStatus() == false){
			super.getHttpServletResponse().setStatus(HttpStatus.SC_NOT_FOUND);
		}
		return ping;
	}
	
	//Method for diagnosing all systems
	
	//Method for Sniff of all api's
	
	//Method to reset cache
	
	//Method to reset queue
	
	//Method to disable queue
	
	//Method to disable cache
	
	//Method to disable background jobs
	
	//Method to enable background jobs
	
	
	// if we do user defined roles then a mechanism to refresh the server
	
	/**
	 * This method lists entire configuration for the current system
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(	value="Lists all configuration values. In production this api"
			+ "should be turned off or only be allowed from local access"
		 )
	@ApiResponses(value={
						@ApiResponse(code=200, message="Ok")
					})
	@RequestMapping(value = "/health/configuration", method = RequestMethod.GET)
	public @ResponseBody BoilerplateMap<String,String> getConfigurations() throws Exception{
		//return the config values as found
		return this.configurationManager.getConfigurations();
	}
}
