package com.boilerplate.service.interfaces;

import com.boilerplate.java.entities.Ping;

public interface IPingService {
	public void getCacheStatus(Ping ping);
	
	public void getQueueStatus(Ping ping);
	
	public void getDataBaseStatus(Ping ping);
	
	public void getBackgroundJobStatus(Ping ping);
	
	//public void setDatabasePinger(IDatabasePinger iDatabasePinger);
}
