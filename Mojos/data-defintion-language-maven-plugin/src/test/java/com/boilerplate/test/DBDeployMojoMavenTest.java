package com.boilerplate.test;
import java.io.File;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import com.boilerplate.DBDeployMojo;

/**
 * 
 * This class tests the basic execution of the deployDB goal of the data-definition-language maven plugin
 * 
 * @author shrivb
 *
 */
public class DBDeployMojoMavenTest extends AbstractMojoTestCase {
	 private String testPOMSrcPath = "src/test/resources/unit/data-definition-language-mojo/pom.xml";

	public DBDeployMojoMavenTest() {
		
	}
	
	/** {@inheritDoc} */
    protected void setUp()
        throws Exception
    {
        // required
        super.setUp();
        
    }

    /** {@inheritDoc} */
    protected void tearDown()
        throws Exception
    {
        // required
        super.tearDown();
        
    }

    /**
     * @throws Exception if any
     */
    public void testBasicExecutionOfDBDeployMojo()
        throws Exception
    {
    	File pom = getTestFile(testPOMSrcPath);
        assertNotNull(pom);
        assertTrue(pom.exists());

        DBDeployMojo myMojo = (DBDeployMojo) lookupMojo( "deployDB", pom );        
        assertNotNull( myMojo );
        myMojo.execute();       
    }
    
    //TODO - write more negative test cases
	
}
