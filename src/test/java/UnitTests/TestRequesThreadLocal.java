package UnitTests;

import static org.junit.Assert.*;

import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;

import com.boilerplate.framework.RequestThreadLocal;

public class TestRequesThreadLocal {

	/**
	 * This class checks the request id
	 */
	@Test
	public void test() {
		String randomId = UUID.randomUUID().toString();
		RequestThreadLocal.setRequest(randomId,null,null,null);
		Assert.assertEquals(randomId, RequestThreadLocal.getRequestId());
		RequestThreadLocal.remove();
		Assert.assertNull(RequestThreadLocal.getRequestId());
	}
//TODO test the custom attributes on thread local
	@Test
	public void testCustomAttributesOnThread(){
		String randomId = UUID.randomUUID().toString();
		RequestThreadLocal.setRequest(randomId,null,null,null);
		Assert.assertEquals(randomId, RequestThreadLocal.getRequestId());
		RequestThreadLocal.setAttribute(randomId, randomId);
		String id =(String) RequestThreadLocal.getAttribute(randomId);
		Assert.assertEquals(id, randomId);
		RequestThreadLocal.remove();
		Assert.assertNull(RequestThreadLocal.getRequestId());
	}
}
