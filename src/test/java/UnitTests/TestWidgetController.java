package UnitTests;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.boilerplate.exceptions.BaseBoilerplateException;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.framework.web.HttpRequestIdInterceptor;
import com.boilerplate.java.controllers.WidgetController;
import com.boilerplate.java.entities.BaseEntity;
import com.boilerplate.java.entities.Widget;
import com.boilerplate.service.interfaces.IWidgetService;


@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-context.xml"})
public class TestWidgetController {
	@Autowired
	WidgetController widgetController;
	
	@Test
	public void testGetByIdPositiveTestCase() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response=new MockHttpServletResponse();
		HttpRequestIdInterceptor requestIdInterseptor = new HttpRequestIdInterceptor();
		requestIdInterseptor.preHandle(request,response, null);
		
		BaseEntity widget = widgetController.getById("77");
		assertEquals(widget.getId(),"77");
		
		requestIdInterseptor.afterCompletion(request, response, null, null);
		
	}

}
