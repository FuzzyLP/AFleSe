package filesAndPaths;

import constants.KConstants;

public class FilesAndPathsException extends Exception {

	private static final long serialVersionUID = 1L;
	public FilesAndPathsException(String reason) {
		super(reason);
		KConstants.PathsMgmt.stateErrorConfigFile2 = true;
		KConstants.PathsMgmt.reasonError2 = reason;
	}
}
