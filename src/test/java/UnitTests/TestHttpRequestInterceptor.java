package UnitTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

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

import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.framework.web.HttpRequestIdInterceptor;
import com.boilerplate.java.Constants;
import com.boilerplate.java.controllers.UserController;
import com.boilerplate.java.entities.AuthenticationRequest;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.sessions.Session;

@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-context.xml"})
public class TestHttpRequestInterceptor {

	@Autowired
	UserController userController;
	
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
       
        HttpRequestIdInterceptor httpRequestIdInterceptor = new
        		HttpRequestIdInterceptor();
        httpRequestIdInterceptor.preHandle(request, response, new Object());
        
        
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
		httpRequestIdInterceptor = new
        		HttpRequestIdInterceptor();
		httpRequestIdInterceptor.preHandle(request, response, new Object());
        
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);
        
        
       httpRequestIdInterceptor.afterCompletion(request, response, new Object(), new Exception());
       String sessionId = (String) response.getHeaderValue(Constants.AuthTokenHeaderKey);
              
       request.addHeader(Constants.AuthTokenHeaderKey, sessionId);
       
       httpRequestIdInterceptor = new HttpRequestIdInterceptor();
       httpRequestIdInterceptor.preHandle(request, response, new Object()); 
       //because it is an auth request session should be null
       session = RequestThreadLocal.getSession();
       Assert.assertNull(session);
       //now we call authenticate again
		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		session = userController.authenticate(authenticationRequest);
       String newSessionId = session.getSessionId();
      httpRequestIdInterceptor.afterCompletion(request, response, new Object(), new Exception());
      //the old and new seession ids should not match
      Assert.assertFalse(sessionId.equals(newSessionId));
	}
	
	//auth part of sting
	public void testSessionIsPickedUpIfItIsUsedForNonAuthenticateMethods(){
		//first create a user
		//then authenticate
		
		//Next make a call to ping
		//check in ping response session id is the same as that used during auth
		
	}
	
	//check that session token is present in header and cookie
	
	//test session picked up from header
	
	//test session is picked from QS
	
	//test session is picked from cookie
	
	//test session is picked after cleaning cache
	
	//test session is put back on the database from the queue
	
	//test session is put back on cache
	

}
