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

import com.boilerplate.asyncWork.AsyncWorkItem;
import com.boilerplate.cache.CacheFactory;
import com.boilerplate.cache.ICache;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.queue.QueueFactory;

@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-context.xml"})
public class TestDeployedQueue {
	@Test
	public void test() throws Exception{

		
		String key = UUID.randomUUID().toString();
		QueueFactory.getInstance().insert("Test",key);
		String  returnValue = QueueFactory.getInstance().remove("Test");
		Assert.assertEquals(key, returnValue);
		returnValue = QueueFactory.getInstance().remove("Test");
		Assert.assertNull(returnValue);
	}

	@Test
	public void testDifferentSubjectsDontConflict() throws Exception{
		String key1 = UUID.randomUUID().toString();
		String key2 = UUID.randomUUID().toString();

		QueueFactory.getInstance().insert("Test1",key1);
		String returnValue = QueueFactory.getInstance().remove("Test2");
		Assert.assertNull(returnValue);

		QueueFactory.getInstance().insert("Test2",key2);
		
	 returnValue = QueueFactory.getInstance().remove("Test2");
	 Assert.assertEquals(key2, returnValue);

	 returnValue = QueueFactory.getInstance().remove("Test1");
	 Assert.assertEquals(key1, returnValue);
	}
}
