/**
 * 
 */
package com.boilerplate.dbscript.interfaces;
/**
 * @author Vignesh
 *
 */
public abstract class DBScriptService {	
	
	PreScriptGenerator _preScriptGenerator;
	DDLScriptGenerator _ddlScriptGenerator;
	PostScriptGenerator _postScriptGenerator;		
	
	public abstract void generateScripts(DBInstanceInfo dbInstanceInfo,String sourceXMLPath, String destinationFilePath);

	
	
	
	
}
