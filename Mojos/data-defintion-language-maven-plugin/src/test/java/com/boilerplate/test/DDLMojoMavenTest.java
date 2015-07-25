package com.boilerplate.test;
import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import com.boilerplate.DBDeployMojo;


public class DDLMojoMavenTest extends AbstractMojoTestCase {
	 private String testPOMSrcPath = "src/test/resources/unit/data-definition-language-mojo/pom.xml";

	public DDLMojoMavenTest() {
		// TODO Auto-generated constructor stub
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
    public void testSomething()
        throws Exception
    {
    	assertTrue(true);    	
        File pom = getTestFile(testPOMSrcPath);
        assertNotNull( pom );
        assertTrue( pom.exists() );

        DBDeployMojo myMojo = (DBDeployMojo) lookupMojo( "deployDB", pom );        
        assertNotNull( myMojo );
        myMojo.execute();

        
    }
	
	
}
