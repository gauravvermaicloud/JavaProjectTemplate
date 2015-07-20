package com.boilerplate.service.implemetations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IRole;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.Role;
import com.boilerplate.service.interfaces.IRoleService;

public class RoleService implements IRoleService{

	/**
	 * The list of roles,they are initialized for performance
	 */
	static List<Role> roles = null;
	
	/**
	 * The DAL layer for role
	 */
	@Autowired
	IRole role;
	
	public void setRole(IRole role){
		this.role =role;
	}
	
	/**
	 * This method gets all the roles
	 */
	@Override
	public List<Role> getRoles() {
		if(roles == null){
			roles = role.getRoles();
		}
		return roles;
	}
	
	/**
	 * initializes the roles
	 */
	public void initialize(){
		roles = role.getRoles();
	}

}
