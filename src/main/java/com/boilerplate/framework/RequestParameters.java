package com.boilerplate.framework;

import javax.servlet.http.HttpServletRequest;

import com.boilerplate.sessions.Session;

/**
 * This is the internal class to keep the information of thread local.
 * @author gaurav
 */
public class RequestParameters{
	/**
	 * This is the request Id
	 */
	private String requestId;
	
	/**
	 * This is http request
	 */
	
	private HttpServletRequest httpServletRequest;
	
	/**
	 * This is the session of the request
	 */
	private Session session;
	
	/**
	 * This returns the request id
	 * @return The request id
	 */
	public String getRequestId() {
		return requestId;
	}
	
	/**
	 * This sets the request id
	 * @param requestId
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	/**
	 * This returns the http request
	 * @return The http request
	 */
	public HttpServletRequest getHttpServletRequest() {
		return httpServletRequest;
	}
	
	/**
	 * This sets the http request
	 * @param httpServletRequest The http request
	 */
	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}

	/**
	 * This gets the session
	 * @return The instance of session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * This sets the session
	 * @param session The instance of session.
	 */
	public void setSession(Session session) {
		this.session = session;
	}
	
}

