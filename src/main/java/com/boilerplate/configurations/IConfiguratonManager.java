package com.boilerplate.configurations;

import com.boilerplate.database.interfaces.IConfigurations;
import com.boilerplate.java.collections.BoilerplateMap;

public interface IConfiguratonManager {
	
	public void setDatabaseConfiguration(IConfigurations databaseConfiguration);
	
	public void initialize();
	
	public String get(String key);
	
	public  BoilerplateMap<String,String> getConfigurations();
	
	public void resetConfiguration();
}
