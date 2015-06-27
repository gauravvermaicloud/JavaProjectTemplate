package UnitTests;

import static org.junit.Assert.*;

import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.boilerplate.exceptions.BaseBoilerplateException;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.controllers.UserController;
import com.boilerplate.java.controllers.WidgetController;
import com.boilerplate.java.entities.BaseEntity;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.Widget;
import com.boilerplate.service.interfaces.IWidgetService;


@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-context.xml"})
public class TestUserController {
	@Autowired
	UserController userController;
	
	@Test
	public void testCreateUser() throws BaseBoilerplateException {
		RequestThreadLocal.setRequest(UUID.randomUUID().toString(),null);
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals("DEFAULT:"+userId, externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		//need to write code for get / auth to check the user creation worked
		RequestThreadLocal.remove();
	}
	
	//case provider is not told
	@Test
	public void testCreateUserWithProviderNotSpecified() throws BaseBoilerplateException {
		RequestThreadLocal.setRequest(UUID.randomUUID().toString(),null);
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals("DEFAULT:"+userId, externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		Assert.assertEquals(userId, externalFacingUserReturned.getExternalSystemId());
		//need to write code for get / auth to check the user creation worked
		RequestThreadLocal.remove();
	}
	
	//auth provider is in some other case
	@Test
	public void testCreateUserWithProviderInARandomCase() throws BaseBoilerplateException {
		RequestThreadLocal.setRequest(UUID.randomUUID().toString(),null);
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("DeFaUlT");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals("DEFAULT:"+userId, externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		Assert.assertEquals(userId, externalFacingUserReturned.getExternalSystemId());
		//need to write code for get / auth to check the user creation worked
		RequestThreadLocal.remove();
	}
	
	
	//case duplicate user name in same provider
	@Test(expected=ConflictException.class)
	public void testCreateUserWithDuplicateUserName() throws BaseBoilerplateException {
		RequestThreadLocal.setRequest(UUID.randomUUID().toString(),null);
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals("DEFAULT:"+userId, externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		Assert.assertEquals(userId, externalFacingUserReturned.getExternalSystemId());
		externalFacingUser.setUserId(userId);
		userController.createUser(externalFacingUser);
		//need to write code for get / auth to check the user creation worked
		RequestThreadLocal.remove();
	}
	
	//case duplicate user name in different provider
	@Test
	public void testCreateUserWithDuplicateUserNameInDifferentProviders() throws BaseBoilerplateException {
		RequestThreadLocal.setRequest(UUID.randomUUID().toString(),null);
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		Assert.assertEquals("DEFAULT:"+userId, externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "DEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		externalFacingUser.setUserId(userId);
		externalFacingUser.setAuthenticationProvider("NotDefault");
		userController.createUser(externalFacingUser);
		Assert.assertEquals("NOTDEFAULT:"+userId, externalFacingUserReturned.getUserId());
		Assert.assertEquals(externalFacingUser.getAuthenticationProvider(), "NOTDEFAULT");
		Assert.assertEquals(externalFacingUser.getPassword(), "Password Encrypted");
		//need to write code for get / auth to check the user creation worked
		RequestThreadLocal.remove();
	}
	
	//user name null
	@Test(expected=ValidationFailedException.class)
	public void testCreateUserWithUserNameNull() throws BaseBoilerplateException {
		RequestThreadLocal.setRequest(UUID.randomUUID().toString(),null);
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		//need to write code for get / auth to check the user creation worked
		RequestThreadLocal.remove();
	}
	
	//user name blank
	@Test(expected=ValidationFailedException.class)
	public void testCreateUserWithUserNameBlank() throws BaseBoilerplateException {
		RequestThreadLocal.setRequest(UUID.randomUUID().toString(),null);
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId("");
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		//need to write code for get / auth to check the user creation worked
		RequestThreadLocal.remove();
	}
	//password null

	@Test(expected=ValidationFailedException.class)
	public void testCreateUserWithPasswordNull() throws BaseBoilerplateException {
		RequestThreadLocal.setRequest(UUID.randomUUID().toString(),null);
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		//need to write code for get / auth to check the user creation worked
		RequestThreadLocal.remove();
	}
	//password blank
	@Test(expected=ValidationFailedException.class)
	public void testCreateUserWithPasswordNBlank() throws BaseBoilerplateException {
		RequestThreadLocal.setRequest(UUID.randomUUID().toString(),null);
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword("");
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);
		//need to write code for get / auth to check the user creation worked
		RequestThreadLocal.remove();
	}
	
}
