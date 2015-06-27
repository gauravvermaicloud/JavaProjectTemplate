package UnitTests;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import UnitTests.TestLogger.InnerClass.ReallyInnerClass;

import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;

/**
 * Tests the logger
 * @author gaurav
 *
 */

@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-context.xml"})
public class TestLogger {
	
	/**
	 * This is the inner class
	 * @author gaurav
	 *
	 */
	public class InnerClass{
		/**
		 * The instance of class inside inner class
		 */
		public ReallyInnerClass reallyInnerClass = null;
		/**
		 * Default constructor
		 */
		public InnerClass(){
			reallyInnerClass = new ReallyInnerClass();
		}
		/**
		 * Another level of depth in inner class
		 */
		public class ReallyInnerClass{
			/**
			 * Always throws an exception.
			 * @throws Exception
			 */
			public void throwNewException() throws Exception{
				throw new Exception();
			}
		}
	}
	
	/**
	 * This method generates an exception
	 * @return an exception with some degree of depth
	 */
	public Exception generateException(){
		InnerClass innerClass = new InnerClass();
		Exception ex =null;
		try{
			thrower(innerClass);
		}
		catch(Exception throwdException){
			ex  = throwdException;
		}
		return ex;
	}
	
	/**
	 * Helps throw an exception
	 * @param innerClass The inner class which will help throw an exception
	 * @throws Exception This is always thrown as it has been coded
	 */
	public void thrower(InnerClass innerClass) throws Exception{
		innerClass.reallyInnerClass.throwNewException();
	}
	
	/**
	 * This is the logger
	 */
	Logger logger = Logger.getInstance(TestLogger.class);
	
	/**
	 * This method tests the loggers
	 */
	@Test
	public void test() {
		RequestThreadLocal.setRequest(UUID.randomUUID().toString(),null);
		logger.isDebugEnabled();
		logger.logDebug("TestLogger", "test", "test", "Test Message");
		logger.logError("TestLogger", "test", "test", "Test Message");
		Exception ex = generateException();
		logger.logException("TestLogger", "test", "test", "Test Message", ex);
		logger.logFatel("TestLogger", "test", "test", "Test Message");
		logger.logFatel("TestLogger", "test", "test", "Test Message", ex);
		logger.logInfo("TestLogger", "test", "test", "Test Message");
		logger.logWarning("TestLogger", "test", "test", "Test Message");
		Object[] args = new Object [] {"1","2","3"};
		logger.logTraceExit("TestLogger", "test",args,77);
		logger.logTraceExit("TestLogger", "test",null,77);
		logger.logTraceExit("TestLogger", "test",null,null);
		logger.logTraceExit("TestLogger", "test",args,null);

		logger.logTraceExitException("TestLogger", "test",args,ex);
		logger.logTraceExitException("TestLogger", "test",null,ex);
	}

}
