package UnitTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.boilerplate.cache.CacheFactory;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.framework.web.HttpRequestIdInterceptor;
import com.boilerplate.java.Constants;
import com.boilerplate.java.controllers.HealthController;
import com.boilerplate.java.controllers.UserController;
import com.boilerplate.java.entities.AuthenticationRequest;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.jobs.CleanupSessionJob;
import com.boilerplate.sessions.Session;
import com.boilerplate.sessions.SessionManager;

@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-context.xml"})
public class TestHttpRequestInterceptor {

	@Autowired
	UserController userController;
	
	@Autowired
	HealthController healthController;
	
	@Autowired SessionManager sessionManager;
	
	@Autowired HttpRequestIdInterceptor httpRequestInterseptor;
	
	/**
	 * This method tests Http interseptor to create a per request id
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception{
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
       
        HttpRequestIdInterceptor httpRequestIdInterceptor = new
        		HttpRequestIdInterceptor();
        httpRequestIdInterceptor.preHandle(request, response, new Object());
        
       String requestId = (String) response.getHeader("X-Http-Request-Id");
       Assert.assertEquals(requestId, RequestThreadLocal.getRequestId());
       httpRequestIdInterceptor.afterCompletion(request, response, new Object(), new Exception());
       Assert.assertNull(RequestThreadLocal.getRequestId());
	}
	
	//test for new session created during authentictae
	@Test
	public void testNewSessionIsCreatedDuringAuthenticate() throws Exception{
		
		MockHttpServletRequest request = new MockHttpServletRequest("POST","/authenticate");
		MockHttpServletResponse response = new MockHttpServletResponse();
       
		httpRequestInterseptor.preHandle(request, response, new Object());
        
        
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals(("DEFAULT:"+userId).toUpperCase(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		
		//need to write code for get / auth to check the user creation worked
		
		httpRequestInterseptor.preHandle(request, response, new Object());
        
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);
        
        
		httpRequestInterseptor.afterCompletion(request, response, new Object(), new Exception());
       String sessionId = (String) response.getHeaderValue(Constants.AuthTokenHeaderKey);
              
       request.addHeader(Constants.AuthTokenHeaderKey, sessionId);
       
       httpRequestInterseptor = new HttpRequestIdInterceptor();
       httpRequestInterseptor.preHandle(request, response, new Object()); 
       //because it is an auth request session should be null
       session = RequestThreadLocal.getSession();
       Assert.assertNull(session);
       //now we call authenticate again
		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		session = userController.authenticate(authenticationRequest);
       String newSessionId = session.getSessionId();

       
      //the old and new seession ids should not match
      Assert.assertFalse(sessionId.equals(newSessionId));
	}
	
	@Test
	public void testSessionIsPickedUpIfItIsUsedForNonAuthenticateMethodsInHeader() throws Exception{
		//first create a user
		//then authenticate
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
       
        
		httpRequestInterseptor.preHandle(request, response, new Object());
        
        
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals(("DEFAULT:"+userId).toUpperCase(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		
		//need to write code for get / auth to check the user creation worked
		
		request = new MockHttpServletRequest("POST","/authenticate");
		response = new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request, response, new Object());
        
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);

		
		//Next make a call to ping by sending session it in header
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.addHeader(Constants.AuthTokenHeaderKey, session.getSessionId());

		httpRequestInterseptor.preHandle(request, response, new Object());
		healthController.ping();
		//check in ping response session id is the same as that used during auth
		String returnedSessionId =(String)response.getHeaderValue(Constants.AuthTokenHeaderKey);
		Assert.assertEquals(session.getSessionId(), returnedSessionId);
		String sessionIdInCookie = null;
		for(Cookie cookie:response.getCookies()){
			if(cookie.getName().equals(Constants.AuthTokenCookieKey)){
				sessionIdInCookie = cookie.getValue();
			}
		}
		Assert.assertEquals(session.getSessionId(), sessionIdInCookie);
		httpRequestInterseptor.afterCompletion(request, response, null, null);
	}
	
	
	
	//test session is picked from QS
	@Test
	public void testSessionIsPickedUpIfItIsUsedForNonAuthenticateMethodsInQueryString() throws Exception{
		//first create a user
		//then authenticate
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
       
        
		httpRequestInterseptor.preHandle(request, response, new Object());
        
        
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals(("DEFAULT:"+userId).toUpperCase(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		
		//need to write code for get / auth to check the user creation worked
		
		request = new MockHttpServletRequest("POST","/authenticate");
		response = new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request, response, new Object());
        
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);

		
		//Next make a call to ping by sending session it in header
		request = new MockHttpServletRequest("GET","/ping");
		response = new MockHttpServletResponse();
		request.setQueryString(Constants.AuthTokenQueryStringKey+"="+session.getSessionId());
		httpRequestInterseptor.preHandle(request, response, new Object());
		healthController.ping();
		//check in ping response session id is the same as that used during auth
		String returnedSessionId =(String)response.getHeaderValue(Constants.AuthTokenHeaderKey);
		Assert.assertEquals(session.getSessionId(), returnedSessionId);
		String sessionIdInCookie = null;
		for(Cookie cookie:response.getCookies()){
			if(cookie.getName().equals(Constants.AuthTokenCookieKey)){
				sessionIdInCookie = cookie.getValue();
			}
		}
		Assert.assertEquals(session.getSessionId(), sessionIdInCookie);
		httpRequestInterseptor.afterCompletion(request, response, null, null);
	}
	//test session is picked from cookie
	@Test
	public void testSessionIsPickedUpIfItIsUsedForNonAuthenticateMethodsInCookie() throws Exception{
		//first create a user
		//then authenticate
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
       
        
		httpRequestInterseptor.preHandle(request, response, new Object());
        
        
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals(("DEFAULT:"+userId).toUpperCase(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		
		//need to write code for get / auth to check the user creation worked
		
		request = new MockHttpServletRequest("POST","/authenticate");
		response = new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request, response, new Object());
        
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);

		
		//Next make a call to ping by sending session it in header
		request = new MockHttpServletRequest("GET","/ping");
		response = new MockHttpServletResponse();
		Cookie cookieInRequest = new Cookie(Constants.AuthTokenCookieKey, session.getSessionId());
		request.setCookies(cookieInRequest);
		httpRequestInterseptor.preHandle(request, response, new Object());
		healthController.ping();
		//check in ping response session id is the same as that used during auth
		String returnedSessionId =(String)response.getHeaderValue(Constants.AuthTokenHeaderKey);
		Assert.assertEquals(session.getSessionId(), returnedSessionId);
		String sessionIdInCookie = null;
		for(Cookie cookie:response.getCookies()){
			if(cookie.getName().equals(Constants.AuthTokenCookieKey)){
				sessionIdInCookie = cookie.getValue();
			}
		}
		Assert.assertEquals(session.getSessionId(), sessionIdInCookie);
		httpRequestInterseptor.afterCompletion(request, response, null, null);
	}
	
	//test session is picked after cleaning cache
	@Test
	public void testSessionIsPickedUpIfItIsUsedForNonAuthenticateMethodsInHeaderAfterRemovingFromCache() throws Exception{
		//first create a user
		//then authenticate
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
       
        
		httpRequestInterseptor.preHandle(request, response, new Object());
        
        
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals(("DEFAULT:"+userId).toUpperCase(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		
		//need to write code for get / auth to check the user creation worked
		
		request = new MockHttpServletRequest("POST","/authenticate");
		response = new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request, response, new Object());
        
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);

		
		//Next make a call to ping by sending session it in header
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.addHeader(Constants.AuthTokenHeaderKey, session.getSessionId());
		CacheFactory.getInstance().remove(Constants.SESSION+session.getSessionId().toUpperCase());
		httpRequestInterseptor.preHandle(request, response, new Object());
		healthController.ping();
		//check in ping response session id is the same as that used during auth
		String returnedSessionId =(String)response.getHeaderValue(Constants.AuthTokenHeaderKey);
		Assert.assertEquals(session.getSessionId(), returnedSessionId);
		String sessionIdInCookie = null;
		for(Cookie cookie:response.getCookies()){
			if(cookie.getName().equals(Constants.AuthTokenCookieKey)){
				sessionIdInCookie = cookie.getValue();
			}
		}
		Assert.assertEquals(session.getSessionId(), sessionIdInCookie);
		httpRequestInterseptor.afterCompletion(request, response, null, null);
	}
	
	
	
	//test session is picked from QS
	@Test
	public void testSessionIsPickedUpIfItIsUsedForNonAuthenticateMethodsInQueryStringAfterRemovingFromCache() throws Exception{
		//first create a user
		//then authenticate
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
       
        
		httpRequestInterseptor.preHandle(request, response, new Object());
        
        
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals(("DEFAULT:"+userId).toUpperCase(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		
		//need to write code for get / auth to check the user creation worked
		
		request = new MockHttpServletRequest("POST","/authenticate");
		response = new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request, response, new Object());
        
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);

		
		//Next make a call to ping by sending session it in header
		request = new MockHttpServletRequest("GET","/ping");
		response = new MockHttpServletResponse();
		request.setQueryString(Constants.AuthTokenQueryStringKey+"="+session.getSessionId());
		CacheFactory.getInstance().remove(Constants.SESSION+session.getSessionId().toUpperCase());
		httpRequestInterseptor.preHandle(request, response, new Object());
		healthController.ping();
		//check in ping response session id is the same as that used during auth
		String returnedSessionId =(String)response.getHeaderValue(Constants.AuthTokenHeaderKey);
		Assert.assertEquals(session.getSessionId(), returnedSessionId);
		String sessionIdInCookie = null;
		for(Cookie cookie:response.getCookies()){
			if(cookie.getName().equals(Constants.AuthTokenCookieKey)){
				sessionIdInCookie = cookie.getValue();
			}
		}
		Assert.assertEquals(session.getSessionId(), sessionIdInCookie);
		httpRequestInterseptor.afterCompletion(request, response, null, null);
	}
	//test session is picked from cookie
	@Test
	public void testSessionIsPickedUpIfItIsUsedForNonAuthenticateMethodsInCookieAfterCleaningCache() throws Exception{
		//first create a user
		//then authenticate
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
       
        
		httpRequestInterseptor.preHandle(request, response, new Object());
        
        
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals(("DEFAULT:"+userId).toUpperCase(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		
		//need to write code for get / auth to check the user creation worked
		
		request = new MockHttpServletRequest("POST","/authenticate");
		response = new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request, response, new Object());
        
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);

		
		//Next make a call to ping by sending session it in header
		request = new MockHttpServletRequest("GET","/ping");
		response = new MockHttpServletResponse();
		Cookie cookieInRequest = new Cookie(Constants.AuthTokenCookieKey, session.getSessionId());
		request.setCookies(cookieInRequest);
		CacheFactory.getInstance().remove(Constants.SESSION+session.getSessionId().toUpperCase());
		httpRequestInterseptor.preHandle(request, response, new Object());
		healthController.ping();
		//check in ping response session id is the same as that used during auth
		String returnedSessionId =(String)response.getHeaderValue(Constants.AuthTokenHeaderKey);
		Assert.assertEquals(session.getSessionId(), returnedSessionId);
		String sessionIdInCookie = null;
		for(Cookie cookie:response.getCookies()){
			if(cookie.getName().equals(Constants.AuthTokenCookieKey)){
				sessionIdInCookie = cookie.getValue();
			}
		}
		Assert.assertEquals(session.getSessionId(), sessionIdInCookie);
		httpRequestInterseptor.afterCompletion(request, response, null, null);
	}

	@Autowired
	com.boilerplate.jobs.QueueReaderJob queueReaderJob;
	
	//test session is put back on the database from the queue
	@Test
	public void testSessionIsPutBackOnDatabaseUsingQueue() throws Exception{
		//first create a user
		//then authenticate
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
       
        
		httpRequestInterseptor.preHandle(request, response, new Object());
        
        
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals(("DEFAULT:"+userId).toUpperCase(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		
		//need to write code for get / auth to check the user creation worked
		
		request = new MockHttpServletRequest("POST","/authenticate");
		response = new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request, response, new Object());
        
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);

		
		//Next make a call to ping by sending session it in header
		request = new MockHttpServletRequest("GET","/ping");
		response = new MockHttpServletResponse();
		Cookie cookieInRequest = new Cookie(Constants.AuthTokenCookieKey, session.getSessionId());
		request.setCookies(cookieInRequest);
		CacheFactory.getInstance().remove(Constants.SESSION+session.getSessionId().toUpperCase());
		httpRequestInterseptor.preHandle(request, response, new Object());
		healthController.ping();
		
		httpRequestInterseptor.afterCompletion(request, response, null, null);
		
		//Make a call to the queue job
		queueReaderJob.readQueueAndDispatch();
		//make a call to th DB and find the session for given session id
		Session sessionFromDatabase = sessionManager.getSession(session.getSessionId());
		//check date in db is greater than creation time
		Assert.assertTrue((sessionFromDatabase.getUpdationDate().getTime() 
				- sessionFromDatabase.getCreationDate().getTime())>0);
	}

	//test session is put back on cache
	@Test
	public void testSessionIsPutBackOnCacheUsingQueue() throws Exception{
		//first create a user
		//then authenticate
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
       
        
		httpRequestInterseptor.preHandle(request, response, new Object());
        
        
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals(("DEFAULT:"+userId).toUpperCase(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		
		//need to write code for get / auth to check the user creation worked
		
		request = new MockHttpServletRequest("POST","/authenticate");
		response = new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request, response, new Object());
        
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);

		
		//Next make a call to ping by sending session it in header
		request = new MockHttpServletRequest("GET","/ping");
		response = new MockHttpServletResponse();
		Cookie cookieInRequest = new Cookie(Constants.AuthTokenCookieKey, session.getSessionId());
		request.setCookies(cookieInRequest);
		CacheFactory.getInstance().remove(Constants.SESSION+session.getSessionId().toUpperCase());
		httpRequestInterseptor.preHandle(request, response, new Object());
		healthController.ping();
		
		httpRequestInterseptor.afterCompletion(request, response, null, null);
		
		//Make a call to the queue job
		queueReaderJob.readQueueAndDispatch();
		//make a call to th DB and find the session for given session id
		Session sessionFromCache = 
				CacheFactory.getInstance().get(Constants.SESSION+session.getSessionId().toUpperCase(),Session.class);
		//check date in db is greater than creation time
		Assert.assertTrue((sessionFromCache.getUpdationDate().getTime() 
				- sessionFromCache.getCreationDate().getTime())>0);
	}
	
	//test expired session is not picked up if in cache
	
	@SuppressWarnings("deprecation")
	@Test
	public void testSessionIsNotPickedUpFromCacheIfExpiredWhenSentInHeader() throws Exception{
		//first create a user
		//then authenticate
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
       
        
		httpRequestInterseptor.preHandle(request, response, new Object());
        
        
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals(("DEFAULT:"+userId).toUpperCase(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		
		//need to write code for get / auth to check the user creation worked
		
		request = new MockHttpServletRequest("POST","/authenticate");
		response = new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request, response, new Object());
        
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);

		
		//Next make a call to ping by sending session it in header
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.addHeader(Constants.AuthTokenHeaderKey, session.getSessionId());
		
		//expire session on cache and DB but not remove from cache
		session.setUpdationDate(new Date(new Date().getTime()-1300*1000));
		CacheFactory.getInstance().add(Constants.SESSION+session.getSessionId().toUpperCase(), session);
		sessionManager.saveSession(session);
		
		httpRequestInterseptor.preHandle(request, response, new Object());
		healthController.ping();
		//check in ping response session id is the same as that used during auth
		//this should be expired and hence not picked up
		
		String returnedSessionId =(String)response.getHeaderValue(Constants.AuthTokenHeaderKey);
		Assert.assertNull(returnedSessionId);
		httpRequestInterseptor.afterCompletion(request, response, null, null);
	}
	
	
	
	@Test
	public void testSessionIsNotPickedUpFromCacheIfExpiredWhenSentInQueryString() throws Exception{
		//first create a user
		//then authenticate
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
       
        
		httpRequestInterseptor.preHandle(request, response, new Object());
        
        
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals(("DEFAULT:"+userId).toUpperCase(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		
		//need to write code for get / auth to check the user creation worked
		
		request = new MockHttpServletRequest("POST","/authenticate");
		response = new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request, response, new Object());
        
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);

		
		//Next make a call to ping by sending session it in header
		request = new MockHttpServletRequest("GET","/ping");
		response = new MockHttpServletResponse();
		request.setQueryString(Constants.AuthTokenQueryStringKey+"="+session.getSessionId());
		
		//expire session on cache and DB but not remove from cache
		session.setUpdationDate(new Date(new Date().getTime()-1300*1000));
		CacheFactory.getInstance().add(Constants.SESSION+session.getSessionId().toUpperCase(), session);
		sessionManager.saveSession(session);

		
		httpRequestInterseptor.preHandle(request, response, new Object());
		healthController.ping();
		//check in ping response session id is the same as that used during auth
		String returnedSessionId =(String)response.getHeaderValue(Constants.AuthTokenHeaderKey);
		Assert.assertNull(returnedSessionId);
	}
	//test session is picked from cookie
	@Test
	public void testSessionIsNotPickedUpFromCacheIfExpiredWhenSentInCookie() throws Exception{
		//first create a user
		//then authenticate
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
       
        
		httpRequestInterseptor.preHandle(request, response, new Object());
        
        
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals(("DEFAULT:"+userId).toUpperCase(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		
		//need to write code for get / auth to check the user creation worked
		
		request = new MockHttpServletRequest("POST","/authenticate");
		response = new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request, response, new Object());
        
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);

		
		//Next make a call to ping by sending session it in header
		request = new MockHttpServletRequest("GET","/ping");
		response = new MockHttpServletResponse();
		Cookie cookieInRequest = new Cookie(Constants.AuthTokenCookieKey, session.getSessionId());
		request.setCookies(cookieInRequest);
		
		//expire session on cache and DB but not remove from cache
		session.setUpdationDate(new Date(new Date().getTime()-1300*1000));
		CacheFactory.getInstance().add(Constants.SESSION+session.getSessionId().toUpperCase(), session);
		sessionManager.saveSession(session);

		
		httpRequestInterseptor.preHandle(request, response, new Object());
		healthController.ping();
		//check in ping response session id is the same as that used during auth
		String returnedSessionId =(String)response.getHeaderValue(Constants.AuthTokenHeaderKey);
		Assert.assertNull(returnedSessionId);
	}
	
	
		
	//test expired session is not picked up if in DB
	@Test
	public void testSessionIsNotPickedUpFromDBIfExpiredWhenSentInHeader() throws Exception{
		//first create a user
		//then authenticate
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
       
        
		httpRequestInterseptor.preHandle(request, response, new Object());
        
        
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals(("DEFAULT:"+userId).toUpperCase(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		
		//need to write code for get / auth to check the user creation worked
		
		request = new MockHttpServletRequest("POST","/authenticate");
		response = new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request, response, new Object());
        
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);

		
		//Next make a call to ping by sending session it in header
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.addHeader(Constants.AuthTokenHeaderKey, session.getSessionId());
		
		//expire session on cache and DB but not remove from cache
		session.setUpdationDate(new Date(new Date().getTime()-1300*1000));
		CacheFactory.getInstance().remove(Constants.SESSION+session.getSessionId().toUpperCase());
		sessionManager.saveSession(session);
		
		httpRequestInterseptor.preHandle(request, response, new Object());
		healthController.ping();
		//check in ping response session id is the same as that used during auth
		//this should be expired and hence not picked up
		
		String returnedSessionId =(String)response.getHeaderValue(Constants.AuthTokenHeaderKey);
		Assert.assertNull(returnedSessionId);
		httpRequestInterseptor.afterCompletion(request, response, null, null);
	}
	
	
	
	@Test
	public void testSessionIsNotPickedUpFromDBIfExpiredWhenSentInQueryString() throws Exception{
		//first create a user
		//then authenticate
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
       
        
		httpRequestInterseptor.preHandle(request, response, new Object());
        
        
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals(("DEFAULT:"+userId).toUpperCase(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		
		//need to write code for get / auth to check the user creation worked
		
		request = new MockHttpServletRequest("POST","/authenticate");
		response = new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request, response, new Object());
        
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);

		
		//Next make a call to ping by sending session it in header
		request = new MockHttpServletRequest("GET","/ping");
		response = new MockHttpServletResponse();
		request.setQueryString(Constants.AuthTokenQueryStringKey+"="+session.getSessionId());
		
		//expire session on cache and DB but not remove from cache
		session.setUpdationDate(new Date(new Date().getTime()-1300*1000));
		CacheFactory.getInstance().remove(Constants.SESSION+session.getSessionId().toUpperCase());
		sessionManager.saveSession(session);

		
		httpRequestInterseptor.preHandle(request, response, new Object());
		healthController.ping();
		//check in ping response session id is the same as that used during auth
		String returnedSessionId =(String)response.getHeaderValue(Constants.AuthTokenHeaderKey);
		Assert.assertNull(returnedSessionId);
	}
	//test session is picked from cookie
	@Test
	public void testSessionIsNotPickedUpFromDBIfExpiredWhenSentInCookie() throws Exception{
		//first create a user
		//then authenticate
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
       
        
		httpRequestInterseptor.preHandle(request, response, new Object());
        
        
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals(("DEFAULT:"+userId).toUpperCase(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		
		//need to write code for get / auth to check the user creation worked
		
		request = new MockHttpServletRequest("POST","/authenticate");
		response = new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request, response, new Object());
        
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);

		
		//Next make a call to ping by sending session it in header
		request = new MockHttpServletRequest("GET","/ping");
		response = new MockHttpServletResponse();
		Cookie cookieInRequest = new Cookie(Constants.AuthTokenCookieKey, session.getSessionId());
		request.setCookies(cookieInRequest);
		
		//expire session on cache and DB but not remove from cache
		session.setUpdationDate(new Date(new Date().getTime()-1300*1000));
		CacheFactory.getInstance().remove(Constants.SESSION+session.getSessionId().toUpperCase());
		sessionManager.saveSession(session);

		
		httpRequestInterseptor.preHandle(request, response, new Object());
		healthController.ping();
		//check in ping response session id is the same as that used during auth
		String returnedSessionId =(String)response.getHeaderValue(Constants.AuthTokenHeaderKey);
		Assert.assertNull(returnedSessionId);
	}

	@Autowired
	CleanupSessionJob cleanupSessionJob;
	//test expired session is cleaned
	@Test
	public void testExpiredSessionIsCleanedFromDB() throws Exception{
		//first create a user
		//then authenticate
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
       
        
		httpRequestInterseptor.preHandle(request, response, new Object());
        
        
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals(("DEFAULT:"+userId).toUpperCase(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		
		//need to write code for get / auth to check the user creation worked
		
		request = new MockHttpServletRequest("POST","/authenticate");
		response = new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request, response, new Object());
        
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);

		
		//Next make a call to ping by sending session it in header
		request = new MockHttpServletRequest("GET","/ping");
		response = new MockHttpServletResponse();
		Cookie cookieInRequest = new Cookie(Constants.AuthTokenCookieKey, session.getSessionId());
		request.setCookies(cookieInRequest);
		
		//expire session on cache and DB but not remove from cache
		session.setUpdationDate(new Date(new Date().getTime()-1300*1000));
		CacheFactory.getInstance().remove(Constants.SESSION+session.getSessionId().toUpperCase());
		sessionManager.saveSession(session);

		
		httpRequestInterseptor.preHandle(request, response, new Object());
		healthController.ping();
		//check in ping response session id is the same as that used during auth
		String returnedSessionId =(String)response.getHeaderValue(Constants.AuthTokenHeaderKey);
		Assert.assertNull(returnedSessionId);
		
		//callthe job to clean session from the DB
		cleanupSessionJob.cleanup();
		Session sessionFromDB = sessionManager.getSession(session.getSessionId());
		Assert.assertNull(sessionFromDB);
	}

	@Test
	public void testUnExpiredSessionIsNotCleanedFromDB() throws Exception{
		//first create a user
		//then authenticate
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
       
        
		httpRequestInterseptor.preHandle(request, response, new Object());
        
        
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals(("DEFAULT:"+userId).toUpperCase(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		
		//need to write code for get / auth to check the user creation worked
		
		request = new MockHttpServletRequest("POST","/authenticate");
		response = new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request, response, new Object());
        
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);

		
		//callthe job to clean session from the DB
		cleanupSessionJob.cleanup();
		Session sessionFromDB = sessionManager.getSession(session.getSessionId());
		Assert.assertEquals(session.getSessionId(), sessionFromDB.getSessionId());
	}

	
	@Test
	public void testInvalidSessionId() throws Exception{
		//first create a user
		//then authenticate
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
       
        
		httpRequestInterseptor.preHandle(request, response, new Object());
        
        
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals(("DEFAULT:"+userId).toUpperCase(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		
		//need to write code for get / auth to check the user creation worked
		
		request = new MockHttpServletRequest("POST","/authenticate");
		response = new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request, response, new Object());
        
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);

		
		//Next make a call to ping by sending session it in header
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.addHeader(Constants.AuthTokenHeaderKey, UUID.randomUUID().toString());
				
		httpRequestInterseptor.preHandle(request, response, new Object());
		healthController.ping();
		//check in ping response session id is the same as that used during auth
		//this should be expired and hence not picked up
		
		String returnedSessionId =(String)response.getHeaderValue(Constants.AuthTokenHeaderKey);
		Assert.assertNull(returnedSessionId);
		httpRequestInterseptor.afterCompletion(request, response, null, null);
	}
	
}
