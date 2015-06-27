package UnitTests;

import junit.framework.Assert;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.boilerplate.framework.HibernateUtility;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.controllers.HealthController;

@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-context.xml"})
public class TestConfiguration {
	
	@Autowired 
	HealthController healthController;
	
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;
	
	@Test
	public void testConfigurationValues() throws Exception{
		BoilerplateMap< String, String> configurations
			= healthController.getConfigurations();
		//All env and all version
		Assert.assertEquals(configurations.get("V_All_All"), "V_All_All");
		//All env and v1
		//Assert.assertEquals(configurations.get("V1_All_B"), "V1_All_B");
		//env dev and All v
		Assert.assertEquals(configurations.get("V_All_Dev"), "V_All_Dev");
		//env dev and v1
		//Assert.assertEquals(configurations.get("V1_Dev"), "V1_Dev");
		//envQA and v1
		Assert.assertNull(configurations.get("V1_QA"));
		//envDev and v2
		Assert.assertNull(configurations.get("V2_Dev"));
		
		Assert.assertEquals(configurations.get("DefaultAuthenticationProvider"), "DEFAULT");
		
		Assert.assertEquals("1", configurations.get("Version")); //testing values from.propeorties file
		Assert.assertNull(configurations.get("D")); //testing values which do not match current versioncs
		Assert.assertNotNull(configurations.get("PATH"));//testing values from enviornment variables
		Assert.assertNull(configurations.get(UUID.randomUUID().toString())); //check values that do not exist
	}
}
