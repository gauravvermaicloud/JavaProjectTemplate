package com.boilerplate.framework;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtility {
	private static SessionFactory sessionFactory =
			buildSessionFactory();
	

	private static SessionFactory buildSessionFactory(){
		try{
			
			Configuration configuration = new Configuration();
		    configuration.configure("mysql.hibernate.cfg.xml");

		    ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
		            configuration.getProperties()).build();
		    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
           /*return new Configuration().configure().buildSessionFactory(
			    new StandardServiceRegistryBuilder().build() );*/
		    return sessionFactory;
		}
		catch(Exception ex){
			System.out.println(ex.toString());
		}
		return null;
	}
	
	public static SessionFactory getSessionFactory(){
		return HibernateUtility.sessionFactory;
	}
	
	public static void close(){
		HibernateUtility.sessionFactory.close();
	}
}
