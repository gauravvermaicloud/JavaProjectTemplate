package com.boilerplate.service.interfaces;

import com.boilerplate.java.entities.Ping;
/**
 * This service is implemented by any system implementing ping.
 * We can make this very generic in whch case we can have a list of status reply systems
 * all implementing IPing and being configured from beans in spring, however as these will
 * not change very often this should be okey.
 * @author gaurav.verma.icloud
 *
 */
public interface IPingService {
	/**
	 * Sets the status of ping
	 */
	public void setPingStatus(Ping ping);
}
