package com.boilerplate.database.interfaces;

import java.util.List;

import com.boilerplate.java.entities.Role;

public interface IUserRole {
	/**
	 * Grants given set of roles to the user
	 * @param userId This is the id of the user to whom roles are being granted
	 * @param roles This is the set of role
	 */
	public void grantUserRole(String userId, List<Role> roles);
}
