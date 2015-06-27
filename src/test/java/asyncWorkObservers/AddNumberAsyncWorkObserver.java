package asyncWorkObservers;

import com.boilerplate.asyncWork.AsyncWorkItem;
import com.boilerplate.asyncWork.IAsyncWorkObserver;

public class AddNumberAsyncWorkObserver implements IAsyncWorkObserver{

	@Override
	public void observe(AsyncWorkItem asyncWorkItem) {
		DispatchObject dispatchObject = (DispatchObject)asyncWorkItem.getPayload();
		dispatchObject.setAddResult(dispatchObject.getNumberOne()+dispatchObject.getNumberTwo());
	}

}
