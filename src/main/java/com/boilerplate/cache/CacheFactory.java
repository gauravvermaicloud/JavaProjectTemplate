package com.boilerplate.cache;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class CacheFactory {
	public static ICache getInstance() throws Exception{
		//TODO - Write code to get cache from configuration in DB
		String cacheProvider = "BoilerplateNonProductionInMemoryCache";
		return CacheFactory.getInstance(cacheProvider);
	}
	
	public static ICache getInstance(String cacheProvider)throws Exception{
		switch (cacheProvider){
		case "BoilerplateNonProductionInMemoryCache" : 
			return BoilerplateNonProductionInMemoryCache.getInstance();
		case "RedisCache":
			return RedisCache.getInstance();
		case "MemCache":
			return MemCache.getInstance();
		}
		throw new NotImplementedException();
	}
}
