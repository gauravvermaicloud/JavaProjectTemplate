package UnitTests;

import static org.junit.Assert.*;

import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;

import UnitTests.TestDeployedCache.BaseWrapper;

import com.boilerplate.cache.CacheFactory;
import com.boilerplate.java.Base;

public class TestInMemoryCache {

	public class BaseWrapper extends Base{
		public String data = null;
		
	}
	
	@Test
	public void test() throws Exception{
		String key = UUID.randomUUID().toString();
		BaseWrapper baseWrapper = new BaseWrapper();
		baseWrapper.data = key;
		
		CacheFactory.getInstance("BoilerplateNonProductionInMemoryCache").add(key, baseWrapper);
		BaseWrapper returnValue = CacheFactory.getInstance("BoilerplateNonProductionInMemoryCache").get(key,BaseWrapper.class);
		Assert.assertEquals(key, returnValue.data);
		CacheFactory.getInstance("BoilerplateNonProductionInMemoryCache").remove(key);
		returnValue = CacheFactory.getInstance("BoilerplateNonProductionInMemoryCache").get(key,BaseWrapper.class);
		Assert.assertNull(returnValue);

	}

}
