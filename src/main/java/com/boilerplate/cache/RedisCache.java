package com.boilerplate.cache;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

import com.boilerplate.framework.Logger;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.controllers.WidgetController;

/**
 * This is a singleton access point for the Redis cache.
 * @author gaurav
 *
 */
public class RedisCache  extends BaseCache implements ICache{
	
	private static Logger  logger = Logger.getInstance(RedisCache.class);
	
	/**
	 * Static instance of cache for singleton
	 */
	private static RedisCache redisCache;
	
	/**
	 * The time after which cache should expire.
	 */
	private int timeOutInSeconds;
	
	/**
	 * This is the jedis command object
	 */
	private JedisCommands jedisCommands;
	
	/**
	 * This is the number of times cache had an error
	 */
	private int cacheExceptionCount =0;
	
	/**
	 * This is the maximum number of exceptions that can occur after
	 * which cache is turned off
	 */
	private int maximumCacheExceptionCount=0;
	
	/**
	 * The prefix for the cache.
	 */
	private static String cacheKeyPrefix;
	
	@Override
	public void resetCacheExceptionCount(){
		this.cacheExceptionCount = 0;
	}
	
	/**
	 * This creates an instance of the cache
	 * @param configurations This is the configs to the cache
	 */
	private RedisCache(BoilerplateMap<String,String> configurations){	
		//First check if this is just one local machine or a cluster
		
		//This is the connections to redis it is expected in format host:port;host:port;...
		String redisConnections = configurations.get("CacheServer");
		int cacheTimeoutInMinutes = Integer.parseInt(configurations.get("CacheTimeoutInMinutes"));
		this.cacheExceptionCount = 0;
		this.maximumCacheExceptionCount = Integer.parseInt(configurations.get("MaximumCacheExceptionCount"));
		
		this.timeOutInSeconds = cacheTimeoutInMinutes*60;
		String[] hostPort;
		String host;
		int port;
		//split the connections with ;
		String[] connections = redisConnections.split(";");
		//if there are more than one connections we have a cluster
		if(connections.length>1){
			logger.logInfo("RedisCache", "RedisCache", "Creating Cluster",connections.toString());
			Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
			//for each server in cluster
			for(String connection : connections){
				//find the host and the port
				hostPort = connection.split(":");
				host = hostPort[0];
				port = Integer.parseInt(hostPort[1]);
				//and add it to cluster nodes
				jedisClusterNodes.add(new HostAndPort(host, port));
				if(logger.isDebugEnabled()){
					logger.logDebug("RedisCache", "RedisCache", "Creating Cluster","Adding to cluster -"+hostPort);
				}
			}
			//create a command using cluster
			jedisCommands = new JedisCluster(jedisClusterNodes,this.timeOutInSeconds);
		}
		else //if there is only one server
		{
			//then find host and port
			hostPort = connections[0].split(":");
			host = hostPort[0];
			port = Integer.parseInt(hostPort[1]);
			//and create a non clustered command
			jedisCommands = new Jedis(host,port,this.timeOutInSeconds);
			logger.logInfo("RedisCache", "RedisCache", "Creating Single",host+":"+port);
		}
		RedisCache.cacheKeyPrefix = BaseCache.getCachePrefix();
	}
	
	/**
	 * This method returns an instance of the redis cache
	 * @return a singleton instance of the cache
	 */
	public static RedisCache getInstance(BoilerplateMap<String, String>confirgurations
			) throws Exception{
		try{
		//if the cacher is null then we have called this method for 1st time
		if(RedisCache.redisCache ==null){
			//only one thread will be allowed so that map is made thread safe and is now loaded
			//again  and again
			synchronized (RedisCache.class) {
				//it is possible that two threads came into this class and at same time
				//and when 2nd thread his this line of code the cache  was 
				//fully prepared hence we check this null again
				if(RedisCache.redisCache ==null){
					//create a new instance of the cache
					RedisCache.redisCache = new RedisCache(confirgurations);
				}//end 2nd if check
			}//end sync block
		}//end 1st if check
		//return the said insamce
		return RedisCache.redisCache;
		}
		catch(Exception ex){
			logger.logException("RedisCache", "getInstance", "catch",
					"Error in getting instance", ex);
			RedisCache.redisCache.cacheExceptionCount++;
			throw ex;
		}
	}
	
	/**
	 *@see ICache.add 
	 */
	@Override
	public <T extends Base> void add(String key, T value) {
		if(this.isCacheEnabled()){
			try{
				this.jedisCommands.setex(this.cacheKeyPrefix+":"+key, this.timeOutInSeconds, value.toJSON());
				if(this.cacheExceptionCount>0){
					this.cacheExceptionCount =0;
				}
			}
			catch(Exception ex){
				//The reason we do not throw the exceptions is because 
				//we expect the code to work without cache
				this.cacheExceptionCount++;
				logger.logException("RedisCache", "add", "Catch Block"
						, "Exception in adding a new key", ex);
			}
		}
	}

	/**
	 * @see ICache.add
	 */
	@Override
	public <T extends Base> void add(String key,T value, int timeoutInSeconds){
		if(this.isCacheEnabled()){
			try{
				this.jedisCommands.setex(this.cacheKeyPrefix+":"+key, timeoutInSeconds, value.toJSON());
				if(this.cacheExceptionCount>0){
					this.cacheExceptionCount =0;
				}
			}
			catch(Exception ex){
				//The reason we do not throw the exceptions is because 
				//we expect the code to work without cache
				this.cacheExceptionCount++;
				logger.logException("RedisCache", "add", "Catch Block"
						, "Exception in adding a new key", ex);
			}
		}
	}
	
	
	/**
	 * @see ICache.get
	 */
	@Override
	public <T extends Base> T get(String key,Class<T> typeOfClass) {
		T t = null;
		if(this.isCacheEnabled()){
			try{
				t= Base.fromJSON(this.jedisCommands.get(this.cacheKeyPrefix+":"+key),typeOfClass);
				this.jedisCommands.expire(key, this.timeOutInSeconds);
				if(this.cacheExceptionCount>0){
					this.cacheExceptionCount =0;
				}
			}
			catch(Exception ex){
				//The reason we do not throw the exceptions is because 
				//we expect the code to work without cache
				this.cacheExceptionCount++;
				logger.logException("RedisCache", "get", "Catch Block"
						, "Exception in getting", ex);
			}
		}
		return t;
	}

	/**
	 * @see ICache.remove
	 */
	@Override
	public void remove(String key) {
		if(this.isCacheEnabled()){
			try{
				this.jedisCommands.del(this.cacheKeyPrefix+":"+key);
				if(this.cacheExceptionCount>0){
					this.cacheExceptionCount =0;
				}
			}
			catch(Exception ex){
				//The reason we do not throw the exceptions is because 
				//we expect the code to work without cache
				this.cacheExceptionCount++;
				logger.logException("RedisCache", "add", "Catch Block"
						, "Exception in remove", ex);
			}
		}
	}

	/**
	 * @see ICache.isCacheEnabled
	 */
	@Override
	public boolean isCacheEnabled() {
		return this.cacheExceptionCount <= this.maximumCacheExceptionCount;
	}
}
