package storeHouse;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import auxiliar.FleSeException;
import constants.KConstants;
import filesAndPaths.FilesAndPathsException;
import filesAndPaths.ProgramFileInfo;

public class RequestStoreHouse {

	final Log LOG = LogFactory.getLog(RequestStoreHouse.class);
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	private ServletContext servletContext = null;
	private String doMethod = null;
	private SessionStoreHouse session = null;
	private ResultsStoreHouse resultsStoreHouse = null;

	private HashMap<String, String[]> requestParams = null;

	private RequestStoreHouse(HttpServletRequest request, boolean create, boolean exceptionIfSessionIsNull,
			boolean exceptionIfLocalUserInfoIsNull, boolean getRequestParams, boolean restoreRequestParams) throws FleSeException {

		if (request == null)
			throw new RequestStoreHouseException("request is null");

		this.request = request;
		session = new SessionStoreHouse(request, create, exceptionIfSessionIsNull, exceptionIfLocalUserInfoIsNull);
		if ((session == null) && (exceptionIfSessionIsNull)) {
			throw new RequestStoreHouseException("session is null");
		}
		getResultsStoreHouse();

		if (getRequestParams) {
			copyRequestParameters();
		}
		if (restoreRequestParams) {
			restoreRequestParameters();
		}
		String requestParametersToLog = getRequestParametersString();
		LOG.info("requestParameters: " + requestParametersToLog);
	}

	public static RequestStoreHouse getRequestStoreHouse(HttpServletRequest request) {
		RequestStoreHouse requestStoreHouse = null;
		try {
			requestStoreHouse = new RequestStoreHouse(request, false, false, false, false, true);
		} catch (FleSeException e) {
			e.printStackTrace();
			requestStoreHouse = null;
		}
		return requestStoreHouse;
	}

	public static RequestStoreHouse getRequestStoreHouse(HttpServletRequest request, boolean create, boolean exceptionIfSessionIsNull,
			boolean exceptionIfLocalUserInfoIsNull) throws FleSeException {
		return new RequestStoreHouse(request, create, exceptionIfSessionIsNull, exceptionIfLocalUserInfoIsNull, true, false);
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setResponse(HttpServletResponse response) throws RequestStoreHouseException {
		if (response == null)
			throw new RequestStoreHouseException("response is null");
		this.response = response;
	}

	public HttpServletResponse getResponse() {
		return this.response;
	}

	public SessionStoreHouse getSession() {
		return this.session;
	}

	public void setServletContext(ServletContext servletContext) throws RequestStoreHouseException {
		if (servletContext == null)
			throw new RequestStoreHouseException("servletContext is null");

		this.servletContext = servletContext;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setDoMethod(String doMethod) throws RequestStoreHouseException {
		if (doMethod == null)
			throw new RequestStoreHouseException("doMethod is null");
		if ((!"doGet".equals(doMethod)) && (!"doPost".equals(doMethod)))
			throw new RequestStoreHouseException("doMethod is not doGet nor doPost.");

		this.doMethod = doMethod;
	}

	public String getDoMethod() {
		return this.doMethod != null ? this.doMethod : "";
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void copyRequestParameters() {

		if (this.requestParams == null) {
			this.requestParams = new HashMap<String, String[]>();
		}

		String parameterName = null;
		String[] valuesArrayIn = null;
		String[] valuesArrayTmp = null;

		if (this.request != null) {
			// Get the values of all request parameters
			Enumeration<String> parametersEnum = this.request.getParameterNames();

			if (parametersEnum != null) {
				while (parametersEnum.hasMoreElements()) {
					// Get the name of the request parameter
					parameterName = (parametersEnum.nextElement()).toString();
					valuesArrayIn = this.request.getParameterValues(parameterName);
					if (valuesArrayIn != null) {
						valuesArrayTmp = new String[valuesArrayIn.length];
						int j = 0;
						for (int i = 0; i < valuesArrayIn.length; i++) {
							if ((valuesArrayIn[i] != null) && (!"".equals(valuesArrayIn[i]))) {
								valuesArrayTmp[j] = valuesArrayIn[i];
								j++;
							}
						}
						if (j != 0) {
							this.requestParams.put(parameterName, valuesArrayTmp);
						}
					}
					this.request.removeAttribute(parameterName);
				}
			}
		}
	}

	private void saveRequestParameters() {
		if (this.resultsStoreHouse != null) {
			this.resultsStoreHouse.setRequestParamsHashMap(requestParams);
		}
	}

	private void restoreRequestParameters() {
		if (this.requestParams == null) {
			this.requestParams = new HashMap<String, String[]>();
		}
		if ((this.resultsStoreHouse != null) && (this.resultsStoreHouse.getRequestParamsHashMap() != null)) {
			HashMap<String, String[]> oldValuesHashMap = this.resultsStoreHouse.getRequestParamsHashMap();
			Set<String> keys = getFullKeysSet(oldValuesHashMap.keySet(), this.requestParams.keySet());

			for (String key : keys) {
				String[] oldValues = oldValuesHashMap.get(key);
				String[] newValues = this.requestParams.get(key);

				HashMap<String, String> valuesAL = new HashMap<String, String>();

				if (newValues != null) {
					for (int i = (newValues.length - 1); i >= 0; i--) {
						String value = newValues[i];
						if ((value != null) && (!"".equals(value))) {
							valuesAL.put(value, value);
						}
					}
				}

				if (oldValues != null) {
					for (int i = (oldValues.length - 1); i >= 0; i--) {
						String value = oldValues[i];
						if ((value != null) && (!"".equals(value))) {
							valuesAL.put(value, value);
						}
					}
				}
				Set<String> valuesSet = valuesAL.keySet();
				String[] values = valuesSet.toArray(new String[valuesSet.size()]);
				this.requestParams.put(key, values);

			}
		}
	}

	public String getRequestParametersString() {
		StringBuilder requestSB = new StringBuilder();
		String[] keys = getRequestParametersNames();
		for (int i = 0; i < keys.length; i++) {
			String paramName = keys[i];
			if ((paramName != null) && (!"".equals(paramName))) {
				if (i != 0) {
					requestSB.append("&");
				}
				requestSB.append(keys[i]);
				requestSB.append("=");
				String paramValue = getRequestParameter(paramName);
				paramValue = (paramValue == null) ? "" : paramValue;
				requestSB.append(paramValue);
			}
		}
		return requestSB.toString();
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public boolean requestIsMultipartContent() {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		return isMultipart;
	}

	private String[] getRequestParametersNames() {
		if (this.requestParams == null) {
			return new String[0];
		}
		Set<String> keysSet = this.requestParams.keySet();
		String[] keys = keysSet.toArray(new String[keysSet.size()]);
		Arrays.sort(keys);
		return keys;
	}

	public String getRequestParameter(String paramName) {
		String[] values = this.requestParams.get(paramName);
		if (values != null) {
			if (values.length > 0)
				return values[0];
		}
		return "";
	}

	public String getRequestUrlString() {
		if (this.request.getRequestURL() == null)
			return null;
		return this.request.getRequestURL().toString();
	}

	public String getRequestServerName() {
		return this.request.getServerName();
	}

	public String getAuthProviderId() {
		String providerId = "";
		if (session != null)
			providerId = session.getAuthProviderId();

		if ((providerId == null) || ("".equals(providerId))) {
			providerId = (String) request.getParameter(KConstants.Request.providerId);
			if (session != null){
				try {
					session.setAuthProviderId(providerId);
				} catch (SessionStoreHouseException e) {
					e.printStackTrace();
				}
			}
		}
		return providerId;
	}

	public ProgramFileInfo getProgramFileInfo() throws FilesAndPathsException {
		String fileName = getRequestParameter(KConstants.Request.fileNameParam);
		String fileOwner = getRequestParameter(KConstants.Request.fileOwnerParam);

		return new ProgramFileInfo(fileOwner, fileName);
	}

	public boolean isAjax() {
		String isAjaxString = getRequestParameter(KConstants.Request.isAjaxParam);
		boolean isAjax = (isAjaxString != null) && (KConstants.Values.True.equals(isAjaxString));
		return isAjax;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public ResultsStoreHouse getResultsStoreHouse() {

		if (this.resultsStoreHouse == null) {
			this.resultsStoreHouse = (ResultsStoreHouse) this.getRequest().getAttribute(KConstants.Request.resultsStoreHouse);
		}

		if (this.resultsStoreHouse == null) {
			this.resultsStoreHouse = new ResultsStoreHouse();
		}

		return resultsStoreHouse;
	}

	public void storeResultsStoreHouse() {
		this.getRequest().removeAttribute(KConstants.Request.resultsStoreHouse);
		if (resultsStoreHouse != null) {
			this.saveRequestParameters();
			this.getRequest().setAttribute(KConstants.Request.resultsStoreHouse, this.resultsStoreHouse);
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private Set<String> getFullKeysSet(Set<String> keySetIn1, Set<String> keySetIn2) {

		HashMap<String, String> keys = new HashMap<String, String>();

		if (keySetIn1 != null) {
			for (String key : keySetIn1) {
				if ((key != null) && (!"".equals(key))) {
					keys.put(key, key);
				}
			}
		}

		if (keySetIn2 != null) {
			for (String key : keySetIn2) {
				if ((key != null) && (!"".equals(key))) {
					keys.put(key, key);
				}
			}
		}
		return keys.keySet();
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

}

// EOF
