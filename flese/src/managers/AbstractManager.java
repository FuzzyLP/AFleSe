package managers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import storeHouse.RequestStoreHouse;
import storeHouse.ResultsStoreHouse;
import urls.UrlMap;
import urls.UrlMapException;
import urls.UrlsMaps;
import auxiliar.NextStep;
import auxiliar.RegistryEntry;
import constants.KConstants;
import constants.KUrls;

public abstract class AbstractManager implements InterfaceManager {

	private static final Log LogAbstractManager = LogFactory.getLog(AbstractManager.class);

	protected RequestStoreHouse requestStoreHouse = null;
	protected ResultsStoreHouse resultsStoreHouse = null;

	private NextStep nextStep;
	private Method method;
	private String op;

	public void actionWhenExceptionInTargetMethodInvocation(String methodName) {
		NextStep nextStep = new NextStep(KConstants.NextStep.forward_to, KUrls.Pages.Exception, "");
		setNextStep(nextStep);
	}

	public boolean createSessionIfNull() {
		return false;
	}

	public boolean exceptionIfSessionIsNull() {
		return true;
	}

	public boolean exceptionIfLocalUserInfoIsNull() {
		return true;
	}

	public boolean reinitializeResultsStoreHouse() {
		return true;
	}

	public NextStep getNextStep() {
		return nextStep;
	}

	public void setNextStep(NextStep nextStep) {
		this.nextStep = nextStep;
	}

	public void setRequestStoreHouse(RequestStoreHouse requestStoreHouse) {
		this.requestStoreHouse = requestStoreHouse;
	}
	
	public RequestStoreHouse getRequestStoreHouse() {
		return requestStoreHouse;
	}
	
	public String getOp()
	{
		return op;
	}

	protected String getRequestManager() {
		return requestStoreHouse.getRequestParameter(KConstants.Request.managerParam);
	}

	protected String getRequestOp() {
		return requestStoreHouse.getRequestParameter(KConstants.Request.operationParam);
	}

	public String getCalledMethodName() {
		if (this.method != null) {
			return this.method.getName();
		}
		return "unknown";
	}

	public NextStep processRequest() {

		op = null;
		method = null;
		setNextStep(null);
		UrlMap urlMap = getValidUrlMap();

		if (urlMap != null) {
			op = urlMap.getOp(false);
			if ((op != null) && (!"".equals(op))) {
				getMethod();
			}
		}
		// This allows a failsafe when the method is not found or there is no
		// method.
		if ((urlMap == null) || (method == null) || (op == null) || ("".equals(op))) {
			LogAbstractManager.info("Impossible to reach url method. Getting default method.");
			op = methodToInvokeIfMethodRequestedIsNotAvailable();
			getMethod();
		}

		// Get the results storage facility.
		this.resultsStoreHouse = this.requestStoreHouse.getResultsStoreHouse();

		// Use the registry storage facility to register input.
		String className = this.getClass().getName();
		RegistryEntry registryEntry = new RegistryEntry(className, op, "", this.requestStoreHouse);
		if (this.requestStoreHouse.getSession() != null) {
			this.requestStoreHouse.getSession().addToRegistryStoreHouse(registryEntry);
		}

		// Invoke the method.
		invokeMethod();

		// Use the registry storage facility to register output. Include
		// exception msg if any.
		registryEntry = new RegistryEntry(registryEntry, nextStep, requestStoreHouse.isAjax());
		registryEntry.setMsg(this.resultsStoreHouse.getExceptionMsg());
		if (this.requestStoreHouse.getSession() != null) {
			this.requestStoreHouse.getSession().addToRegistryStoreHouse(registryEntry);
		}

		// Save results in the request, to access them from jsps.
		this.requestStoreHouse.storeResultsStoreHouse();

		return nextStep;
	}

	private UrlMap getValidUrlMap() {
		UrlMap urlMap = UrlMap.getUrlMap(requestStoreHouse);
		try {
			urlMap = UrlsMaps.getUrlMap(urlMap);
		} catch (UrlMapException e1) {
			e1.printStackTrace();
			urlMap = null;
		}
		return urlMap;
	}

	private void getMethod() {
		try {
			method = this.getClass().getMethod(op, new Class[] {});
		} catch (NoSuchMethodException e) {
			method = null;
			LogAbstractManager.error("ERROR looking for method " + op + " in class " + this.getClass().getName());
			setNextStep(null);
			e.printStackTrace();
		} catch (SecurityException e) {
			method = null;
			LogAbstractManager.error("ERROR looking for method " + op + " in class " + this.getClass().getName());
			setNextStep(null);
			e.printStackTrace();
		}
	}

	private void invokeMethod() {
		if (method != null) {
			try {	
				LogAbstractManager.info(getInfoMsg("Calling"));
				method.invoke((Object) this, new Object[0]);
					
			} catch (IllegalAccessException e) {
				LogAbstractManager.error(getInfoMsg("ERROR invoking"));
				e.printStackTrace();
				setNextStep(null);
			} catch (IllegalArgumentException e) {
				LogAbstractManager.error(getInfoMsg("ERROR invoking"));
				e.printStackTrace();
				setNextStep(null);
			} catch (InvocationTargetException e) {
				actionWhenExceptionInTargetMethodInvocationEnvelope(e);
				// } catch (Throwable e) {
			} catch (RuntimeException e) {
				actionWhenExceptionInTargetMethodInvocationEnvelope(e);
			} 
		}
	}

	private String getInfoMsg(String prefix) {
		StringBuilder msg = new StringBuilder();
		msg.append(prefix == null ? "Playing with" : prefix);
		msg.append(" method ");
		msg.append(getCalledMethodName());
		msg.append(" in class ");
		if ((this.getClass() != null) && (this.getClass().getName() != null)) {
			msg.append(this.getClass().getName());
		} else {
			msg.append(" unknown ");
		}
		return msg.toString();
	}

	private void actionWhenExceptionInTargetMethodInvocationEnvelope(Exception e) {
		LogAbstractManager.error(getInfoMsg("Exception executing"));
		if ((e.getMessage() != null) && (!"".equals(e.getMessage()))) {
			LogAbstractManager.error(e.getMessage());
		}
		LogAbstractManager.error("------------------------------");
		e.printStackTrace();
		LogAbstractManager.error("------------------------------");

		saveExceptionInformation(e);
		actionWhenExceptionInTargetMethodInvocation(getCalledMethodName());
	}

	private void saveExceptionInformation(Exception e) {
		StringBuilder msg = new StringBuilder();
		msg.append(getInfoMsg("Exception executing "));

		if ((e != null) && (e.getMessage() != null) && (!"".equals(e.getMessage()))) {
			msg.append(": ");
			msg.append(e.getMessage());
		}
		resultsStoreHouse.setExceptionMsg(msg.toString());
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

}

// EOF
