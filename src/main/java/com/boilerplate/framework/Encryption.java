package com.boilerplate.framework;

/**
 * This class will provide simple encryption wrappers.
 * @author gaurav
 *
 */
public class Encryption {

	/**
	 * This class is expected to have all members static
	 * hence making the contrsuctor private
	 */
	private Encryption(){
		
	}
	
	/**
	 * This method returns a hashcode for the object
	 * @param obj The object whose hash code is to be returned
	 * @return A hash code
	 */
	public static long getHashCode(Object obj){
		//TODO - This is a bad way to get any hash, fix this and start returning md5 hash or something
		return obj.hashCode();
	}
}
