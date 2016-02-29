package urls;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import constants.KConstants;

public class ServerAndAppUrlsData {
	static final Log LOG = LogFactory.getLog(ServerAndAppUrlsData.class);

	private String preUrl = null;
	private String serverUrl = null;
	private String serverPort = null;
	private String appUrl = null;
	
	/**
	 * This are the invalid ends for the serverUrl computed. Be carefull:
	 * removal order matters.
	 */
	private static final String[] invalidTailsStarts = { "?", "index.jsp", "WEB-INF", "servlets", "Servlet", "images", "js_and_css" };
	

	public ServerAndAppUrlsData(String url) {
		LOG.info("url: " + url + " ");
		if ((url != null) && (!"".equals(url))) {
			parseUrl(url);
		}
		
		LOG.info("url (with spaces): " + preUrl + " " + serverUrl + " " + serverPort + " " + appUrl + " ");
	}

	public boolean isValid() {
		boolean valid = ((this.preUrl != null) && (this.serverUrl != null) && (this.serverPort != null) && (this.appUrl != null));
		valid = valid
				&& ((!"".equals(this.preUrl)) && (!"".equals(this.serverUrl)) && (!"".equals(this.serverPort)) && (!"".equals(this.appUrl)));
		return (valid);
	}

	public String getPreUrl() {
		return (preUrl != null) ? preUrl : "";
	}

	public String getServerUrl() {
		return (serverUrl != null) ? serverUrl : "";
	}

	public String getServerPort() {
		return (serverPort != null) ? serverPort : "";
	}

	public String getAppUrl() {
		return (appUrl != null) ? appUrl : "";
	}

	public String toString() {
		if (isValid()) {
			return getPreUrl() + getServerUrl() + ":" + getServerPort() + getAppUrl();	
		}
		return "";
	}
	
	public boolean isAppInTestingMode() {
		String subPart = KConstants.Application.testModeSubUrl.toLowerCase();
		String url = toString().toLowerCase();
		return url.contains(subPart);
	}
	
	
	private void parseUrl(String url) {
		this.preUrl = null;
		if (url.startsWith(KConstants.Application.httpPrefix)) {
			url = url.substring(KConstants.Application.httpPrefix.length());
			this.preUrl = KConstants.Application.httpPrefix;
		}
		if (url.startsWith(KConstants.Application.httpsPrefix)) {
			url = url.substring(KConstants.Application.httpsPrefix.length());
			this.preUrl = KConstants.Application.httpsPrefix;
		}
		
		url = removeSlashCharacter(url);

		int index = 0;
		// Remove the invalid ends.
		url = removeInvalidTails(url);

		// Now get the server name and port.
		this.serverUrl = null;
		this.serverPort = "80";
		
		index = url.indexOf(":");
		if (index > -1) {
			this.serverUrl = url.substring(0, index);
			url = url.substring(index +1); // Remove ':' too.
			
			index = url.indexOf('/');
			if (index > -1) {
				if (index > 0) {
					this.serverPort = url.substring(0, index);
				}
				url = url.substring(index);
			}
		}
		else {
			index = url.indexOf('/');
			if (index > -1) {
				this.serverUrl = url.substring(0, index);
				url = url.substring(index);
			}			
		}

		this.appUrl = url;
	}
	
	/**
	 * Looks for the invalid ends defined in invalidEnds and removes the tail of
	 * the string, including the invalid end.
	 * 
	 * @param url
	 *            is the input string.
	 * @return the url without the invalid ends.
	 */
	private String removeInvalidTails(String url) {
		int index = -1;
		String invalidEnd = null;
		for (int i = 0; i < invalidTailsStarts.length; i++) {
			invalidEnd = invalidTailsStarts[i];
			index = url.indexOf(invalidEnd);
			if (index > -1) {
				url = url.substring(0, index);
			}
		}
		return url;
	}
	
	private String removeSlashCharacter(String input) {
		while (input.startsWith("/")) {
			input = input.substring(1);
		}
		return input;
	}
}
