package com.boilerplate.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.Constants;
import com.boilerplate.sessions.Session;
import com.boilerplate.sessions.SessionManager;
import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.configurations.IConfiguratonManager;

/**
 * This class is used to log and trace an exception.
 * Any controller method when called will be intercepted and logged 
 * If an exception takes place then it will be handled and a web error
 * is sent back 
 * @author gaurav
 */
//This code is an aspect
@Aspect
@Component
public class LogAndTraceExceptionAspect {

    /**
    * This is an instance of the configuration manager
    */
    @Autowired
    com.boilerplate.configurations.ConfigurationManager configurationManager;
    

	/**
	 * This is the logger
	 */
	private Logger logger = Logger.getInstance(LogAndTraceExceptionAspect.class);
	
	/**
	 * This method logs every entry to the controller. It logs
	 * class and method name, arguments and return value if any
	 * @param proceedingJoinPoint The join point of the method
	 * @throws Throwable A Http error for the business case.
	 */
    @Around("execution(public* com.boilerplate.java.controllers.*.*(..))") 
    public Object logTraceAndHandleException(ProceedingJoinPoint proceedingJoinPoint
    		) throws Throwable {
    		
    	try{
   			
       		//TODO -find the session
    		
    		//Check if the user can execute the method from configuration from a map of method and role needed to execute
    		
    		//if the user is in the role or the role required is * then continue else throw an unautjorized exception
    		
    		//Get the return value
    		Object returnValue  = proceedingJoinPoint.proceed();
    		//TODO - Put if enabled,config manager should have a set of properties for common and 
    		//system defined values
    		//log the details including class, method, input arguments and return
            if(Boolean.parseBoolean(configurationManager.get(Constants.IsDebugEnabled))){ //TODO - This should not be parsed everytime
	    		logger.logTraceExit(
	    				proceedingJoinPoint.getSignature().getDeclaringTypeName(),
	    				proceedingJoinPoint.getSignature().toLongString(),
	    				proceedingJoinPoint.getArgs(),
	    				returnValue
	    				);   
            }
            else{
            	logger.logTraceExit(
	    				proceedingJoinPoint.getSignature().getDeclaringTypeName(),
	    				proceedingJoinPoint.getSignature().toLongString(),
	    				new String[]{"Not_Printed"},
	    				"Not_Printed"
	    				);  
            }
    		//send the return value back
    		return returnValue;
    	}
    	catch(Throwable th){
    		//TODO - Put if enabled 
    		logger.logTraceExitException(proceedingJoinPoint.getSignature().getDeclaringTypeName(),
    				proceedingJoinPoint.getSignature().getName(),
    				proceedingJoinPoint.getArgs(),
    				th
    				);
    		throw th;
    	}
    	
    	
    }
}
