package UnitTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.framework.web.HttpRequestIdInterceptor;

public class TestHttpRequestInterceptor {

	/**
	 * This method tests Http interseptor to create a per request id
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception{
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
       
        HttpRequestIdInterceptor httpRequestIdInterceptor = new
        		HttpRequestIdInterceptor();
        httpRequestIdInterceptor.preHandle(request, response, new Object());
        
       String requestId = (String) response.getHeader("X-Http-Request-Id");
       Assert.assertEquals(requestId, RequestThreadLocal.getRequestId());
       httpRequestIdInterceptor.afterCompletion(request, response, new Object(), new Exception());
       Assert.assertNull(RequestThreadLocal.getRequestId());
	}

}
