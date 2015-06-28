package com.boilerplate.cache;

import com.boilerplate.java.Base;

/**
 * This interface is used to manage cache.
 * @author gaurav
 */
public interface ICache {
	/**
	 * This method is used to add a new item into cache
	 * @param key This is the key of the object
	 * @param value This is the value to be added to the cache
	 */
	public <T extends Base> void add(String key,T value);
	
	/**
	 * Sets an item on cache with expiry 
	 * @param key The key of the object
	 * @param value The value of object
	 * @param timeoutInSeconds Time out
	 */
	public <T extends Base> void add(String key,T value, int timeoutInSeconds);
	/**
	 * This method is used to read an item from cache
	 * @param key This is the key of the item
	 * @param typeOfClass The type of the class
	 * @return This is the value associated with the key.
	 */
	public <T extends Base> T get(String key,Class<T> typeOfClass);
	
	/**
	 * This removes the item from cache. If one is found
	 * If no items are present in cache then nothing is done.
	 * @param key This is the key to be removed
	 */
	public void remove(String key);
	
	/**
	 * This method tells if the cache is enabled.
	 * @return
	 */
	public boolean isCacheEnabled();

	/**
	 * Reset the cache if it was disabled due to errors.
	 */
	public void resetCacheExceptionCount();
}
