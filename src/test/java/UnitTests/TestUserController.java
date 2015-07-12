package UnitTests;

import static org.junit.Assert.*;

import java.util.UUID;

import javax.servlet.http.Cookie;

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
import com.boilerplate.exceptions.BaseBoilerplateException;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.framework.web.HttpRequestIdInterceptor;
import com.boilerplate.java.Constants;
import com.boilerplate.java.controllers.HealthController;
import com.boilerplate.java.controllers.UserController;
import com.boilerplate.java.controllers.WidgetController;
import com.boilerplate.java.entities.AuthenticationRequest;
import com.boilerplate.java.entities.BaseEntity;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.Widget;
import com.boilerplate.service.interfaces.IWidgetService;
import com.boilerplate.sessions.Session;


@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-context.xml"})
public class TestUserController {
	@Autowired
	UserController userController;
	@Autowired
	HealthController healthController;
	@Autowired HttpRequestIdInterceptor httpRequestInterseptor;
	
	@Test
	public void testCreateUser() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response=new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request,response, null);
		
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
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);
		Assert.assertNotNull(session.getId());
		Assert.assertNotNull(session.getSessionId());
		Assert.assertNotNull(session.getId());
		Assert.assertEquals(session.getExternalFacingUser().getUserId(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(session.getExternalFacingUser().getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(session.getExternalFacingUser().getPassword(), "Password Encrypted");
		Assert.assertEquals(externalFacingUser.getId(), session.getExternalFacingUser().getId());
		//check if response has headers and cookie

		for(String s : response.getHeaderNames()){
			System.out.println(s+" -- "+response.getHeaderValue(s));
		}
				
				
		Assert.assertEquals(session.getSessionId(),response.getHeaderValue(Constants.AuthTokenHeaderKey));
		Assert.assertEquals(session.getSessionId(),response.getCookie("AuthToken").getValue());
		
		httpRequestInterseptor.afterCompletion(request,response,null,null);
	}
	
	//case provider is not told
	@Test
	public void testCreateUserWithProviderNotSpecified() throws Exception {
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response=new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request,response, null);
		
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals(("DEFAULT:"+userId).toUpperCase(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		Assert.assertEquals(userId.toUpperCase(), externalFacingUserReturned.getExternalSystemId().toUpperCase());
		//need to write code for get / auth to check the user creation worked
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);
		Assert.assertNotNull(session.getId());
		Assert.assertNotNull(session.getSessionId());
		Assert.assertNotNull(session.getId());
		Assert.assertEquals(session.getExternalFacingUser().getUserId(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(session.getExternalFacingUser().getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(session.getExternalFacingUser().getPassword(), "Password Encrypted");
		Assert.assertEquals(externalFacingUser.getId(), session.getExternalFacingUser().getId());
		httpRequestInterseptor.afterCompletion(request, response, null, null);
	}
	
	//auth provider is in some other case
	@Test
	public void testCreateUserWithProviderInARandomCase() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response=new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request,response, null);
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("DeFaUlT");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals(("DEFAULT:"+userId).toUpperCase(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		Assert.assertEquals(userId.toUpperCase(), externalFacingUserReturned.getExternalSystemId().toUpperCase());
		//need to write code for get / auth to check the user creation worked
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);
		Assert.assertNotNull(session.getId());
		Assert.assertNotNull(session.getSessionId());
		Assert.assertNotNull(session.getId());
		Assert.assertEquals(session.getExternalFacingUser().getUserId(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(session.getExternalFacingUser().getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(session.getExternalFacingUser().getPassword(), "Password Encrypted");
		Assert.assertEquals(externalFacingUser.getId(), session.getExternalFacingUser().getId());
		
		httpRequestInterseptor.afterCompletion(request, response, null, null);
	}
	
	
	//case duplicate user name in same provider
	@Test(expected=ConflictException.class)
	public void testCreateUserWithDuplicateUserName() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response=new MockHttpServletResponse();
		HttpRequestIdInterceptor requestIdInterseptor = new HttpRequestIdInterceptor();
		requestIdInterseptor.preHandle(request,response, null);
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals(("DEFAULT:"+userId).toUpperCase(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		Assert.assertEquals(userId.toUpperCase(), externalFacingUserReturned.getExternalSystemId());
		externalFacingUser.setUserId(userId);
		userController.createUser(externalFacingUser);
		
		requestIdInterseptor.afterCompletion(request, response, null, null);
	}
	
	//case duplicate user name in different provider
	@Test
	public void testCreateUserWithDuplicateUserNameInDifferentProviders() throws Exception {
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response=new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request,response, null);
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals(("DEFAULT:"+userId).toUpperCase(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);
		Assert.assertNotNull(session.getId());
		Assert.assertNotNull(session.getSessionId());
		Assert.assertNotNull(session.getId());
		Assert.assertEquals(session.getExternalFacingUser().getUserId(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(session.getExternalFacingUser().getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(session.getExternalFacingUser().getPassword(), "Password Encrypted");
		Assert.assertEquals(externalFacingUser.getId(), session.getExternalFacingUser().getId());
		
		externalFacingUser.setUserId(userId);
		externalFacingUser.setAuthenticationProvider("NotDefault");
		userController.createUser(externalFacingUser);
		Assert.assertEquals(("NOTDEFAULT:"+userId).toUpperCase(), externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "NOTDEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		
		httpRequestInterseptor.afterCompletion(request, response, null, null);
	}
	
	//user name null
	@Test(expected=ValidationFailedException.class)
	public void testCreateUserWithUserNameNull() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response=new MockHttpServletResponse();
		HttpRequestIdInterceptor requestIdInterseptor = new HttpRequestIdInterceptor();
		requestIdInterseptor.preHandle(request,response, null);
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		requestIdInterseptor.afterCompletion(request, response, null, null);
	}
	
	//user name blank
	@Test(expected=ValidationFailedException.class)
	public void testCreateUserWithUserNameBlank() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response=new MockHttpServletResponse();
		HttpRequestIdInterceptor requestIdInterseptor = new HttpRequestIdInterceptor();
		requestIdInterseptor.preHandle(request,response, null);
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId("");
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		requestIdInterseptor.afterCompletion(request, response, null, null);
	}
	//password null

	@Test(expected=ValidationFailedException.class)
	public void testCreateUserWithPasswordNull() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response=new MockHttpServletResponse();
		HttpRequestIdInterceptor requestIdInterseptor = new HttpRequestIdInterceptor();
		requestIdInterseptor.preHandle(request,response, null);
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		requestIdInterseptor.afterCompletion(request, response, null, null);
	}
	//password blank
	@Test(expected=ValidationFailedException.class)
	public void testCreateUserWithPasswordNBlank() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response=new MockHttpServletResponse();
		HttpRequestIdInterceptor requestIdInterseptor = new HttpRequestIdInterceptor();
		requestIdInterseptor.preHandle(request,response, null);
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword("");
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		requestIdInterseptor.afterCompletion(request, response, null, null);
	}
	@Test
	public void testCustomAttributeOnSessionUsingCache() throws Exception{
		//create user
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
		//set a custom parameter
		session.addSessionAttribute(userId, userId);
		httpRequestInterseptor.afterCompletion(request,response, null,null);
		
		//make a new call
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.addHeader(Constants.AuthTokenHeaderKey, session.getSessionId());

		httpRequestInterseptor.preHandle(request, response, new Object());
		healthController.ping();
		Session sessionBack = RequestThreadLocal.getSession();
		String customAttribut = (String)session.getSessionAttribute(userId);
		//check session on thread
		Assert.assertEquals(userId, customAttribut);
	}
	
	@Test
	public void testCustomAttributeOnSessionUsingDB() throws Exception{
		//create user
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
		//set a custom parameter
		session.addSessionAttribute(userId, userId);
		httpRequestInterseptor.afterCompletion(request,response, null,null);

		CacheFactory.getInstance().remove(Constants.SESSION+session.getSessionId().toUpperCase());
		//make a new call
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.addHeader(Constants.AuthTokenHeaderKey, session.getSessionId());

		httpRequestInterseptor.preHandle(request, response, new Object());
		healthController.ping();
		Session sessionBack = RequestThreadLocal.getSession();
		String customAttribut = (String)session.getSessionAttribute(userId);
		//check session on thread
		Assert.assertEquals(userId, customAttribut);
	}
	
	
	
	@Test
	public void testSessionUserIsCorrectWithHeader() throws Exception{
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
		httpRequestInterseptor.afterCompletion(request, response, null, null);
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
		Assert.assertEquals(session.getExternalFacingUser()
				, RequestThreadLocal.getSession().getExternalFacingUser());
		httpRequestInterseptor.afterCompletion(request, response, null, null);
	}
	
	
	
	//test session is picked from QS
	@Test
	public void testSessionUserIsCorrectWithQueryString() throws Exception{
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
		httpRequestInterseptor.afterCompletion(request, response, null, null);
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

		Assert.assertEquals(session.getExternalFacingUser()
				, RequestThreadLocal.getSession().getExternalFacingUser());
		httpRequestInterseptor.afterCompletion(request, response, null, null);
	}
	//test session is picked from cookie
	@Test
	public void testSessionUserIsCorrectWithCookie() throws Exception{
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
		httpRequestInterseptor.afterCompletion(request, response, null, null);
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

		Assert.assertEquals(session.getExternalFacingUser()
				, RequestThreadLocal.getSession().getExternalFacingUser());
		httpRequestInterseptor.afterCompletion(request, response, null, null);
	}
	
	
	
	@Test
	public void testBaseControllerMethods() throws Exception{
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
		
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		httpRequestInterseptor.preHandle(request, response, new Object());

		Session session = userController.authenticate(authenticationRequest);
        
		
		Assert.assertEquals(response, userController.getHttpServletResponse());
		
		
		userController.addHeader(userId, userId);
		Assert.assertEquals(userId, response.getHeaderValue(userId));
		
		userController.addCookie(userId, userId, 1200);
		Assert.assertEquals(userId,response.getCookie(userId).getValue());
		
		Assert.assertNotNull(userController.getRequestId());
		Session session1 = RequestThreadLocal.getSession();
	      Assert.assertEquals(session, userController.getSession());
	}
	//test exceptions from base controller
	
	
	
}
