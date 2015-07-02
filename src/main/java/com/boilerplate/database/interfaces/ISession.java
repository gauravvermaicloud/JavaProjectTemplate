package com.boilerplate.database.interfaces;

import com.boilerplate.sessions.Session;

public interface ISession {
	
	/**
	 * This method saves a session in the database
	 * @param session The session to be saved
	 * @return A session
	 */
	public Session create(Session session);
}
