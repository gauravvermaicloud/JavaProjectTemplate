package UnitTests;

import static org.junit.Assert.*;

import java.util.UUID;

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

import com.boilerplate.exceptions.BaseBoilerplateException;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.framework.web.HttpRequestIdInterceptor;
import com.boilerplate.java.Constants;
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
	
	@Test
	public void testCreateUser() throws Exception {
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
		
		requestIdInterseptor.afterCompletion(request,response,null,null);
	}
	
	//case provider is not told
	@Test
	public void testCreateUserWithProviderNotSpecified() throws Exception {
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response=new MockHttpServletResponse();
		HttpRequestIdInterceptor requestIdInterseptor = new HttpRequestIdInterceptor();
		requestIdInterseptor.preHandle(request,response, null);
		
		
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
		requestIdInterseptor.afterCompletion(request, response, null, null);
	}
	
	//auth provider is in some other case
	@Test
	public void testCreateUserWithProviderInARandomCase() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response=new MockHttpServletResponse();
		HttpRequestIdInterceptor requestIdInterseptor = new HttpRequestIdInterceptor();
		requestIdInterseptor.preHandle(request,response, null);
		
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
		
		requestIdInterseptor.afterCompletion(request, response, null, null);
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
		
		requestIdInterseptor.afterCompletion(request, response, null, null);
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
	
	
	//Test calling an api after login with stuff in cookie
	//header
	//QS
	//clear cache and then fall back on DB to retreve the session
	
	//SessionId doesnt exist
	
	//test exceptions from base controller
	
	//test methods of base controllers
	
	//test session is picked and user details are correct after login
	
	//test session is updated in cache and DB after a request
	
	//test custom attributes on session
	
	//test with url being authenticate in auth a new session must be created
	
	// test with url not being authenticate the existing session must be retured
	
	//session ater clearing from cache picks it from DB
	
	//session with session reput job
	
	//after multiple requests the session time out on cookie is changed and last update is changed on the session object
	//Test custom attributes on session
}
