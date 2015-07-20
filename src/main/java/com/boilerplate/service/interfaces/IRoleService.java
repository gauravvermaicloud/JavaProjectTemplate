package com.boilerplate.service.interfaces;

import java.util.List;

import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.Role;

/**
 * This interface implements the code for roles
 * @author gaurav.verma.icloud
 *
 */
public interface IRoleService {
	/**
	 * This method returns the roles
	 * @return The list of roles
	 */
	List<Role> getRoles();
}
