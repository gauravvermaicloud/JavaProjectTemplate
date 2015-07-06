package UnitTests;

import java.io.Serializable;
import java.util.UUID;

import junit.framework.Assert;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedClientException;

import org.junit.Test;
import org.junit.Assume;

import UnitTests.TestInMemoryCache.BaseWrapper;

import com.boilerplate.cache.CacheFactory;
import com.boilerplate.java.Base;

public class TestMemCached {

	public class BaseWrapper extends Base implements Serializable{
		public String data = null;
		
	}

	public boolean canRunTest(){
		//if the MemCache is not even working then there is no point in running this test
		//There can be two cases -either we are not using memcacheq in which case if this test fails it is okey
		//Or we are using memcache mq but it is not properly deployed in which case the test deployed cache will fail
		//and we will learn.
			try{
				String key = UUID.randomUUID().toString();
				BaseWrapper baseWrapper = new BaseWrapper();
				baseWrapper.data = key;	
				CacheFactory.getInstance("MemCache").add(key, baseWrapper,10);
				BaseWrapper returnValue = CacheFactory.getInstance("MemCache").get(key,BaseWrapper.class);
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
		CacheFactory.getInstance("MemCache").add(key, baseWrapper);
		BaseWrapper returnValue = CacheFactory.getInstance("MemCache").get(key,BaseWrapper.class);
		Assert.assertEquals(key, returnValue.data);
		CacheFactory.getInstance("MemCache").remove(key);
		returnValue = CacheFactory.getInstance("MemCache").get(key,BaseWrapper.class);
		Assert.assertNull(returnValue);
	}

	@Test
	public void testExpireAutomatic() throws Exception{
		
		Assume.assumeTrue(this.canRunTest());
		String key = UUID.randomUUID().toString();
		BaseWrapper baseWrapper = new BaseWrapper();
		baseWrapper.data = key;

		CacheFactory.getInstance("MemCache").add(key, baseWrapper);
		BaseWrapper returnValue = CacheFactory.getInstance("MemCache").get(key,BaseWrapper.class);
		Assert.assertEquals(key, returnValue.data);
		Thread.currentThread().sleep(1000*60+10);
		returnValue = CacheFactory.getInstance("MemCache").get(key,BaseWrapper.class);
		Assert.assertNull(returnValue);
	}
	
	@Test
	public void testExpireExpicit() throws Exception{
		
		Assume.assumeTrue(this.canRunTest());
		String key = UUID.randomUUID().toString();
		BaseWrapper baseWrapper = new BaseWrapper();
		baseWrapper.data = key;

		CacheFactory.getInstance("MemCache").add(key, baseWrapper,10);
		BaseWrapper returnValue = CacheFactory.getInstance("MemCache").get(key,BaseWrapper.class);
		Assert.assertEquals(key, returnValue.data);
		Thread.currentThread().sleep(1000*60+10);
		returnValue = CacheFactory.getInstance("MemCache").get(key,BaseWrapper.class);
		Assert.assertNull(returnValue);
	}
	
	//TODO test cache with explictly putting cache time out
}
