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
//test the custom attributes on thread local
}
