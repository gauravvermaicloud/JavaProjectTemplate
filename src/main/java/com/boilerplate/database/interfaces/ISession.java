package com.boilerplate.database.interfaces;

import com.boilerplate.sessions.Session;

public interface ISession {
	
	/**
	 * This method saves a session in the database
	 * @param session The session to be saved
	 * @return A session
	 */
	public Session create(Session session);

	/**
	 * This method gets the given session by id from the database
	 * @param sessionId The id of the session
	 * @return The session
	 */
	public Session getSession(String sessionId);
}
