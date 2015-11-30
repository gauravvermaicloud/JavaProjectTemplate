package UnitTests;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import UnitTests.TestInMemoryCache.BaseWrapper;

import com.boilerplate.cache.CacheFactory;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateMap;

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
				CacheFactory.getInstance("RedisCache",configurations).add(key, baseWrapper,10);
				BaseWrapper returnValue = CacheFactory.getInstance("RedisCache",configurations).get(key,BaseWrapper.class);
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
		
		CacheFactory.getInstance("RedisCache",configurations).add(key, baseWrapper);
		BaseWrapper returnValue = CacheFactory.getInstance("RedisCache",configurations).get(key,BaseWrapper.class);
		Assert.assertEquals(key, returnValue.data);
		CacheFactory.getInstance("RedisCache",configurations).remove(key);
		returnValue = CacheFactory.getInstance("RedisCache",configurations).get(key,BaseWrapper.class);
		Assert.assertNull(returnValue);


	}

	//@Test
	//Removed this test case as it will take 20 min to eork
	public void testExpireAutomatic() throws Exception{
		Assume.assumeTrue(this.canRunTest());
		String key = UUID.randomUUID().toString();
		BaseWrapper baseWrapper = new BaseWrapper();
		baseWrapper.data = key;

		CacheFactory.getInstance("RedisCache",configurations).add(key, baseWrapper);
		BaseWrapper returnValue = CacheFactory.getInstance("RedisCache",configurations).get(key,BaseWrapper.class);
		Assert.assertEquals(key, returnValue.data);
		Thread.currentThread().sleep(1000*60+10);
		returnValue = CacheFactory.getInstance("RedisCache",configurations).get(key,BaseWrapper.class);
		Assert.assertNull(returnValue);
		
	}
	
	@Test
	public void testExpireExpicit() throws Exception{
		Assume.assumeTrue(this.canRunTest());
		String key = UUID.randomUUID().toString();
		BaseWrapper baseWrapper = new BaseWrapper();
		baseWrapper.data = key;

		CacheFactory.getInstance("RedisCache",configurations).add(key, baseWrapper,10);
		BaseWrapper returnValue = CacheFactory.getInstance("RedisCache",configurations).get(key,BaseWrapper.class);
		Assert.assertEquals(key, returnValue.data);
		Thread.currentThread().sleep(1000*10+2);
		returnValue = CacheFactory.getInstance("RedisCache",configurations).get(key,BaseWrapper.class);
		Assert.assertNull(returnValue);
	}
	
	//TODO test cache with explictly putting cache time out
	@Before
    public void setUp() {
		loadPropertyFile();
	}
	
	private static BoilerplateMap<String, String> configurations = new BoilerplateMap<String, String>();
	/**
	 * This method loads configuration from the property file
	 */
	private static void loadPropertyFile(){
		Properties properties  = null;
		InputStream inputStream =null;
		try{
			properties = new Properties();
			//Using the .properties file in the class  path load the file
			//into the properties class
			inputStream = 
					CacheFactory.class.getClassLoader().getResourceAsStream("boilerplate.properties");
			properties.load(inputStream);
			//for each key that exists in the properties file put it into
			//the configuration map
			for(String key : properties.stringPropertyNames()){
				TestRedisCache.configurations.put(key, properties.getProperty(key));
			}
		}
		catch(IOException ioException){
			//we do not generally expect an exception here
			//and because we are start of the code even before loggers
			//have been enabled if something goes wrong we will have to print it to
			//console. We do not throw this exception because we do not expect it
			//and if we do throw it then it would have to be handeled in all code 
			//making it bloated, it is hence a safe assumption this exception ideally will not
			//happen unless the file access has  issues
			System.out.println(ioException.toString());
		}
		finally{
			//close the input stream if it is not null
			if(inputStream !=null){
				try{
					inputStream.close();
				}
				catch(Exception ex){
					//if there is an issue closing it we just print it
					//and move forward as there is not a good way to inform user.
					System.out.println(ex.toString());
				}
			}
		}//end finally
	}
}
