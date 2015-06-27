package com.boilerplate.framework;

import javax.servlet.http.HttpServletRequest;

/**
 * This class is used to set and unset a request id on thread
 * @author gaurav
 *
 */
public class RequestThreadLocal {
	
	/**
	 * This is the instance of thread local
	 */
	public static final ThreadLocal<RequestParameters> threadLocal
		= new ThreadLocal<RequestParameters>();
	
	/**
	 * This sets the request id on thread
	 * @param requestId The request id
	 * @param httpServletRequest The http request
	 */
	public static void setRequest(String requestId,HttpServletRequest httpServletRequest){
		RequestParameters requestParameters = new RequestParameters();
		requestParameters.setRequestId(requestId);
		requestParameters.setHttpServletRequest(httpServletRequest);
		RequestThreadLocal.threadLocal.set(requestParameters);
	}
	
	/**
	 * This gets the request id on thread
	 * @return The request id
	 */
	public static String getRequestId(){
		if(RequestThreadLocal.threadLocal.get() != null){ 
			return RequestThreadLocal.threadLocal.get().getRequestId();
		}
		else{
			return null;
		}
	}
	
	/**
	 * This method returns the Http request 
	 * @return The Http request
	 */
	public static HttpServletRequest getHttpRequest(){
		if(RequestThreadLocal.threadLocal.get() != null){
			return RequestThreadLocal.threadLocal.get().getHttpServletRequest();
		}
		else{
			return null;
		}
	}
	/**
	 * This removes the request id from thread.
	 */
	public static void remove(){
		if(RequestThreadLocal.threadLocal != null){
			RequestThreadLocal.threadLocal.remove();
		}
	}
}
