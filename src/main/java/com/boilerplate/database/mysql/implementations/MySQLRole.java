package com.boilerplate.database.mysql.implementations;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.boilerplate.database.interfaces.IRole;
import com.boilerplate.framework.HibernateUtility;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.Role;

public class MySQLRole extends MySQLBaseDataAccessLayer implements IRole {

	/**
	 * @see IRole.getRoles
	 */
	@Override
	public List<Role> getRoles() {
		Session session =null;
		try{
			//open a session
			session = HibernateUtility.getSessionFactory().openSession();
			//begin a transaction
			Transaction transaction = session.beginTransaction();
			List<Role> roles = session.createCriteria(Role.class).list();					
			transaction.commit();
			return roles;
		}
		finally
		{
			if(session !=null && session.isOpen()){
				session.close();
			}
		}//end finally
	}

}
