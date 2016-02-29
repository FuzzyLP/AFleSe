package auxiliar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import logs.LogsManager;
import programAnalysis.ProgramPartAnalysis;
import storeHouse.RequestStoreHouse;
import storeHouse.ResultsStoreHouse;
import storeHouse.SessionStoreHouse;
import urls.ServerAndAppUrls;
import constants.KConstants;

public class JspsUtils {

	public static String getStringWithValueS() {
		return "S";
	}

	public static String includeMainBodyWhenNotAjax(RequestStoreHouse requestStoreHouse) {
		if (!isAjax(requestStoreHouse)) {
			return "<jsp:include page='commonHtmlBody.jsp' />";
		}
		return "";
	}

	public static RequestStoreHouse getRequestStoreHouse(HttpServletRequest request) {
		return RequestStoreHouse.getRequestStoreHouse(request);
	}

	public static boolean isAjax(RequestStoreHouse requestStoreHouse) {
		if (requestStoreHouse != null) {
			return requestStoreHouse.isAjax();
		}
		return false;
	}

	public static String getLocalUserInfoName(SessionStoreHouse sessionStoreHouse) {

		String localUserInfoName = "";
		if (sessionStoreHouse != null) {
			LocalUserInfo localUserInfo = sessionStoreHouse.getLocalUserInfo();
			if (localUserInfo != null) {
				localUserInfoName = localUserInfo.getLocalUserName();
			}
		}
		return localUserInfoName;
	}

	public static SessionStoreHouse getSessionStoreHouse(RequestStoreHouse requestStoreHouse) {
		SessionStoreHouse sessionStoreHouse = null;
		if (requestStoreHouse != null)
			try {
				sessionStoreHouse = requestStoreHouse.getSession();
			} catch (Exception e) {
				sessionStoreHouse = null;
			}
		return sessionStoreHouse;
	}

	public static ResultsStoreHouse getResultsStoreHouse(RequestStoreHouse requestStoreHouse) {
		ResultsStoreHouse resultsStoreHouse = null;
		if (requestStoreHouse != null) {
			resultsStoreHouse = requestStoreHouse.getResultsStoreHouse();
		}
		return resultsStoreHouse;
	}

	public static String getExceptionMsgInJS(ResultsStoreHouse resultsStoreHouse) {
		String msg = resultsStoreHouse.getExceptionMsg();
		String[] msgsAux = { msg };
		return getMessagesInJS(msgsAux);
	}

	public static String getResultMessagesInJS(ResultsStoreHouse resultsStoreHouse) {
		String msg = "";
	
		if (resultsStoreHouse != null) {
			String[] msgs = resultsStoreHouse.getResultMessages();
			msg = getMessagesInJS(msgs);
		}
		
		return msg;
	}

	public static String getEmptyArrayMessagesInJs() {
		return "new Array()";
	}

	public static String getMessagesInJS(ArrayList<String> msgs) {
		String[] msgsAux = msgs.toArray(new String[msgs.size()]);
		String msg = getMessagesInJS(msgsAux);
		return msg;
	}

	public static String getMessagesInJS(String[] msgs) {

		StringBuilder msg = new StringBuilder();
		msg.append("new Array(");
		for (int i = 0; i < msgs.length; i++) {
			msg.append("'");
			if ((msgs[i] != null) && (!"".equals(msgs[i]))) {
				msg.append(msgs[i]);
			} else {
				msg.append(" ");
			}
			msg.append("'");
			if (i + 1 < msgs.length) {
				msg.append(", ");
			}
		}
		msg.append(")");
		return msg.toString();
	}

	public static String comboBoxDefaultValue() {
		return "<option id='----' title='----' value='----'>----</option>";
	}

	public static String getPrologNameInColloquialLanguage(String textIn) {

		if ((textIn == null) || ("".equals(textIn))) {
			return "";
		}

		textIn = fixNamesInSpecialCases(textIn);

		// debug.info("textIn: " + textIn);
		String text = null;
		int i = textIn.indexOf("_");
		while (i != -1) {
			text = ""; // Initialize
			text += textIn.substring(0, i);
			text += " ";
			text += textIn.substring(i + 1, textIn.length());
			// debug.info(textLabel);
			textIn = text;
			i = textIn.indexOf("_");
		}
		return textIn;
	}

	private static String fixNamesInSpecialCases(String textIn) {
		if (textIn == null)
			return null;
		if ("".equals(textIn))
			return "";

		if ("fnot".equals(textIn))
			return "not";

		return textIn;
	}

	public static String humanizeIfTrue(String text, boolean humanize) {
		if (humanize) {
			return getPrologNameInColloquialLanguage(text);
		}
		return text;
	}

	public static String getFromFuzzificationNameOf(ProgramPartAnalysis programPartAnalysis, String element, boolean humanize) {
		if (programPartAnalysis == null) {
			return "";
		}

		String predDefined = programPartAnalysis.getPredDefined();
		String predNecessary = programPartAnalysis.getPredNecessary();

		int indexI1 = predDefined.indexOf("(");
		int indexJ1 = predDefined.indexOf(")");

		int indexI2 = predNecessary.indexOf("(");
		// int indexJ2 = predNecessary.indexOf(")");

		String databaseName1 = predDefined.substring(indexI1 + 1, indexJ1);
		// String databaseName2 = predDefined.substring(indexI2 + 1, indexJ2);

		predDefined = predDefined.substring(0, indexI1);
		predNecessary = predNecessary.substring(0, indexI2);

		if (element.equals(KConstants.Fuzzifications.predDefined)) {
			return humanizeIfTrue(predDefined, humanize);
		}
		if (element.equals(KConstants.Fuzzifications.predNecessary)) {
			return humanizeIfTrue(predNecessary, humanize);
		}
		if (element.equals(KConstants.Fuzzifications.database)) {
			return humanizeIfTrue(databaseName1, humanize);
		}

		return "";
	}

	public static String getValue(String value) {
		return value;
	}

	public static String getRunningSince() {
		return ServerAndAppUrls.getRunningSince();
	}

	public static String[] getKeyValues(ProgramPartAnalysis[] fuzzifications) {
		HashMap<String, String> keyValues = new HashMap<String, String>();

		for (int i = 0; i < fuzzifications.length; i++) {
			HashMap<String, String> functionPoints = fuzzifications[i].getFunctionPoints();
			Set<String> functionKeyValuesSet = functionPoints.keySet();
			String[] functionKeyValues = functionKeyValuesSet.toArray(new String[functionKeyValuesSet.size()]);
			for (int j = 0; j < functionKeyValues.length; j++) {
				keyValues.put(functionKeyValues[j], functionKeyValues[j]);
			}
		}

		Set<String> keyValuesSet = keyValues.keySet();
		String[] keyValuesArray = keyValuesSet.toArray(new String[keyValuesSet.size()]);
		Arrays.sort(keyValuesArray, StringsComparator.PERSONALIZED);
		return keyValuesArray;
	}

	public static ProgramPartAnalysis getMyFuzzification(ProgramPartAnalysis[] fuzzifications, LocalUserInfo localUserInfo, String mode) {
		if (KConstants.Request.modeAdvanced.equals(mode)) {
			return getDefaultFuzzification(fuzzifications);
		}
		int i = 0;
		boolean found = false;
		while ((i < fuzzifications.length) && (!found)) {
			found = fuzzifications[i].getPredOwner().equals(localUserInfo.getLocalUserName());

			if (!found) {
				i++;
			}
		}
		return fuzzifications[i];
	}

	public static ProgramPartAnalysis getDefaultFuzzification(ProgramPartAnalysis[] fuzzifications) {
		int i = 0;
		boolean found = false;
		while ((i < fuzzifications.length) && (!found)) {
			found = fuzzifications[i].getPredOwner().equals(KConstants.Fuzzifications.DEFAULT_DEFINITION);

			if (!found) {
				i++;
			}
		}
		return fuzzifications[i];
	}

	public static ProgramPartAnalysis[] getOthersFuzzifications(ProgramPartAnalysis[] fuzzifications, LocalUserInfo localUserInfo,
			String mode) {
		ArrayList<ProgramPartAnalysis> result = new ArrayList<ProgramPartAnalysis>();
		int i = 0;
		while (i < fuzzifications.length) {
			if (!(fuzzifications[i].getPredOwner().equals(KConstants.Fuzzifications.DEFAULT_DEFINITION))) {

				if ((KConstants.Request.modeAdvanced.equals(mode))
						|| (!(fuzzifications[i].getPredOwner().equals(localUserInfo.getLocalUserName())))) {
					result.add(fuzzifications[i]);
				}
			}

			i++;
		}
		return result.toArray(new ProgramPartAnalysis[result.size()]);
	}

	public static String getValueFor(String keyValue, HashMap<String, String> functionPoints, HashMap<String, String> defaultPoints) {
		String value = functionPoints.get(keyValue);
		if (value == null) {
			value = defaultPoints.get(keyValue);
			if (value == null) {
				return "0";
			}
		}
		return value;
	}

	public static String convertFunctionPointsToJS(String name, String[] keyValues, HashMap<String, String> functionPoints) {
		boolean isTheFirstPoint = true;
		StringBuilder result = new StringBuilder();
		result.append("new fuzzificationPoints('" + name + "', '" + name + "', new Array(");

		for (int i = 0; i < keyValues.length; i++) {
			String value = functionPoints.get(keyValues[i]);
			if ((value != null) && (!"".equals(value))) {
				if (!isTheFirstPoint) {
					result.append(", ");
				}
				result.append("new Array(" + keyValues[i] + ", " + value + ")");
				isTheFirstPoint = false;
			}
		}
		result.append("))");
		return result.toString();
	}

	public static String getLogsQueries() {
		return LogsManager.getLogsQueries();
	}

	public static String getLogsSignedUsers() {
		return LogsManager.getLogsSignedUsers();
	}

	public static String setUrlParamServiceEndPoint(String queryUrl, String serviceEndPoint) {
		StringBuilder urlSB = new StringBuilder();
		urlSB.append(queryUrl);
		urlSB.append("&");
		urlSB.append(KConstants.Request.serviceEndPoint);
		urlSB.append("=");
		urlSB.append(serviceEndPoint);
		String urlAux = urlSB.toString();
		return urlAux;
	}

	public static String setUrlParamDivIdPrefix(String queryUrl, String divIdPrefix) {
		StringBuilder urlSB = new StringBuilder();
		urlSB.append(queryUrl);
		urlSB.append("&");
		urlSB.append(KConstants.Request.divIdPrefix);
		urlSB.append("=");
		urlSB.append(divIdPrefix);
		String urlAux = urlSB.toString();
		return urlAux;
	}

	public static String setUrlParamUrl(String queryUrl, String url) {
		StringBuilder urlSB = new StringBuilder();
		urlSB.append(queryUrl);
		urlSB.append("&");
		urlSB.append(KConstants.Request.url);
		urlSB.append("=");
		urlSB.append(url);
		String urlAux = urlSB.toString();
		return urlAux;
	}

	public static String getDivIdPrefix(ResultsStoreHouse resultsStoreHouse, String defaultDivIdPrefix) {
		String[] divIdPrefixes = resultsStoreHouse.getRequestParamsHashMap().get(KConstants.Request.divIdPrefix);
		String divIdPrefix = ((divIdPrefixes != null) && (divIdPrefixes.length > 0)) ? divIdPrefixes[0] : "";
		if ((divIdPrefix == null) || (divIdPrefix.isEmpty())) {
			divIdPrefix = defaultDivIdPrefix;
		}
		return divIdPrefix;
	}

	public static String getServiceEndPointParam(ResultsStoreHouse resultsStoreHouse) {
		String[] serviceEndPoints = resultsStoreHouse.getRequestParamsHashMap().get(KConstants.Request.serviceEndPoint);
		String serviceEndPoint = ((serviceEndPoints != null) && (serviceEndPoints.length > 0)) ? serviceEndPoints[0] : "";
		return serviceEndPoint;
	}
}

// END