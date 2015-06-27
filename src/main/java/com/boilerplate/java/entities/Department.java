package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class mantains a department.
 * @author gaurav
 *
 */
public class Department extends BaseEntity{

	/**
	 * This is the name of the department.
	 */
	String departmentName;
	
	/**
	 * This method gets department name
	 * @return
	 */
	public String getDepartmentName() {
		return departmentName;
	}

	/**
	 * This method sets a department name
	 * @param departmentName The name of the department
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	/**
	 * This method validates a department.
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		return !(departmentName ==null && departmentName.equalsIgnoreCase(""));
	}

	@Override
	public BaseEntity transformToInternal() {
		return this;
	}

	@Override
	public BaseEntity transformToExternal() {
		return this;
	}

}
