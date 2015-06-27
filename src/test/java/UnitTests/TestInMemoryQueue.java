package UnitTests;

import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;

import com.boilerplate.asyncWork.AsyncWorkItem;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.queue.QueueFactory;


public class TestInMemoryQueue {

	@Test
	public void test() throws Exception{
		
		String key = UUID.randomUUID().toString();
		QueueFactory.getInstance("BoilerplateNonProductionInMemoryQueue").insert("Test",key);
		String  returnValue = QueueFactory.getInstance("BoilerplateNonProductionInMemoryQueue").remove("Test");
		Assert.assertEquals(key, returnValue);
		returnValue = QueueFactory.getInstance("BoilerplateNonProductionInMemoryQueue").remove("Test");
		Assert.assertNull(returnValue);

	}
	@Test
	public void testDifferentSubjectsDontConflict() throws Exception{
		String key1 = UUID.randomUUID().toString();
		String key2 = UUID.randomUUID().toString();

		QueueFactory.getInstance("BoilerplateNonProductionInMemoryQueue").insert("Test1",key1);
		String returnValue = QueueFactory.getInstance("BoilerplateNonProductionInMemoryQueue").remove("Test2");
		Assert.assertNull(returnValue);

		QueueFactory.getInstance("BoilerplateNonProductionInMemoryQueue").insert("Test2",key2);
		
	 returnValue = QueueFactory.getInstance("BoilerplateNonProductionInMemoryQueue").remove("Test2");
	 Assert.assertEquals(key2, returnValue);

	 returnValue = QueueFactory.getInstance("BoilerplateNonProductionInMemoryQueue").remove("Test1");
	 Assert.assertEquals(key1, returnValue);
	}
}
