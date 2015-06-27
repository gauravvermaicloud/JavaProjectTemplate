package com.boilerplate.framework.web;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.Constants;

/**
 * This class adds unique id to every http request. This id may be used for
 * debugging and client escalation / logging process. 
 * The said id is sent back to users on the header X-Http-Request-Id
 */
@Component
public class HttpRequestIdInterceptor extends HandlerInterceptorAdapter{
	/**
	 * This method sets a http request id for the request.
	 */
	@Override
	 public boolean preHandle(HttpServletRequest request,
		      HttpServletResponse response, Object handler) throws Exception {
		 	String requestId = UUID.randomUUID().toString();
		 	response.addHeader(Constants.X_Http_Request_Id, requestId);
		 	RequestThreadLocal.setRequest(requestId, request);
		 	return true;
	 }
	 
	/**
	 * This method is called just before the response is sent back to client.
	 */
	@Override
	public void afterCompletion(
			HttpServletRequest request, HttpServletResponse response
			, Object handler, Exception ex)
			throws Exception {
		RequestThreadLocal.remove();
	}
}
