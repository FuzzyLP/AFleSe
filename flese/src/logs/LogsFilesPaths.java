package logs;

import constants.KConstants;
import filesAndPaths.FilesAndPathsException;
import filesAndPaths.PathsMgmt;
import filesAndPaths.PathsUtils;

public class LogsFilesPaths {

	private static String logsFolderPath = null;

	LogsFilesPaths() {
		if (logsFolderPath == null) {
			String logsFolderPathAux = determineLogsFolderPath();
			setLogsFolderPath(logsFolderPathAux);
		}
	}

	private synchronized void setLogsFolderPath(String logsFolderPathAux) {
		if (logsFolderPath == null) {
			logsFolderPath = logsFolderPathAux;
		}
	}

	private String determineLogsFolderPath() {

		PathsMgmt pathsMgmt;
		String programFilesPath;
		String logsFolderPathAux;

		try {
			pathsMgmt = new PathsMgmt();
			programFilesPath = pathsMgmt.getProgramFilesPath();
		} catch (FilesAndPathsException e) {
			e.printStackTrace();
			pathsMgmt = null;
			programFilesPath = null;
		}

		if (programFilesPath != null) {
			logsFolderPathAux = PathsUtils.concatPathsStrings(programFilesPath, KConstants.Application.LogsFolder);
		} else {
			logsFolderPathAux = null;
		}

		return logsFolderPathAux;
	}
	
	public String getLogsFolderPath() {
		return logsFolderPath;
	}

}

/*
 * 
 */

/* EOF */