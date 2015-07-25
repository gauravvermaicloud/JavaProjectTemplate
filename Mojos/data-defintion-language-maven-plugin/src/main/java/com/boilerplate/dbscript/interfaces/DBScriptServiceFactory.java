/**
 * 
 */
package com.boilerplate.dbscript.interfaces;

/**
 * @author Vignesh
 *
 */
public abstract class DBScriptServiceFactory {

	public abstract DBDeployer createDBDeployer();
	public abstract DBScriptService createDBScriptService();
	
}
