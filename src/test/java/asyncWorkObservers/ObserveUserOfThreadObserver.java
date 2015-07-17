package asyncWorkObservers;

import com.boilerplate.asyncWork.AsyncWorkItem;
import com.boilerplate.asyncWork.IAsyncWorkObserver;
import com.boilerplate.framework.RequestThreadLocal;

public class ObserveUserOfThreadObserver implements IAsyncWorkObserver
{

	@Override
	public void observe(AsyncWorkItem asyncWorkItem) {
		// TODO Auto-generated method stub
		DispatchObject dispatchObject = (DispatchObject)asyncWorkItem.getPayload();
		dispatchObject.setUserName(RequestThreadLocal.getSession().getExternalFacingUser().getUserId());
		
	}

}
