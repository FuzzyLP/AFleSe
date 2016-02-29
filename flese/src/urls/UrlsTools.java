package urls;

import javax.servlet.http.HttpServletRequest;

import constants.KConstants;
import filesAndPaths.PathsUtils;

public class UrlsTools {

	private boolean isFirstParam = true;
	private StringBuilder currentUrl = null;

	public UrlsTools(boolean withServerPath, boolean withAppPath, boolean withServletPath, String url, HttpServletRequest request) {
		if (url == null) {
			url = "";
		}
		String urlAux;
		if (withServerPath) {
			urlAux = PathsUtils.concatPathsStrings(ServerAndAppUrls.getAppFullUrl(request), url);
		} else {
			if (withAppPath) {
				urlAux = PathsUtils.concatPathsStrings(ServerAndAppUrls.getAppUrl(request), url);
			}
			else {
				urlAux = url;
			}
		}
		if (withServletPath) {
			urlAux = PathsUtils.concatPathsStrings(urlAux, KConstants.Application.servletName);
		}
		
		// We do not want the url without the server info !!!
		while (urlAux.startsWith("/")) {
			urlAux = urlAux.substring(1);
		}
		
		this.currentUrl = new StringBuilder();
		this.currentUrl.append(urlAux);
		isFirstParam = true;
	}

	public void addParam(String paramName, String paramValue) {
		if ((paramName == null) || ("".equals(paramName)))
			return;
		if ((paramValue == null) || ("".equals(paramValue)))
			return;

		if (this.isFirstParam) {
			this.isFirstParam = false;
			this.currentUrl.append("?");
		} else {
			this.currentUrl.append("&");
		}
		this.currentUrl.append(paramName + "=" + paramValue);
	}

	public String getResult() {
		return this.currentUrl.toString();
	}
}
