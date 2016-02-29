package logs;

import prologConnector.CiaoPrologQueryInterface;
import auxiliar.Dates;
import auxiliar.LocalUserInfo;
import constants.KConstants;

public class LogsManager {

	private static final String puntoYComa = ";";
	private static final String extension = ".csv";
	
	private static LogsFiles logFileForQueries = null;
	private static LogsFiles logFileForSignedUsers = null;

	public static void logSignedUser(String userName) {
		try {
			logSignedUserAux(userName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void logSignedUserAux(String userName) throws Exception {
		String date = Dates.getStringOfCurrentDate();
		if (logFileForSignedUsers == null) {
			newLogsFileForSignedUsers(date);
		}
		StringBuilder toLog = new StringBuilder();
		toLog.append(date);
		toLog.append(puntoYComa);
		toLog.append(userName);
		String toLogString = toLog.toString();
		logFileForSignedUsers.append(toLogString);
	}

	private synchronized static void newLogsFileForSignedUsers(String date) {
		if (logFileForSignedUsers == null) {
			String fileName = KConstants.Application.LogsFileForSignedUsers + date + extension;
			logFileForSignedUsers = new LogsFiles(fileName);
		}
	}
	
	public static void logQuery(CiaoPrologQueryInterface query, LocalUserInfo localUserInfo) {
		try {
			logQueryAux(query, localUserInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void logQueryAux(CiaoPrologQueryInterface query, LocalUserInfo localUserInfo) throws Exception {
		String date = Dates.getStringOfCurrentDate();
		if (logFileForQueries == null) {
			newLogsFileForQueries(date);
		}
		StringBuilder toLog = new StringBuilder();
		toLog.append(date);
		toLog.append(puntoYComa);
		toLog.append(localUserInfo == null ? "unknown" : localUserInfo.getLocalUserName());
		toLog.append(puntoYComa);
		toLog.append(query.getProgramFileInfo().getFileOwner());
		toLog.append(puntoYComa);
		toLog.append(query.getProgramFileInfo().getFileName());
		toLog.append(puntoYComa);
		toLog.append(query.toString());
		String toLogString = toLog.toString();
		logFileForQueries.append(toLogString);

	}

	private synchronized static void newLogsFileForQueries(String date) {
		if (logFileForQueries == null) {
			String fileName = KConstants.Application.LogsFileForQueries + date + extension;
			logFileForQueries = new LogsFiles(fileName);
		}
	}
	
	public static String getLogsSignedUsers() {
		LogsFilesPaths logsFilesPaths = new LogsFilesPaths();
		String folderName = logsFilesPaths.getLogsFolderPath();
		String prefix = KConstants.Application.LogsFileForSignedUsers;
		
		LogsFilesAux logsFilesAux = new LogsFilesAux();
		return logsFilesAux.getContentsOfAllFilesInFolderStartingBy(folderName, prefix);
	}
	
	public static String getLogsQueries() {
		LogsFilesPaths logsFilesPaths = new LogsFilesPaths();
		String folderName = logsFilesPaths.getLogsFolderPath();
		String prefix = KConstants.Application.LogsFileForQueries;
		
		LogsFilesAux logsFilesAux = new LogsFilesAux();
		return logsFilesAux.getContentsOfAllFilesInFolderStartingBy(folderName, prefix);
		
	}
}
