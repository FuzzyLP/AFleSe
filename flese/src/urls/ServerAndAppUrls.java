package urls;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import auxiliar.Dates;
import constants.KConstants;

public class ServerAndAppUrls {
	static final Log LOG = LogFactory.getLog(ServerAndAppUrls.class);
	
	private static ServerAndAppUrlsData serverAndAppUrlsData = new ServerAndAppUrlsData(null);
	private static String loadDate = Dates.getStringOfCurrentDate();

	public static String getRunningSince() {
		return loadDate;
	}
	
	public static String getAppUrl() {
		return getAppUrl(null);
	}

	public static String getAppUrl(HttpServletRequest request) {
		if (! serverAndAppUrlsData.isValid()) {
			computeAppAndServerUrls(request);
		}

		return serverAndAppUrlsData.getAppUrl();
	}

	public static String getAppFullUrl() {
		return getAppUrl(null);
	}

	public static String getAppFullUrl(HttpServletRequest request) {
		if (! serverAndAppUrlsData.isValid()) {
			computeAppAndServerUrls(request);
		}
		
		return serverAndAppUrlsData.toString();
	}

	public static boolean isAppInTestingMode(HttpServletRequest request) {
		if (! serverAndAppUrlsData.isValid()) {
			computeAppAndServerUrls(request);
		}
		
		boolean isAppInTestingMode = serverAndAppUrlsData.isAppInTestingMode(); 
		return isAppInTestingMode;
	}
	
	private static void computeAppAndServerUrls(HttpServletRequest request) {

		if (serverAndAppUrlsData.isValid()) {
			return;
		}

		if (request == null) {
			return;
		}

		// No usar (serverName = moises.ls.fi.upm.es/java-apps no me sirve !!!): 
		// String serverName = request.getServerName();
		// if ((serverName == null) || ("".equals(serverName))) {
		//	 return;
		// }
		// LOG.info("serverName: " + serverName);

		String url = request.getRequestURL().toString();
		if ((url == null) || ("".equals(url))) {
			return;
		}

		ServerAndAppUrlsData tmp = new ServerAndAppUrlsData(url);
		tmp = fixValuesWhenInMoises(tmp);
		set(tmp);
		LOG.info("set: ServerAndAppUrlsData: " + tmp.toString());
	}

	private static synchronized void set(ServerAndAppUrlsData tmp) {
		if (serverAndAppUrlsData.isValid()) {
			return;
		}
		serverAndAppUrlsData = tmp;
	}
	
	private static ServerAndAppUrlsData fixValuesWhenInMoises(ServerAndAppUrlsData tmp) {
		if ("moises.ls.fi.upm.es".equals(tmp.getServerUrl())) {
			String preUrl = KConstants.Application.httpsPrefix;
			String serverUrl = tmp.getServerUrl();
			String serverPort = "443";
			String appUrl = tmp.getAppUrl();
			ServerAndAppUrlsData aux = new ServerAndAppUrlsData(preUrl + serverUrl + ":" + serverPort + appUrl);
			return aux;
		}
		return tmp;
	}
	
}
