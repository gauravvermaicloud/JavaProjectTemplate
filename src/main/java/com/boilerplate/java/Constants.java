package com.boilerplate.java;

/**
 * This class contains all the constants.
 * @author gaurav
 *
 */
public class Constants {
	/**
	 * Http Request id header.
	 */
	public static final String X_Http_Request_Id = "X-Http-Request-Id";
	
	/**
	 * The field is debug enabled
	 */
	public static final String IsDebugEnabled = "IsDebugEnabled";
	
	/**
	 * This is the tag for session key used in the cache
	 */
	public static final String SESSION = "Session:";
	
	/**
	 * The header in which auth token will be specified
	 */
	public static final String AuthTokenHeaderKey ="X-AuthToken";
	
	/**
	 * The cookie in which auth token will be specified
	 */
	public static final String AuthTokenCookieKey ="AuthToken";
	
	/**
	 * The query string in which auth token will be specified
	 */
	public static final String AuthTokenQueryStringKey="authtoken";
	
	/**
	 * The header for user id
	 */
	public static final String X_User_Id = "X-User-Id";

	/**
	 * The queue subject to save session to the database
	 */
	public static final String SaveSessionToDatabase = "SaveSessionToDatabase";
}
