package managers;

import storeHouse.RequestStoreHouse;
import auxiliar.NextStep;

public interface InterfaceManager {

	public NextStep processRequest();

	public NextStep getNextStep();

	public void setNextStep(NextStep nextStep);

	public String methodToInvokeIfMethodRequestedIsNotAvailable();

	public boolean createSessionIfNull();
	
	public boolean exceptionIfSessionIsNull();

	public boolean exceptionIfLocalUserInfoIsNull();

	public void actionWhenExceptionInTargetMethodInvocation(String methodName);

	public void setRequestStoreHouse(RequestStoreHouse requestStoreHouse);
	
	public String getCalledMethodName();
}
