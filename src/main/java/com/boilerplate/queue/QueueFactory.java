package com.boilerplate.queue;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * This method returns a queue
 * @author gaurav
 */
public class QueueFactory {
	
	/**
	 * Returns an instance of the default queue type
	 * @return The instance of queue
	 */
	public static IQueue getInstance() throws Exception{
		//TODO - Read this from config
		String queueProvider = "BoilerplateNonProductionInMemoryQueue";
		return QueueFactory.getInstance(queueProvider);
	}
	
	/**
	 * Returns an instance of given queue provider type.
	 * @param queueProvider The queue provider.
	 * @return The instance of queue
	 * @throws Exception 
	 */
	public static IQueue getInstance(String queueProvider) throws Exception{
		switch (queueProvider){
		// based on queue provider return an instance
		case "BoilerplateNonProductionInMemoryQueue" : 
			return BoilerplateNonProductionInMemoryQueue.getInstance();
		case "RabbitMQ" :
			return RabbitMQ.getInstance();
		}
		throw new NotImplementedException();
	}
}
