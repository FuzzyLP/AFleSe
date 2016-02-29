package urls;

import javax.servlet.http.HttpServletRequest;

import storeHouse.RequestStoreHouse;
import constants.KConstants;

public class UrlMap {

	private String manager = null;
	private String op = null;
	private UrlMap nextPage = null;
	private UrlMap exceptionPage = null;
	private String currentUrl = null;

	private class Constants {
		public static final String defaultManager = "defaultManager";
		public static final String defaultOp = "defaultOp";
	}

	private UrlMap(String manager, String op, UrlMap nextPage, UrlMap exceptionPage, String currentUrl) {
		if (manager == null)
			manager = "";
		if (op == null)
			op = "";
		if (currentUrl == null)
			currentUrl = "";

		this.manager = adequateManagerName(manager);
		this.op = adequateOpName(op);
		this.nextPage = nextPage;
		this.exceptionPage = exceptionPage;
		this.currentUrl = currentUrl;
	}

	protected static UrlMap getUrlMap(String manager, String op, UrlMap nextPage, UrlMap exceptionPage) {
		return new UrlMap(manager, op, nextPage, exceptionPage, "");
	}

	public static UrlMap getUrlMap(String manager, String op, UrlMap nextPage, UrlMap exceptionPage, String currentUrl) {
		return new UrlMap(manager, op, nextPage, exceptionPage, currentUrl);
	}

	public static UrlMap getUrlMap(RequestStoreHouse sessionStoreHouse) {
		String manager = sessionStoreHouse.getRequestParameter(KConstants.Request.managerParam);
		String op = sessionStoreHouse.getRequestParameter(KConstants.Request.operationParam);
		return new UrlMap(manager, op, null, null, null);
	}

	private String adequateManagerName(String managerName) {
		if ((managerName == null) || ("".equals(managerName))) {
			return Constants.defaultManager;
		}
		if (!managerName.endsWith(KConstants.Managers.managerSuffix)) {
			return managerName + KConstants.Managers.managerSuffix;
		}
		return managerName;
	}

	private String adequateOpName(String op) {
		if ((op == null) || ("".equals(op))) {
			return Constants.defaultOp;
		}
		return op;
	}

	public String getManager(boolean acceptDefaults) {
		if (acceptDefaults) {
			return this.manager;
		}
		if (Constants.defaultManager.equals(this.manager)) {
			return "";
		}
		return this.manager;
	};

	public String getOp(boolean acceptDefaults) {
		if (acceptDefaults) {
			return this.op;
		}
		if (Constants.defaultOp.equals(this.op)) {
			return "";
		}
		return this.op;
	};

	public String getUrl(boolean isAjax) {
		return getUrl(false, false, isAjax, null);
	}

	public String getUrl(boolean withServerPath, boolean withAppPath, boolean isAjax, HttpServletRequest request) {
		boolean withServletPath = ("".equals(this.currentUrl)); // If empty we
																// need the
																// servlet !!!
		return getUrl(withServerPath, withAppPath, withServletPath, isAjax, request);
	}

	public String getUrl(boolean withServerPath, boolean withAppPath, boolean withServletPath, boolean isAjax, HttpServletRequest request) {

		UrlsTools urlTool = new UrlsTools(withServerPath, withAppPath, withServletPath, this.currentUrl, request);

		urlTool.addParam(KConstants.Request.managerParam, managerNameToParam());
		urlTool.addParam(KConstants.Request.operationParam, operationNameToParam());
		urlTool.addParam(KConstants.Request.isAjaxParam, isAjax ? KConstants.Values.True : "");

		return urlTool.getResult();
	};

	private String managerNameToParam() {
		String managerName = getManager(false);
		if (!"".equals(managerName)) {

			if (managerName.endsWith(KConstants.Managers.managerSuffix)) {
				int end = managerName.length() - KConstants.Managers.managerSuffix.length();
				managerName = managerName.substring(0, end);
			}
		}
		return managerName;
	}

	private String operationNameToParam() {
		String opName = getOp(false);
		return opName;
	}

	public String getCurrentUrl() {
		return this.currentUrl;
	}

	public String getNextPage() {
		if (this.nextPage != null)
			return this.nextPage.getCurrentUrl();
		return "";
	}

	public String getExceptionPage() {
		if (this.exceptionPage != null)
			return this.exceptionPage.getCurrentUrl();
		return "";
	}
}
