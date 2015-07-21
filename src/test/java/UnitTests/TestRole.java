package UnitTests;


import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.util.Assert;

import com.boilerplate.exceptions.BaseBoilerplateException;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.controllers.RoleController;
import com.boilerplate.java.entities.Role;
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-context.xml"})

public class TestRole {

	@Autowired
	RoleController roleController;
	
	@Test
	public void testGetRoles() throws BaseBoilerplateException{
		List<Role> roles= roleController.get().getEntityList();
		//check has admin role
		
		boolean isAdminFound= false;
		boolean isRoleGranterFound=false;
		for(int i =0;i<roles.size();i++){
			Role r = (Role) roles.get(i);
		}
		for(Object obj: roles){
			System.out.println(obj.getClass());
			Role role = (Role)obj;
			if(role.getRoleName().equals("Admin")){
				isAdminFound = true;
			}
			
			if(role.getRoleName().equals("RoleAssigner")){
				isRoleGranterFound = true;
			}
		}
		//check has Role assigner role
		Assert.isTrue(isAdminFound);
		Assert.isTrue(isRoleGranterFound);
	}
}
