/**
 * 
 */
package com.boilerplate.test;

import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.WithoutMojo;

import java.io.File;

import org.junit.Rule;

import static org.junit.Assert.*;

import org.junit.Test;

import com.boilerplate.DBDeployMojo;

public class DDLMojoJUnitTest {
	private String testPOMSrcPath = "src/test/resources/unit/data-definition-language-mojo/pom.xml";
	
    @Rule
    public MojoRule rule = new MojoRule()
    {
      @Override
      protected void before() throws Throwable 
      {
      }

      @Override
      protected void after()
      {
      }
    };

    /**
     * @throws Exception if any
     */
    @Test
    public void testSomething()
        throws Exception
    {

        DBDeployMojo myMojo = (DBDeployMojo) rule.lookupMojo( "deployDB", testPOMSrcPath );
        
        assertNotNull( myMojo );
        myMojo.execute();

        
    }

    /** Do not need the MojoRule. */
    @WithoutMojo
    @Test
    public void testSomethingWhichDoesNotNeedTheMojoAndProbablyShouldBeExtractedIntoANewClassOfItsOwn()
    {
      
    }

}

