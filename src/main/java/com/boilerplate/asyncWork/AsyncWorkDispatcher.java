package com.boilerplate.asyncWork;

import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.jobs.QueueReaderJob;

/**
 * This class dispatches work items to all observers of the payload subject.
 * It is expected that an async request will have zero or more subjects.
 * This class will read all observers and send them message payload for processing.
 * This class will also send messssages to observers which listen to ALL subjects
 * @author gaurav
 *
 */
public class AsyncWorkDispatcher {
	
	/**
	 * This is the logger
	 */
	private Logger logger = Logger.getInstance(AsyncWorkDispatcher.class);
	
	/**
	 * This is the dispatch map which contains list of observers for each subject
	 */
	private BoilerplateMap<String,AsyncWorkDispatcherObserverList> dispatchMap = 
			new BoilerplateMap();

	/**
	 * Gets the dispatch map
	 * @return An instance of dispatch map
	 */
	public BoilerplateMap<String,AsyncWorkDispatcherObserverList> getDispatchMap() {
		return dispatchMap;
	}

	/**
	 * Sets the dispatch map
	 * @param dispatchMap Instance of dispatch map
	 */
	public void setDispatchMap(BoilerplateMap<String,AsyncWorkDispatcherObserverList> dispatchMap) {
		this.dispatchMap = dispatchMap;
	}
	
	/**
	 * This method dispatches work item to all interested subjects.
	 * Messages are publised to all observers which are in subject list
	 * and the observers in list ALL. If however the subject NONE is present then
	 * message is not published to anyone
	 * @param workItem the work item
	 */
	public void dispatch(AsyncWorkItem workItem){
		//if the subject of type NONE is present then do not publish to anyone
		if(workItem.getSubjects().contains("NONE")) return;
		
		//Get the list of all observers.
		AsyncWorkDispatcherObserverList allList = dispatchMap.get("ALL");
		//in parallel on multiple threads send request
		allList.getAsyncJobList().forEach(x-> 
			{
				//log the message
				if(logger.isDebugEnabled()){
					logger.logDebug("AsyncWorkDispatcher", "dispatch"
							, "Subject : ALL"
							, "Dispatching "+workItem ==null?"Null":workItem.toJSON()+"to observer "+x.getClass());
				}
				//observer
				((IAsyncWorkObserver)x).observe(workItem);
			}
		);
		workItem.getSubjects().forEach(y->{
			AsyncWorkDispatcherObserverList observerListForSubject = dispatchMap.get(y);
			observerListForSubject.getAsyncJobList().forEach(x-> 
			{
				//log the message
				if(logger.isDebugEnabled()){
					logger.logDebug("AsyncWorkDispatcher", "dispatch"
							, "Subject : ALL"
							, "Dispatching "+workItem ==null?"Null":workItem.toJSON()+"to observer "+x.getClass());
				}
				//observer
				((IAsyncWorkObserver)x).observe(workItem);
			}
		);
		});
	}
}
