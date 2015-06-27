package UnitTests;

import static org.junit.Assert.*;

import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.boilerplate.cache.CacheFactory;
import com.boilerplate.cache.ICache;
import com.boilerplate.java.Base;

@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-context.xml"})
public class TestDeployedCache {
	
	public class BaseWrapper extends Base{
		public String data = null;
		
	}
	
	@Test
	public void test() throws Exception{
		String key = UUID.randomUUID().toString();
		BaseWrapper baseWrapper = new BaseWrapper();
		baseWrapper.data = key;
		
		CacheFactory.getInstance().add(key, baseWrapper);
		BaseWrapper returnValue = CacheFactory.getInstance().get(key,BaseWrapper.class);
		Assert.assertEquals(key, returnValue.data);
		CacheFactory.getInstance().remove(key);
		returnValue = CacheFactory.getInstance().get(key,BaseWrapper.class);
		Assert.assertNull(returnValue);
	}

}
