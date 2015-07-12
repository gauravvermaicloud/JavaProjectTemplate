package UnitTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.boilerplate.framework.web.HttpRequestIdInterceptor;
import com.boilerplate.java.Constants;
import com.boilerplate.java.controllers.HealthController;
import com.boilerplate.java.entities.Ping;

@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-context.xml"})
public class TestHealthController {


	@Autowired HttpRequestIdInterceptor httpRequestInterseptor;
	@Autowired
	HealthController healthController;
	
	@Test
	public void testPing() throws Exception{
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		httpRequestInterseptor.preHandle(request, response, new Object());
		Ping ping = healthController.ping();
		//not checking individal values as they depend upon deployment
	}
}
