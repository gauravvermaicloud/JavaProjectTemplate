package com.boilerplate.java.controllers;

import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.Department;

/**
 * This class is used to create departments for users.
 * @author gaurav
 *
 */
public class DepartmentController extends BaseController{

	/**
	 * This method creates a new department
	 * @param department The instance of the department to be created. The
	 * department name is expected to be present
	 * @return An instance of the created department.
	 */
	public Department create(Department department){
		return department;
	}
	
	/**
	 * This method deletes a department given its id
	 * @param departmentId  The id of the department.
	 */
	public void delete(String departmentId){
		
	}
	
	/**
	 * Returns a list of all departments.
	 */
	public BoilerplateList<Department> getAll(){
		return null;
	}
	
	/**
	 * This method returns a department given the id.
	 * @param departmentId This is the id of the department.
	 */
	public Department get(String departmentId){
		return null;
	}
}
