package UnitTests;

import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.Assume;

import com.boilerplate.asyncWork.AsyncWorkItem;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.queue.QueueFactory;


public class TestInRabbitMQ {

	public boolean canRunTest(){
		//if the RabbitMQ is not even working then there is no point in running this test
		//There can be two cases -either we are not using rabit mq in which case if this test fails it is okey
		//Or we are using rabit mq but it is not properly deployed in which case the test deployed queue will fail
		//and we will learn.
			try{
				QueueFactory.getInstance("RabbitMQ").insert("Test",UUID.randomUUID().toString());
				return true;
			}
			catch(Exception ex){
				return false;
			}	
	}
	
	@Test
	public void test() throws Exception{
		Assume.assumeTrue(this.canRunTest());
		String key = UUID.randomUUID().toString();
		QueueFactory.getInstance("RabbitMQ").insert("Test",key);
		String  returnValue = QueueFactory.getInstance("RabbitMQ").remove("Test");
		Assert.assertEquals(key, returnValue);
		returnValue = QueueFactory.getInstance("RabbitMQ").remove("Test");
		Assert.assertNull(returnValue);

	}
	
	@Test
	public void testDifferentSubjectsDontConflict() throws Exception{
		
		Assume.assumeTrue(this.canRunTest());
		String key1 = UUID.randomUUID().toString();
		String key2 = UUID.randomUUID().toString();

		QueueFactory.getInstance("RabbitMQ").insert("Test1",key1);
		String returnValue = QueueFactory.getInstance("RabbitMQ").remove("Test2");
		Assert.assertNull(returnValue);

		QueueFactory.getInstance("RabbitMQ").insert("Test2",key2);
		
	 returnValue = QueueFactory.getInstance("RabbitMQ").remove("Test2");
	 Assert.assertEquals(key2, returnValue);

	 returnValue = QueueFactory.getInstance("RabbitMQ").remove("Test1");
	 Assert.assertEquals(key1, returnValue);
	}
	
	@Test
	public void testTimeOutOfQueue() throws Exception{
		
		Assume.assumeTrue(this.canRunTest());
		String returnValue = QueueFactory.getInstance("RabbitMQ").remove(UUID.randomUUID().toString());
		Assert.assertNull(returnValue);
	}
}
