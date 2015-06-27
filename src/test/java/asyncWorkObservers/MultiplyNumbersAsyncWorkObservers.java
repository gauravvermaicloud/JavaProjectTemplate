package asyncWorkObservers;

import com.boilerplate.asyncWork.AsyncWorkItem;
import com.boilerplate.asyncWork.IAsyncWorkObserver;

public class MultiplyNumbersAsyncWorkObservers  implements IAsyncWorkObserver{
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) {
		DispatchObject dispatchObject = (DispatchObject)asyncWorkItem.getPayload();
		dispatchObject.setMultiplyResult(dispatchObject.getNumberOne()* dispatchObject.getNumberTwo());
	}
}
