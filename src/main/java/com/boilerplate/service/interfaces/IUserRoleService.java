package com.boilerplate.service.interfaces;

import java.util.List;

import com.boilerplate.exceptions.rest.NotFoundException;

public interface IUserRoleService {
	/**
	 * This method grants the user roles
	 * @param userId The id of the user to be granted roles
	 * @param roles The roles to be granted
	 * @param granterId This is the user who is performing the operation.
	 * Some roles can only be granted by priviglaed users
	 * @throws NotFoundException This is thrown if the user or the role is not found
	 */
	public void grantUserRoles(String userId, List<String> roles, String granterId) 
			throws  NotFoundException;
}
