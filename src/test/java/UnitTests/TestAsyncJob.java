package UnitTests;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.boilerplate.asyncWork.AsyncWorkItem;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.queue.QueueFactory;

import asyncWorkObservers.DispatchObject;

@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-context.xml"})
public class TestAsyncJob {
	
	@Autowired
	com.boilerplate.jobs.QueueReaderJob queueReaderJob;
	
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;
	/**
	 * Tests a job
	 * @throws Exception 
	 */
	@Test
	public void testAsyncJobPositive() throws Exception{
		DispatchObject dispatchObject = new DispatchObject();
		dispatchObject.setNumberOne(2);
		dispatchObject.setNumberTwo(3);
		
		BoilerplateList<String> emptySubject = new BoilerplateList<String>();
		
		BoilerplateList<String> oneSubject = new BoilerplateList<String>();
		oneSubject.add("ONE");
		
		BoilerplateList<String> twoSubject = new BoilerplateList<String>();
		twoSubject.add("TWO");
		
		BoilerplateList<String> oneAndTwoSubject = new BoilerplateList<String>();
		oneAndTwoSubject.add("ONE");
		oneAndTwoSubject.add("TWO");
		
		
		queueReaderJob.requestBackroundWorkItem(dispatchObject, emptySubject, "TestAsyncJob", "testAsyncJobPositive");
		//Note this method is not actually called by any code as this will be called by BG services
		queueReaderJob.readQueueAndDispatch();
		Assert.assertEquals(5, dispatchObject.getAddResult());
		Assert.assertEquals(0, dispatchObject.getDevideResult());
		Assert.assertEquals(0, dispatchObject.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject.getPowerResult());
		Assert.assertEquals(0, dispatchObject.getSubtractResult());
		
		dispatchObject = new DispatchObject();
		dispatchObject.setNumberOne(2);
		dispatchObject.setNumberTwo(3);
		queueReaderJob.requestBackroundWorkItem(dispatchObject, oneSubject, "TestAsyncJob", "testAsyncJobPositive");
		queueReaderJob.readQueueAndDispatch();
		Assert.assertEquals(5, dispatchObject.getAddResult());
		Assert.assertEquals(0, dispatchObject.getDevideResult());
		Assert.assertEquals(0, dispatchObject.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject.getPowerResult());
		Assert.assertEquals(-1, dispatchObject.getSubtractResult());
		
		dispatchObject = new DispatchObject();
		dispatchObject.setNumberOne(6);
		dispatchObject.setNumberTwo(3);
		queueReaderJob.requestBackroundWorkItem(dispatchObject, twoSubject, "TestAsyncJob", "testAsyncJobPositive");
		queueReaderJob.readQueueAndDispatch();

		Assert.assertEquals(9, dispatchObject.getAddResult());
		Assert.assertEquals(2, dispatchObject.getDevideResult());
		Assert.assertEquals(18, dispatchObject.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject.getPowerResult());
		Assert.assertEquals(0, dispatchObject.getSubtractResult());
		
		
		dispatchObject = new DispatchObject();
		dispatchObject.setNumberOne(6);
		dispatchObject.setNumberTwo(3);
		queueReaderJob.requestBackroundWorkItem(dispatchObject, oneAndTwoSubject, "TestAsyncJob", "testAsyncJobPositive");
		queueReaderJob.readQueueAndDispatch();

		Assert.assertEquals(9, dispatchObject.getAddResult());
		Assert.assertEquals(2, dispatchObject.getDevideResult());
		Assert.assertEquals(18, dispatchObject.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject.getPowerResult());
		Assert.assertEquals(3, dispatchObject.getSubtractResult());
	}

	@Test
	public void testAsyncJobPositiveWithSubjectNone() throws Exception{
		DispatchObject dispatchObject = new DispatchObject();
		dispatchObject.setNumberOne(2);
		dispatchObject.setNumberTwo(3);
		
		BoilerplateList<String> noneSubject = new BoilerplateList<String>();
		noneSubject.add("NONE");
		
		queueReaderJob.requestBackroundWorkItem(dispatchObject, noneSubject, "TestAsyncJob", "testAsyncJobPositive");
		queueReaderJob.readQueueAndDispatch();
		Assert.assertEquals(0, dispatchObject.getAddResult());
		Assert.assertEquals(0, dispatchObject.getDevideResult());
		Assert.assertEquals(0, dispatchObject.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject.getPowerResult());
		Assert.assertEquals(0, dispatchObject.getSubtractResult());
		}
	
	@Test
	public void testAsyncJobPositiveWithSubjectNoneAndONE() throws Exception{
		DispatchObject dispatchObject = new DispatchObject();
		dispatchObject.setNumberOne(2);
		dispatchObject.setNumberTwo(3);
		
		BoilerplateList<String> noneSubject = new BoilerplateList<String>();
		noneSubject.add("NONE");
		noneSubject.add("ONE");
		
		queueReaderJob.requestBackroundWorkItem(dispatchObject, noneSubject, "TestAsyncJob", "testAsyncJobPositive");
		queueReaderJob.readQueueAndDispatch();
		Assert.assertEquals(0, dispatchObject.getAddResult());
		Assert.assertEquals(0, dispatchObject.getDevideResult());
		Assert.assertEquals(0, dispatchObject.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject.getPowerResult());
		Assert.assertEquals(0, dispatchObject.getSubtractResult());
		}

	
	/**
	 * Tests a job
	 * @throws Exception 
	 */
	@Test
	public void testAsyncJobPositiveWithThreeInputs() throws Exception{
		DispatchObject dispatchObject1 = new DispatchObject();
		dispatchObject1.setNumberOne(2);
		dispatchObject1.setNumberTwo(3);
		
		DispatchObject dispatchObject2 = new DispatchObject();
		dispatchObject2.setNumberOne(2);
		dispatchObject2.setNumberTwo(3);
		
		DispatchObject dispatchObject3 = new DispatchObject();
		dispatchObject3.setNumberOne(2);
		dispatchObject3.setNumberTwo(3);
		
		BoilerplateList<String> emptySubject = new BoilerplateList<String>();
		
		BoilerplateList<String> oneSubject = new BoilerplateList<String>();
		oneSubject.add("ONE");
		
		BoilerplateList<String> twoSubject = new BoilerplateList<String>();
		twoSubject.add("TWO");
		
		BoilerplateList<String> oneAndTwoSubject = new BoilerplateList<String>();
		oneAndTwoSubject.add("ONE");
		oneAndTwoSubject.add("TWO");
		
				
		queueReaderJob.requestBackroundWorkItem(dispatchObject1, emptySubject, "TestAsyncJob", "testAsyncJobPositive");
		queueReaderJob.requestBackroundWorkItem(dispatchObject2, emptySubject, "TestAsyncJob", "testAsyncJobPositive");
		queueReaderJob.requestBackroundWorkItem(dispatchObject3, emptySubject, "TestAsyncJob", "testAsyncJobPositive");
		queueReaderJob.readQueueAndDispatch();
		Assert.assertEquals(5, dispatchObject1.getAddResult());
		Assert.assertEquals(0, dispatchObject1.getDevideResult());
		Assert.assertEquals(0, dispatchObject1.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject1.getPowerResult());
		Assert.assertEquals(0, dispatchObject1.getSubtractResult());
		Assert.assertEquals(5, dispatchObject2.getAddResult());
		Assert.assertEquals(0, dispatchObject2.getDevideResult());
		Assert.assertEquals(0, dispatchObject2.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject2.getPowerResult());
		Assert.assertEquals(0, dispatchObject2.getSubtractResult());
		Assert.assertEquals(5, dispatchObject3.getAddResult());
		Assert.assertEquals(0, dispatchObject3.getDevideResult());
		Assert.assertEquals(0, dispatchObject3.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject3.getPowerResult());
		Assert.assertEquals(0, dispatchObject3.getSubtractResult());
		
		queueReaderJob.requestBackroundWorkItem(dispatchObject1, oneSubject, "TestAsyncJob", "testAsyncJobPositive");
		
		queueReaderJob.requestBackroundWorkItem(dispatchObject2, oneSubject, "TestAsyncJob", "testAsyncJobPositive");
		
		queueReaderJob.requestBackroundWorkItem(dispatchObject3, oneSubject, "TestAsyncJob", "testAsyncJobPositive");
				
		queueReaderJob.readQueueAndDispatch();
		Assert.assertEquals(5, dispatchObject1.getAddResult());
		Assert.assertEquals(0, dispatchObject1.getDevideResult());
		Assert.assertEquals(0, dispatchObject1.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject1.getPowerResult());
		Assert.assertEquals(-1, dispatchObject1.getSubtractResult());
		Assert.assertEquals(5, dispatchObject2.getAddResult());
		Assert.assertEquals(0, dispatchObject2.getDevideResult());
		Assert.assertEquals(0, dispatchObject2.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject2.getPowerResult());
		Assert.assertEquals(-1, dispatchObject2.getSubtractResult());
		Assert.assertEquals(5, dispatchObject3.getAddResult());
		Assert.assertEquals(0, dispatchObject3.getDevideResult());
		Assert.assertEquals(0, dispatchObject3.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject3.getPowerResult());
		Assert.assertEquals(-1, dispatchObject3.getSubtractResult());

		
		dispatchObject1 = new DispatchObject();
		dispatchObject1.setNumberOne(6);
		dispatchObject1.setNumberTwo(3);
		queueReaderJob.requestBackroundWorkItem(dispatchObject1, twoSubject, "TestAsyncJob", "testAsyncJobPositive");

		
		dispatchObject2 = new DispatchObject();
		dispatchObject2.setNumberOne(6);
		dispatchObject2.setNumberTwo(3);
		queueReaderJob.requestBackroundWorkItem(dispatchObject2, twoSubject, "TestAsyncJob", "testAsyncJobPositive");

		dispatchObject3 = new DispatchObject();
		dispatchObject3.setNumberOne(6);
		dispatchObject3.setNumberTwo(3);
		queueReaderJob.requestBackroundWorkItem(dispatchObject3, twoSubject, "TestAsyncJob", "testAsyncJobPositive");
		
		queueReaderJob.readQueueAndDispatch();

		Assert.assertEquals(9, dispatchObject1.getAddResult());
		Assert.assertEquals(2, dispatchObject1.getDevideResult());
		Assert.assertEquals(18, dispatchObject1.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject1.getPowerResult());
		Assert.assertEquals(0, dispatchObject1.getSubtractResult());
		
		Assert.assertEquals(9, dispatchObject2.getAddResult());
		Assert.assertEquals(2, dispatchObject2.getDevideResult());
		Assert.assertEquals(18, dispatchObject2.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject2.getPowerResult());
		Assert.assertEquals(0, dispatchObject2.getSubtractResult());

		Assert.assertEquals(9, dispatchObject3.getAddResult());
		Assert.assertEquals(2, dispatchObject3.getDevideResult());
		Assert.assertEquals(18, dispatchObject3.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject3.getPowerResult());
		Assert.assertEquals(0, dispatchObject3.getSubtractResult());

		dispatchObject1 = new DispatchObject();
		dispatchObject1.setNumberOne(6);
		dispatchObject1.setNumberTwo(3);
		queueReaderJob.requestBackroundWorkItem(dispatchObject1, oneAndTwoSubject, "TestAsyncJob", "testAsyncJobPositive");

		dispatchObject2 = new DispatchObject();
		dispatchObject2.setNumberOne(6);
		dispatchObject2.setNumberTwo(3);
		queueReaderJob.requestBackroundWorkItem(dispatchObject2, oneAndTwoSubject, "TestAsyncJob", "testAsyncJobPositive");

		dispatchObject3 = new DispatchObject();
		dispatchObject3.setNumberOne(6);
		dispatchObject3.setNumberTwo(3);
		queueReaderJob.requestBackroundWorkItem(dispatchObject3, oneAndTwoSubject, "TestAsyncJob", "testAsyncJobPositive");

		queueReaderJob.readQueueAndDispatch();

		Assert.assertEquals(9, dispatchObject1.getAddResult());
		Assert.assertEquals(2, dispatchObject1.getDevideResult());
		Assert.assertEquals(18, dispatchObject1.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject1.getPowerResult());
		Assert.assertEquals(3, dispatchObject1.getSubtractResult());
		
		Assert.assertEquals(9, dispatchObject2.getAddResult());
		Assert.assertEquals(2, dispatchObject2.getDevideResult());
		Assert.assertEquals(18, dispatchObject2.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject2.getPowerResult());
		Assert.assertEquals(3, dispatchObject2.getSubtractResult());
		
		Assert.assertEquals(9, dispatchObject3.getAddResult());
		Assert.assertEquals(2, dispatchObject3.getDevideResult());
		Assert.assertEquals(18, dispatchObject3.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject3.getPowerResult());
		Assert.assertEquals(3, dispatchObject3.getSubtractResult());
	}

	/**
	 * Tests a job
	 * @throws Exception 
	 */
	@Test
	public void testAsyncJobPositiveWithThreeInputsInMiddleOfProcessing() throws Exception{
		DispatchObject dispatchObject1 = new DispatchObject();
		dispatchObject1.setNumberOne(2);
		dispatchObject1.setNumberTwo(3);
		
		DispatchObject dispatchObject2 = new DispatchObject();
		dispatchObject2.setNumberOne(2);
		dispatchObject2.setNumberTwo(3);
		
		DispatchObject dispatchObject3 = new DispatchObject();
		dispatchObject3.setNumberOne(2);
		dispatchObject3.setNumberTwo(3);
		
		BoilerplateList<String> emptySubject = new BoilerplateList<String>();
		
		BoilerplateList<String> oneSubject = new BoilerplateList<String>();
		oneSubject.add("ONE");
		
		BoilerplateList<String> twoSubject = new BoilerplateList<String>();
		twoSubject.add("TWO");
		
		BoilerplateList<String> oneAndTwoSubject = new BoilerplateList<String>();
		oneAndTwoSubject.add("ONE");
		oneAndTwoSubject.add("TWO");
		


		queueReaderJob.requestBackroundWorkItem(dispatchObject1, emptySubject, "TestAsyncJob", "testAsyncJobPositive");
		queueReaderJob.requestBackroundWorkItem(dispatchObject2, emptySubject, "TestAsyncJob", "testAsyncJobPositive");
		queueReaderJob.readQueueAndDispatch();

		queueReaderJob.requestBackroundWorkItem(dispatchObject3, emptySubject, "TestAsyncJob", "testAsyncJobPositive");
		queueReaderJob.readQueueAndDispatch();
		Assert.assertEquals(5, dispatchObject1.getAddResult());
		Assert.assertEquals(0, dispatchObject1.getDevideResult());
		Assert.assertEquals(0, dispatchObject1.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject1.getPowerResult());
		Assert.assertEquals(0, dispatchObject1.getSubtractResult());
		Assert.assertEquals(5, dispatchObject2.getAddResult());
		Assert.assertEquals(0, dispatchObject2.getDevideResult());
		Assert.assertEquals(0, dispatchObject2.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject2.getPowerResult());
		Assert.assertEquals(0, dispatchObject2.getSubtractResult());
		Assert.assertEquals(5, dispatchObject3.getAddResult());
		Assert.assertEquals(0, dispatchObject3.getDevideResult());
		Assert.assertEquals(0, dispatchObject3.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject3.getPowerResult());
		Assert.assertEquals(0, dispatchObject3.getSubtractResult());
		
		dispatchObject1 = new DispatchObject();
		dispatchObject1.setNumberOne(2);
		dispatchObject1.setNumberTwo(3);
		
		dispatchObject2 = new DispatchObject();
		dispatchObject2.setNumberOne(2);
		dispatchObject2.setNumberTwo(3);
		dispatchObject3 = new DispatchObject();
		dispatchObject3.setNumberOne(2);
		dispatchObject3.setNumberTwo(3);
		queueReaderJob.requestBackroundWorkItem(dispatchObject1, oneSubject, "TestAsyncJob", "testAsyncJobPositive");
		queueReaderJob.requestBackroundWorkItem(dispatchObject2, oneSubject, "TestAsyncJob", "testAsyncJobPositive");
		queueReaderJob.requestBackroundWorkItem(dispatchObject3, oneSubject, "TestAsyncJob", "testAsyncJobPositive");
		
		queueReaderJob.readQueueAndDispatch();
		Assert.assertEquals(5, dispatchObject1.getAddResult());
		Assert.assertEquals(0, dispatchObject1.getDevideResult());
		Assert.assertEquals(0, dispatchObject1.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject1.getPowerResult());
		Assert.assertEquals(-1, dispatchObject1.getSubtractResult());
		Assert.assertEquals(5, dispatchObject2.getAddResult());
		Assert.assertEquals(0, dispatchObject2.getDevideResult());
		Assert.assertEquals(0, dispatchObject2.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject2.getPowerResult());
		Assert.assertEquals(-1, dispatchObject2.getSubtractResult());
		Assert.assertEquals(5, dispatchObject3.getAddResult());
		Assert.assertEquals(0, dispatchObject3.getDevideResult());
		Assert.assertEquals(0, dispatchObject3.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject3.getPowerResult());
		Assert.assertEquals(-1, dispatchObject3.getSubtractResult());

		
		dispatchObject1 = new DispatchObject();
		dispatchObject1.setNumberOne(6);
		dispatchObject1.setNumberTwo(3);
		

		dispatchObject2 = new DispatchObject();
		dispatchObject2.setNumberOne(6);
		dispatchObject2.setNumberTwo(3);

		

		dispatchObject3 = new DispatchObject();
		dispatchObject3.setNumberOne(6);
		dispatchObject3.setNumberTwo(3);
		
		queueReaderJob.readQueueAndDispatch();

		queueReaderJob.requestBackroundWorkItem(dispatchObject1, twoSubject, "TestAsyncJob", "testAsyncJobPositive");

		queueReaderJob.requestBackroundWorkItem(dispatchObject2, twoSubject, "TestAsyncJob", "testAsyncJobPositive");

		queueReaderJob.requestBackroundWorkItem(dispatchObject3, twoSubject, "TestAsyncJob", "testAsyncJobPositive");

		queueReaderJob.readQueueAndDispatch();
		Assert.assertEquals(9, dispatchObject1.getAddResult());
		Assert.assertEquals(2, dispatchObject1.getDevideResult());
		Assert.assertEquals(18, dispatchObject1.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject1.getPowerResult());
		Assert.assertEquals(0, dispatchObject1.getSubtractResult());
		
		Assert.assertEquals(9, dispatchObject2.getAddResult());
		Assert.assertEquals(2, dispatchObject2.getDevideResult());
		Assert.assertEquals(18, dispatchObject2.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject2.getPowerResult());
		Assert.assertEquals(0, dispatchObject2.getSubtractResult());

		Assert.assertEquals(9, dispatchObject3.getAddResult());
		Assert.assertEquals(2, dispatchObject3.getDevideResult());
		Assert.assertEquals(18, dispatchObject3.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject3.getPowerResult());
		Assert.assertEquals(0, dispatchObject3.getSubtractResult());

		dispatchObject1 = new DispatchObject();
		dispatchObject1.setNumberOne(6);
		dispatchObject1.setNumberTwo(3);		
		

		dispatchObject2 = new DispatchObject();
		dispatchObject2.setNumberOne(6);
		dispatchObject2.setNumberTwo(3);

		dispatchObject3 = new DispatchObject();
		dispatchObject3.setNumberOne(6);
		dispatchObject3.setNumberTwo(3);


		queueReaderJob.readQueueAndDispatch();
		Assert.assertEquals(0, dispatchObject1.getAddResult());
		queueReaderJob.requestBackroundWorkItem(dispatchObject1, oneAndTwoSubject, "TestAsyncJob", "testAsyncJobPositive");

		queueReaderJob.requestBackroundWorkItem(dispatchObject2, oneAndTwoSubject, "TestAsyncJob", "testAsyncJobPositive");

		queueReaderJob.requestBackroundWorkItem(dispatchObject3, oneAndTwoSubject, "TestAsyncJob", "testAsyncJobPositive");

		queueReaderJob.readQueueAndDispatch();
		
		Assert.assertEquals(9, dispatchObject1.getAddResult());
		Assert.assertEquals(2, dispatchObject1.getDevideResult());
		Assert.assertEquals(18, dispatchObject1.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject1.getPowerResult());
		Assert.assertEquals(3, dispatchObject1.getSubtractResult());
		
		Assert.assertEquals(9, dispatchObject2.getAddResult());
		Assert.assertEquals(2, dispatchObject2.getDevideResult());
		Assert.assertEquals(18, dispatchObject2.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject2.getPowerResult());
		Assert.assertEquals(3, dispatchObject2.getSubtractResult());
		
		Assert.assertEquals(9, dispatchObject3.getAddResult());
		Assert.assertEquals(2, dispatchObject3.getDevideResult());
		Assert.assertEquals(18, dispatchObject3.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject3.getPowerResult());
		Assert.assertEquals(3, dispatchObject3.getSubtractResult());
	}
	@Test
	public void testTurnOffAndOnJobs() throws Exception{
		DispatchObject dispatchObject = new DispatchObject();
		BoilerplateList<String> emptySubject = new BoilerplateList<String>();
				
		BoilerplateList<String> oneAndTwoSubject = new BoilerplateList<String>();
		oneAndTwoSubject.add("ONE");
		oneAndTwoSubject.add("TWO");
		
		
		dispatchObject.setNumberOne(6);
		dispatchObject.setNumberTwo(3);
		queueReaderJob.requestBackroundWorkItem(dispatchObject, oneAndTwoSubject, "TestAsyncJob", "testAsyncJobPositive");

		queueReaderJob.setBackgroundServiceOff();
		queueReaderJob.readQueueAndDispatch();

		Assert.assertEquals(0, dispatchObject.getAddResult());
		Assert.assertEquals(0, dispatchObject.getDevideResult());
		Assert.assertEquals(0, dispatchObject.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject.getPowerResult());
		Assert.assertEquals(0, dispatchObject.getSubtractResult());

		queueReaderJob.setBackgroundServiceOn();
		queueReaderJob.readQueueAndDispatch();
		
		Assert.assertEquals(9, dispatchObject.getAddResult());
		Assert.assertEquals(2, dispatchObject.getDevideResult());
		Assert.assertEquals(18, dispatchObject.getMultiplyResult());
		Assert.assertEquals(0, dispatchObject.getPowerResult());
		Assert.assertEquals(3, dispatchObject.getSubtractResult());
	}

}
