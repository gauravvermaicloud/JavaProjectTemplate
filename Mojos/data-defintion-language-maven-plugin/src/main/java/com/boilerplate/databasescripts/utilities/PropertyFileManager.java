/**
 * 
 */
package com.boilerplate.databasescripts.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * This class is a helper class to manage(basically read) the properties for this plugin
 * @author shrivb
 *
 */
public class PropertyFileManager {	
	/**
	 * A KVP to hold the representation of the properties file's contents
	 */
	HashMap<String,String> properties;
	
	/**
	 * Initializes the PropertyFileManager object
	 * @throws Exception we throw exception here because if anything wrong happens here, the caller needs to handle and take action, possibly use a fallback configuration value
	 */
	public void initialize() throws Exception
	{
		properties = new HashMap<String,String>();	
		loadPropertyFile();
	}
	
	/**
	 * This method returns the value in the property file for the given key
	 * @param key The key of the property 
	 * @return The value of the property
	 */
	public String getPropertyValue(String key)
	{
		return this.properties.get(key);
	}
	
	/**
	 * This method reads the property file and loads the contents of the property file into a KVP collection	  
	 * @throws IOException throw any exception which happens while reading the file
	 */
	private void loadPropertyFile() throws IOException {
		Properties properties  = null;		
		//Using the .properties file in the class path load the file
		//into the properties class
		try(InputStream inputStream = 
				this.getClass().getClassLoader().getResourceAsStream("datadefintitionplugin.properties")){
			properties = new Properties();			
			properties.load(inputStream);
			for(String key : properties.stringPropertyNames()){
				this.properties.put(key, properties.getProperty(key));
			}
		}
		catch (IOException e) {			
			e.printStackTrace();
			//throwing of exception here is in stark contrast to the boilerplate property file handling mechanism
			//since the plugin codebase is a smaller one and properties being few, expect the plugin code to handle 
			//errors and use an alternative property value
			throw e;
		}	
		//no finally needed - try-with-resources would take care of it. 
	}

}
