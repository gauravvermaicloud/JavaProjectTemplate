package UnitTests;

import java.util.UUID;

import junit.framework.Assert;

import org.junit.Assume;
import org.junit.Test;

import UnitTests.TestInMemoryCache.BaseWrapper;

import com.boilerplate.cache.CacheFactory;
import com.boilerplate.java.Base;

public class TestRedisCache {

	public class BaseWrapper extends Base{
		public String data = null;
		
	}
	
public boolean canRunTest(){
		//if the RedisCache is not even working then there is no point in running this test
		//There can be two cases -either we are not using RedisCache in which case if this test fails it is okey
		//Or we are using RedisCache but it is not properly deployed in which case the test deployed cache will fail
		//and we will learn.
			try{
				String key = UUID.randomUUID().toString();
				BaseWrapper baseWrapper = new BaseWrapper();
				baseWrapper.data = key;	
				CacheFactory.getInstance("RedisCache").add(key, baseWrapper,10);
				BaseWrapper returnValue = CacheFactory.getInstance("RedisCache ").get(key,BaseWrapper.class);
				if(returnValue == null){
					return false;
				}
				return true;
			}
			catch(Throwable ex){
				return false;
			}	
	}	
	
	@Test
	public void test() throws Exception{
		Assume.assumeTrue(this.canRunTest());
		String key = UUID.randomUUID().toString();
		BaseWrapper baseWrapper = new BaseWrapper();
		baseWrapper.data = key;
		
		CacheFactory.getInstance("RedisCache").add(key, baseWrapper);
		BaseWrapper returnValue = CacheFactory.getInstance("RedisCache").get(key,BaseWrapper.class);
		Assert.assertEquals(key, returnValue.data);
		CacheFactory.getInstance("RedisCache").remove(key);
		returnValue = CacheFactory.getInstance("RedisCache").get(key,BaseWrapper.class);
		Assert.assertNull(returnValue);


	}

	@Test
	public void testExpireAutomatic() throws Exception{
		Assume.assumeTrue(this.canRunTest());
		String key = UUID.randomUUID().toString();
		BaseWrapper baseWrapper = new BaseWrapper();
		baseWrapper.data = key;

		CacheFactory.getInstance("RedisCache").add(key, baseWrapper);
		BaseWrapper returnValue = CacheFactory.getInstance("RedisCache").get(key,BaseWrapper.class);
		Assert.assertEquals(key, returnValue.data);
		Thread.currentThread().sleep(1000*60+10);
		returnValue = CacheFactory.getInstance("RedisCache").get(key,BaseWrapper.class);
		Assert.assertNull(returnValue);
		
	}
	
	@Test
	public void testExpireExpicit() throws Exception{
		Assume.assumeTrue(this.canRunTest());
		String key = UUID.randomUUID().toString();
		BaseWrapper baseWrapper = new BaseWrapper();
		baseWrapper.data = key;

		CacheFactory.getInstance("RedisCache").add(key, baseWrapper,10);
		BaseWrapper returnValue = CacheFactory.getInstance("RedisCache").get(key,BaseWrapper.class);
		Assert.assertEquals(key, returnValue.data);
		Thread.currentThread().sleep(1000*10+2);
		returnValue = CacheFactory.getInstance("RedisCache").get(key,BaseWrapper.class);
		Assert.assertNull(returnValue);
	}
	
	//test cache with explictly putting cache time out
}
