package com.boilerplate.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.Constants;
import com.boilerplate.sessions.Session;
import com.boilerplate.sessions.SessionManager;
import com.boilerplate.asyncWork.AsyncWorkItem;
import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.configurations.IConfiguratonManager;

/**
 * This class is used to log and trace an exception for all observers.
 * No observer is expected to do any excpetion handeling
 * @author gaurav
 */
//This code is an aspect
@Aspect
@Component
public class LogAndTraceObserversAspect {


	/**
	 * This is the logger
	 */
	private Logger logger = Logger.getInstance(LogAndTraceObserversAspect.class);
	
	/**
	 * This method logs every entry to the observer. It logs
	 * class and method name, arguments and return value if any
	 * @param proceedingJoinPoint The join point of the method
	 */

   @Around("execution(public* com.boilerplate.asyncWork.*Observer.*(..))") //TODO - why is this not working
	public Object logTraceAndHandleException(ProceedingJoinPoint proceedingJoinPoint)  {
    	Object returnValue =null;
    	try{
    		returnValue  = proceedingJoinPoint.proceed();
            
	    		logger.logTraceExit(
	    				proceedingJoinPoint.getSignature().getDeclaringTypeName(),
	    				proceedingJoinPoint.getSignature().toLongString(),
	    				proceedingJoinPoint.getArgs(),
	    				returnValue
	    				);   
    		//send the return value back
    		return returnValue;
    	}
    	catch(Throwable th){
    		logger.logTraceExitException(proceedingJoinPoint.getSignature().getDeclaringTypeName(),
    				proceedingJoinPoint.getSignature().getName(),
    				proceedingJoinPoint.getArgs(),
    				th
    				);
    		//an exception in observer needs to be logged and consumed here
    	}
    	return returnValue;
    }
}
