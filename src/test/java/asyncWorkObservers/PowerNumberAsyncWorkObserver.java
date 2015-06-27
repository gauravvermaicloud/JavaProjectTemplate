package asyncWorkObservers;

import com.boilerplate.asyncWork.AsyncWorkItem;
import com.boilerplate.asyncWork.IAsyncWorkObserver;

public class PowerNumberAsyncWorkObserver implements IAsyncWorkObserver{
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) {
		DispatchObject dispatchObject = (DispatchObject)asyncWorkItem.getPayload();
		dispatchObject.setPowerResult(dispatchObject.getNumberOne()^ dispatchObject.getNumberTwo());
	}
}
