package com.boilerplate.service.implemetations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.Role;
import com.boilerplate.service.interfaces.IRoleService;
import com.boilerplate.service.interfaces.IUserRoleService;
import com.boilerplate.service.interfaces.IUserService;
import com.boilerplate.database.interfaces.IUserRole;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
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
	 * This is the user role database access layer
	 */
	@Autowired
	private IUserRole userRole;
	
	/**
	 * This sets the user role
	 * @param userRole This is user role
	 */
	public void setUserRole(IUserRole userRole){
		this.userRole = userRole;
	}
	
	/**
	 * @throws NotFoundException If the user or role is not found
	 * @see IUserRoleService.grantUserRoles
	 */
	@Override
	public void grantUserRoles(String userId, List<String> roles,
			ExternalFacingReturnedUser granter) throws NotFoundException,UnauthorizedException {
		
		//check if the granterId is a person who is admin or role granter
		boolean canGrantRoles = isGrantingUserAdminOrRoleGranter(granter);
				
		//check if the granterId is same as userId
		if(!canGrantRoles){
			if(!granter.getUserId().equals(userId)){
				throw new UnauthorizedException("User", "The user "+granter.getUserId()
						+"cant grant roles to "+userId, null);
			}
		}
		//check if all roles are self service roles
		BoilerplateList<Role> rolesToBeGranted = new BoilerplateList<Role>();
		Role role;
		for(String roleName : roles){
			role = roleService.getRoleNameMap().get(roleName.toUpperCase());
			if(role == null) throw new NotFoundException("Role"
					, "Role "+roleName+" not found",null);
			if(!canGrantRoles){
				//if this is the case of self granting and user is not a granter
				if(!role.getIsSelfAssign()){
					throw new UnauthorizedException("User", "The user "+granter.getUserId()
							+"cant grant roles  "+roleName+
							" because it is not a self assign role"
							, null);
				}
			}
			rolesToBeGranted.add(role);
			//check if the user can grant the role
		}//end for
		
		//call the database and grant the said user given roles
		userId = userService.normalizeUserId(userId);
		ExternalFacingUser userToBeGrantedRoles = this.getUser(userId, granter);
		//finally write to the database
		userRole.grantUserRole(userToBeGrantedRoles, rolesToBeGranted);
	}
	
	/**
	 * 
	 * @param userId
	 * @param user
	 * @return
	 * @throws NotFoundException
	 */
	private ExternalFacingUser getUser(String userId, ExternalFacingUser user) 
			throws NotFoundException{
		if(user.getUserId().equals(userId)){
			return user;
		}
		else{
			return userService.get(userId);
		}
	}
	
	private boolean isGrantingUserAdminOrRoleGranter(ExternalFacingReturnedUser granter){
		boolean isAdminOrRoleGranter = false;
		for(Role role:(List<Role>) granter.getRoles()){
			String roleName = role.getRoleName();
			if(roleName.toUpperCase().equals("ADMIN")){
				isAdminOrRoleGranter = true;
				break;
			}
			if(roleName.toUpperCase().equals("ROLEASSIGNER")){
				isAdminOrRoleGranter = true;
				break;
			}
		}
		return isAdminOrRoleGranter;
	}
	
}
