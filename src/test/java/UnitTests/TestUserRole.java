package UnitTests;

import java.util.List;
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

import com.boilerplate.database.interfaces.IRole;
import com.boilerplate.database.interfaces.IUserRole;
import com.boilerplate.database.mysql.implementations.MySQLBaseDataAccessLayer;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.framework.web.HttpRequestIdInterceptor;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.controllers.RoleController;
import com.boilerplate.java.controllers.UserController;
import com.boilerplate.java.controllers.UserRoleController;
import com.boilerplate.java.entities.AuthenticationRequest;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.GenericListEncapsulationEntity;
import com.boilerplate.java.entities.Role;
import com.boilerplate.sessions.Session;

@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-context.xml"})
public class TestUserRole {
	@Autowired
	UserController userController;
	
	@Autowired
	UserRoleController userRoleController;
	

	@Autowired HttpRequestIdInterceptor httpRequestInterseptor;
	
	@Autowired IUserRole userRole;
	@Autowired RoleController roleController;
	@Autowired IRole role;
	
	//test admin can grant self and non self roles
	@Test
	public void testAdminCanGrantSelfRoles() throws Exception{
		//1. Create an admin user - There is no giid way of creating an admin user from api as you need an
		//admin user and I dont want to use admin/admin seed data hence we will create a user
		//and grant admin rights by calling the Data access layer directly
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response=new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request,response, null);
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);

		roleController.reloadRoles();
		GenericListEncapsulationEntity<Role> roles = roleController.get();
		
		BoilerplateList<Role> rolesToBeAdded = new BoilerplateList<Role>();
		int maxRoleId = 0;
		for(Role roleData : roles.getEntityList()){
			if(roleData.getRoleName().toUpperCase().equals("ADMIN")){
				rolesToBeAdded.add(roleData);
			
			}
			if(Integer.parseInt(roleData.getId()) > maxRoleId){
				maxRoleId = Integer.parseInt(roleData.getId());
		}
		
			maxRoleId = maxRoleId+10;
		userRole.grantUserRole(externalFacingUserReturned,rolesToBeAdded);
		
		

		//4. Login as admin
		httpRequestInterseptor.preHandle(request,response, null);
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);
		
		//5. Try assigning the said roles
		
		BoilerplateList<String> rolesToBeAddedFromAPI = new BoilerplateList<String>();
		rolesToBeAddedFromAPI.add("SelfAssign1");
		rolesToBeAddedFromAPI.add("NonSelfAssign1");
		GenericListEncapsulationEntity<String> entity = new GenericListEncapsulationEntity<String>();
		entity.setEntityList(rolesToBeAddedFromAPI);
		userRoleController.addUserToRole(session.getExternalFacingUser().getUserId(), entity);
		
		//6. Logout and login again
		httpRequestInterseptor.afterCompletion(request, response, null, null);
		httpRequestInterseptor.preHandle(request,response, null);
		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		session = userController.authenticate(authenticationRequest);
		
		boolean isRole1Found = false;
		
		boolean isRole2Found = false;
		
		boolean isRole3Found = false;
		
		for(Object roleData2 : session.getExternalFacingUser().getRoles()){
			Role roleData3 = (Role)roleData2;
			if(roleData3.getRoleName().toUpperCase().equals("ADMIN")){
				isRole1Found = true;
			}
			
			if(roleData3.getRoleName().toUpperCase().equals("SELFASSIGN1")){
				isRole2Found = true;
			}
			
			if(roleData3.getRoleName().toUpperCase().equals("NONSELFASSIGN1")){
				isRole3Found = true;
			}
		}//end for
		
		Assert.assertTrue(isRole1Found & isRole2Found & isRole3Found);
		}
	}
	
	//test role granter can assign self and non self roles
	@Test
	public void testRoleGranterCanGrantSelfRoles() throws Exception{
		//1. Create an admin user - There is no giid way of creating an admin user from api as you need an
		//admin user and I dont want to use admin/admin seed data hence we will create a user
		//and grant admin rights by calling the Data access layer directly
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response=new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request,response, null);
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);

		roleController.reloadRoles();
		GenericListEncapsulationEntity<Role> roles = roleController.get();
		
		BoilerplateList<Role> rolesToBeAdded = new BoilerplateList<Role>();
		int maxRoleId = 0;
		for(Object roleDataObject : roles.getEntityList().toArray()){
			Role roleData = (Role)roleDataObject;
			if(roleData.getRoleName().toUpperCase().equals("ROLEASSIGNER")){
				rolesToBeAdded.add(roleData);
			
			}
			if(Integer.parseInt(roleData.getId()) > maxRoleId){
				maxRoleId = Integer.parseInt(roleData.getId());
		}
		}//end for
		
			maxRoleId = maxRoleId+10;
		userRole.grantUserRole(externalFacingUserReturned,rolesToBeAdded);
		
		
		//2. Create some roles - self assigning and non self assigning
		List<Role> rolesToBeCreated = new BoilerplateList<Role>();
		Role roleDataA = new Role();
		roleDataA.setRoleName("SelfAssign1");
		roleDataA.setIsSelfAssign(true);
		roleDataA.setIsSystemRole(false);
		roleDataA.setDescription("UT Role");
		
		rolesToBeCreated.add(roleDataA);
		maxRoleId++;
		roleDataA = new Role();
		roleDataA.setRoleName("SelfAssign2");
		roleDataA.setIsSelfAssign(true);
		roleDataA.setIsSystemRole(false);
		roleDataA.setDescription("UT Role");
		rolesToBeCreated.add(roleDataA);
		maxRoleId++;			
			roleDataA = new Role();
			roleDataA.setRoleName("NonSelfAssign1");
			roleDataA.setIsSelfAssign(false);
			roleDataA.setIsSystemRole(false);
			roleDataA.setDescription("UT Role");
			rolesToBeCreated.add(roleDataA);
			maxRoleId++;				
				roleDataA = new Role();
				roleDataA.setRoleName("NonSelfAssign1");
				roleDataA.setIsSelfAssign(false);
				roleDataA.setIsSystemRole(false);
				roleDataA.setDescription("UT Role");
				rolesToBeCreated.add(roleDataA);
				maxRoleId++;
				
		MySQLBaseDataAccessLayer dal = new MySQLBaseDataAccessLayer();
		try{
		dal.create(rolesToBeCreated);
		}catch(Exception ex){
			
		}
		
		//3. Invalidate the role's servce cache
		roleController.reloadRoles();
		httpRequestInterseptor.afterCompletion(request, response, null, null);

		//4. Login as admin
		httpRequestInterseptor.preHandle(request,response, null);
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);
		
		//5. Try assigning the said roles
		
		BoilerplateList<String> rolesToBeAddedFromAPI = new BoilerplateList<String>();
		rolesToBeAddedFromAPI.add("SelfAssign1");
		rolesToBeAddedFromAPI.add("NonSelfAssign1");
		GenericListEncapsulationEntity<String> entity = new GenericListEncapsulationEntity<String>();
		entity.setEntityList(rolesToBeAddedFromAPI);
		userRoleController.addUserToRole(session.getExternalFacingUser().getUserId(), entity);

		
		//6. Logout and login again
		httpRequestInterseptor.afterCompletion(request, response, null, null);
		httpRequestInterseptor.preHandle(request,response, null);
		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		session = userController.authenticate(authenticationRequest);
		
		boolean isRole1Found = false;
		
		boolean isRole2Found = false;
		
		boolean isRole3Found = false;
		
		for(Object roleData2 : session.getExternalFacingUser().getRoles()){
			Role roleData3 = (Role)roleData2;
			if(roleData3.getRoleName().toUpperCase().equals("ROLEASSIGNER")){
				isRole1Found = true;
			}
			
			if(roleData3.getRoleName().toUpperCase().equals("SELFASSIGN1")){
				isRole2Found = true;
			}
			
			if(roleData3.getRoleName().toUpperCase().equals("NONSELFASSIGN1")){
				isRole3Found = true;
			}
		
		}
		Assert.assertTrue(isRole1Found & isRole2Found & isRole3Found);
	}	

	
	//test self granting of self assign role is possible

	@Test
	public void testSelfGrantingOfSelfAssignedRolesIsPossible() throws Exception{
		//1. Create a non admin user - There is no giid way of creating an admin user from api as you need an
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response=new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request,response, null);
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);

	

		//4. Login as the user
		httpRequestInterseptor.preHandle(request,response, null);
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);
		
		//5. Try assigning the said roles
		
		BoilerplateList<String> rolesToBeAddedFromAPI = new BoilerplateList<String>();
		rolesToBeAddedFromAPI.add("SelfAssign1");
		rolesToBeAddedFromAPI.add("SelfAssign2");
		GenericListEncapsulationEntity<String> entity = new GenericListEncapsulationEntity<String>();
		entity.setEntityList(rolesToBeAddedFromAPI);
		userRoleController.addUserToRole(session.getExternalFacingUser().getUserId(), entity);

		
		//6. Logout and login again
		httpRequestInterseptor.afterCompletion(request, response, null, null);
		httpRequestInterseptor.preHandle(request,response, null);
		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		session = userController.authenticate(authenticationRequest);
		
		
		boolean isRole2Found = false;
		
		boolean isRole3Found = false;
		
		for(Object roleData2 : session.getExternalFacingUser().getRoles()){
			Role roleData3 = (Role)roleData2;
			if(roleData3.getRoleName().toUpperCase().equals("SELFASSIGN2")){
				isRole2Found = true;
			}
			
			if(roleData3.getRoleName().toUpperCase().equals("SELFASSIGN1")){
				isRole3Found = true;
			}
			
		
		}
		Assert.assertTrue(isRole2Found & isRole3Found);
	}	
	
	//test self assigning of privilaged role is not possible

	@Test(expected = UnauthorizedException.class)
	public void testSelfGrantingOfNonSelfAssignedRolesIsNonPossible() throws Exception{
		//1. Create a non admin user - There is no giid way of creating an admin user from api as you need an
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response=new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request,response, null);
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);

	

		//4. Login as the user
		httpRequestInterseptor.preHandle(request,response, null);
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);
		
		//5. Try assigning the said roles
		
		BoilerplateList<String> rolesToBeAddedFromAPI = new BoilerplateList<String>();
		rolesToBeAddedFromAPI.add("SelfAssign1");
		rolesToBeAddedFromAPI.add("NonSelfAssign1");
		GenericListEncapsulationEntity<String> entity = new GenericListEncapsulationEntity<String>();
		entity.setEntityList(rolesToBeAddedFromAPI);
		userRoleController.addUserToRole(session.getExternalFacingUser().getUserId(), entity);

	}	
	
	//test a user who hasent got role granter / admin rights cant assign roles to another user
	@Test(expected = UnauthorizedException.class)
	public void testNonGrantersCantGrantRolesToAnyone() throws Exception{
		//1. Create a non admin user - There is no giid way of creating an admin user from api as you need an
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response=new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request,response, null);

		ExternalFacingUser externalFacingUserToBeGranted = new ExternalFacingUser();
		externalFacingUserToBeGranted.setAuthenticationProvider("Default");
		String userIdexternalFacingUserToBeGranted = UUID.randomUUID().toString();
		externalFacingUserToBeGranted.setUserId(userIdexternalFacingUserToBeGranted);
		externalFacingUserToBeGranted.setPassword(userIdexternalFacingUserToBeGranted);
		ExternalFacingUser externalFacingUserToBeGrantedReturned = userController.createUser(externalFacingUserToBeGranted);
		
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);

	

		//4. Login as the user
		httpRequestInterseptor.preHandle(request,response, null);
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);
		
		//5. Try assigning the said roles
		
		BoilerplateList<String> rolesToBeAddedFromAPI = new BoilerplateList<String>();
		rolesToBeAddedFromAPI.add("SelfAssign1");
		rolesToBeAddedFromAPI.add("NonSelfAssign1");
		GenericListEncapsulationEntity<String> entity = new GenericListEncapsulationEntity<String>();
		entity.setEntityList(rolesToBeAddedFromAPI);
		userRoleController.addUserToRole(userIdexternalFacingUserToBeGranted, entity);

	}	
	
	//test admin can grant a user roles
	@Test
	public void testAdminCanGrantOtherUsersRoles() throws Exception{
		//1. Create an admin user - There is no giid way of creating an admin user from api as you need an
		//admin user and I dont want to use admin/admin seed data hence we will create a user
		//and grant admin rights by calling the Data access layer directly
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response=new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request,response, null);
		
		ExternalFacingUser externalFacingUserToBeGranted = new ExternalFacingUser();
		externalFacingUserToBeGranted.setAuthenticationProvider("Default");
		String userIdexternalFacingUserToBeGranted = UUID.randomUUID().toString();
		externalFacingUserToBeGranted.setUserId(userIdexternalFacingUserToBeGranted);
		externalFacingUserToBeGranted.setPassword("password");
		ExternalFacingUser externalFacingUserToBeGrantedReturned = userController.createUser(externalFacingUserToBeGranted);
		
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);

		roleController.reloadRoles();
		GenericListEncapsulationEntity<Role> roles = roleController.get();
		
		BoilerplateList<Role> rolesToBeAdded = new BoilerplateList<Role>();
		int maxRoleId = 0;
		for(Role roleData : roles.getEntityList()){
			if(roleData.getRoleName().toUpperCase().equals("ADMIN")){
				rolesToBeAdded.add(roleData);
			
			}
			if(Integer.parseInt(roleData.getId()) > maxRoleId){
				maxRoleId = Integer.parseInt(roleData.getId());
		}
		}
		
			maxRoleId = maxRoleId+10;
		userRole.grantUserRole(externalFacingUserReturned,rolesToBeAdded);
		
		

		//4. Login as admin
		httpRequestInterseptor.preHandle(request,response, null);
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);
		
		//5. Try assigning the said roles
		
		BoilerplateList<String> rolesToBeAddedFromAPI = new BoilerplateList<String>();
		rolesToBeAddedFromAPI.add("SelfAssign1");
		rolesToBeAddedFromAPI.add("NonSelfAssign1");
		GenericListEncapsulationEntity<String> entity = new GenericListEncapsulationEntity<String>();
		entity.setEntityList(rolesToBeAddedFromAPI);
		userRoleController.addUserToRole(externalFacingUserToBeGranted.getUserId(), entity);
		
		//6. Logout and login again
		httpRequestInterseptor.afterCompletion(request, response, null, null);
		httpRequestInterseptor.preHandle(request,response, null);
		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword("password");
		authenticationRequest.setUserId(externalFacingUserToBeGranted.getUserId());
		session = userController.authenticate(authenticationRequest);
		
		boolean isRole1Found = true;
		
		boolean isRole2Found = false;
		
		boolean isRole3Found = false;
		
		for(Object roleData2 : session.getExternalFacingUser().getRoles()){
			Role roleData3 = (Role)roleData2;
			
			
			if(roleData3.getRoleName().toUpperCase().equals("SELFASSIGN1")){
				isRole2Found = true;
			}
			
			if(roleData3.getRoleName().toUpperCase().equals("NONSELFASSIGN1")){
				isRole3Found = true;
			}
		}//end for
		
		Assert.assertTrue(isRole1Found & isRole2Found & isRole3Found);
		}
	
	
	//test role granter can grant a user roles
	@Test
	public void testRoleAssignerCanGrantOtherUsersRoles() throws Exception{
		//1. Create an admin user - There is no giid way of creating an admin user from api as you need an
		//admin user and I dont want to use admin/admin seed data hence we will create a user
		//and grant admin rights by calling the Data access layer directly
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response=new MockHttpServletResponse();
		httpRequestInterseptor.preHandle(request,response, null);
		
		ExternalFacingUser externalFacingUserToBeGranted = new ExternalFacingUser();
		externalFacingUserToBeGranted.setAuthenticationProvider("Default");
		String userIdexternalFacingUserToBeGranted = UUID.randomUUID().toString();
		externalFacingUserToBeGranted.setUserId(userIdexternalFacingUserToBeGranted);
		externalFacingUserToBeGranted.setPassword("password");
		ExternalFacingUser externalFacingUserToBeGrantedReturned = userController.createUser(externalFacingUserToBeGranted);
		
		
		ExternalFacingUser externalFacingUser = new ExternalFacingUser();
		externalFacingUser.setAuthenticationProvider("Default");
		String userId = UUID.randomUUID().toString();
		externalFacingUser.setUserId(userId);
		externalFacingUser.setPassword(userId);
		ExternalFacingUser externalFacingUserReturned = userController.createUser(externalFacingUser);

		roleController.reloadRoles();
		GenericListEncapsulationEntity<Role> roles = roleController.get();
		
		BoilerplateList<Role> rolesToBeAdded = new BoilerplateList<Role>();
		int maxRoleId = 0;
		for(Role roleData : roles.getEntityList()){
			if(roleData.getRoleName().toUpperCase().equals("ROLEASSIGNER")){
				rolesToBeAdded.add(roleData);
			
			}
			if(Integer.parseInt(roleData.getId()) > maxRoleId){
				maxRoleId = Integer.parseInt(roleData.getId());
		}
		}
		
			maxRoleId = maxRoleId+10;
		userRole.grantUserRole(externalFacingUserReturned,rolesToBeAdded);
		
		

		//4. Login as admin
		httpRequestInterseptor.preHandle(request,response, null);
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword(userId);
		authenticationRequest.setUserId(userId);
		Session session = userController.authenticate(authenticationRequest);
		
		//5. Try assigning the said roles
		
		BoilerplateList<String> rolesToBeAddedFromAPI = new BoilerplateList<String>();
		rolesToBeAddedFromAPI.add("SelfAssign1");
		rolesToBeAddedFromAPI.add("NonSelfAssign1");
		GenericListEncapsulationEntity<String> entity = new GenericListEncapsulationEntity<String>();
		entity.setEntityList(rolesToBeAddedFromAPI);
		userRoleController.addUserToRole(externalFacingUserToBeGranted.getUserId(), entity);
		
		//6. Logout and login again
		httpRequestInterseptor.afterCompletion(request, response, null, null);
		httpRequestInterseptor.preHandle(request,response, null);
		authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword("password");
		authenticationRequest.setUserId(externalFacingUserToBeGranted.getUserId());
		session = userController.authenticate(authenticationRequest);
		
		boolean isRole1Found = true;
		
		boolean isRole2Found = false;
		
		boolean isRole3Found = false;
		
		for(Object roleData2 : session.getExternalFacingUser().getRoles()){
			Role roleData3 = (Role)roleData2;
			
			
			if(roleData3.getRoleName().toUpperCase().equals("SELFASSIGN1")){
				isRole2Found = true;
			}
			
			if(roleData3.getRoleName().toUpperCase().equals("NONSELFASSIGN1")){
				isRole3Found = true;
			}
		}//end for
		
		Assert.assertTrue(isRole1Found & isRole2Found & isRole3Found);
		}
	
	
}