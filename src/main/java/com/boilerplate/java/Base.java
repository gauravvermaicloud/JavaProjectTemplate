package com.boilerplate.java;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.stream.Stream;

import com.google.gson.Gson;

/**
 * This is the base class of all the classes in the boiler plate code. 
 * All classes are expected to derive from this class.
 * It is recommended that methods in this class not be used for production grade usage, they should be 
 * used only for debugging and instrumentation (exception for toJSON and fromJSON) this is because they have been
 * implemented using generic streams and can have a performance penalty.
 * @author gaurav
 */
public class Base implements Serializable, Cloneable {
	/**
	 * This class returns a string equivalent of the object
	 * @return The string representation of the object
	 */
	@Override
	public String toString(){
		return this.toJSON();
	}
	
	/**
	 * This method converts this object into a JSON
	 * @return A JSON String
	 */
	public String toJSON(){
		return Base.toJSON(this);
	}
	
	/**
	 * This method converts an object to JSON
	 * @param object This is the object
	 * @return A JSON String.
	 */
	public static String toJSON(Object object){
		return new Gson().toJson(object);
	}
	/**
	 * This method returns an object given the JSON.
	 * @param string This is the sting JSON
	 * @param type This is the type
	 * @return An instance of the object
	 */
	public static<T> T fromJSON(String string, Class<T> type){
		return new Gson().fromJson(string,type);
	}
	
	/**
	 * This method creates a deep copy of the object.
	 * The object should be checked for null.
	 * @return A copy of this object
	 */
	@Override
	public Base clone(){
		return (Base)Base.clone(this);
	}
	
	/**
	 * This method creates a clone of a given object.
	 * @param object This is the object to be cloned
	 * @return This returns an instance of the cloned object
	 */
	public static <T> T clone(T object) {
		return (T)Base.fromJSON(Base.toJSON(object), object.getClass());
	}

	/**
	 * This method checks if the two objects this and the parameter are equal.
	 * @return This method returns true if the two objects are equal else false.
	 * @return True if the objects are equal else false
	 */
	@Override
	public boolean equals(Object obj){
		if(this == obj) return true;
		if(this.getClass() == obj.getClass()){
			if(this.toString().equals(obj.toString())){
				return true;
			}
			else{
				return false;
			}
		}
		return false;
	}
	
	/**
	 * This method returns the hash code of the class
	 */
	@Override
	public int hashCode(){
		return this.toJSON().hashCode();
	}
}
