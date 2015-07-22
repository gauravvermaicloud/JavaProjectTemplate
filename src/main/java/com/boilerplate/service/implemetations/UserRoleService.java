package com.boilerplate.service.implemetations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.Role;
import com.boilerplate.service.interfaces.IRoleService;
import com.boilerplate.service.interfaces.IUserRoleService;
import com.boilerplate.service.interfaces.IUserService;
import com.boilerplate.exceptions.rest.NotFoundException;
/**
 * This class perfomrs user role related function
 * @author gaurav.verma.icloud
 *
 */
public class UserRoleService implements IUserRoleService {

	/**
	 * This is the user service
	 */
	@Autowired
	IUserService userService;
	
	/**
	 * Sets the user service
	 * @param userService The user service
	 */
	public void setUserService(IUserService userService){
		this.userService = userService;
	}
	
	/**
	 * This is the role service
	 */
	@Autowired
	IRoleService roleService;
	
	/**
	 * This sets the role service
	 * @param roleService The role service
	 */
	public void setRoleService(IRoleService roleService){
		this.roleService = roleService;
	}
	
	/**
	 * @throws NotFoundException If the user or role is not found
	 * @see IUserRoleService.grantUserRoles
	 */
	@Override
	public void grantUserRoles(String userId, List<String> roles,
			String granterId) throws NotFoundException {
		
		//check if the granterId is a person who is admin or role granter
		
		//if so then grant all the roles
		
		//if this is a case of annonymous we need to ensure announymous user 
		//can do the required things
		
		//check if the granterId is same as userId
		//check if all roles are self service roles
		//grant the roles
		
		
		//throw unauthorized exception
		BoilerplateList<Role> rolesToBeGranted = new BoilerplateList<Role>();
		boolean isRoleFound = false;
		for(String roleName : roles){
			for(Role role : roleService.getRoles().getEntityList()){
				if(roleName.toUpperCase().equals(role.getRoleName())){
					rolesToBeGranted.add(role);
					isRoleFound = true;
					break;
				}//end if
			}//end inner for
			if(!isRoleFound) throw new NotFoundException("Role"
					, "Role "+roleName+" not found",null);
		}//end for
		
	}
	
	private boolean isUserAdminOrRoleGranter(String granterId){
		return true;
	}
}
