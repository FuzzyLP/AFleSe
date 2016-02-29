package filesAndPaths;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import constants.KConstants;

public class PathsMgmt {

	private static final Log LOG = LogFactory.getLog(PathsMgmt.class);

	private static String programFilesPath = null;
	private static String plServerPath = null;

	public PathsMgmt() throws FilesAndPathsException {
		if (programFilesPath == null) {
			String tmpProgramFilesPath = determineProgramFilesValidPath(KConstants.PathsMgmt.programFilesValidPaths);
			setProgramFilesPath(tmpProgramFilesPath);
			LOG.info("programFilesPath: " + programFilesPath);
		}

		if (plServerPath == null) {
			String tmpPlServerPath = determinePlServerValidPath(KConstants.PathsMgmt.plServerValidSubPaths);
			setPlServerPath(tmpPlServerPath);
			LOG.info("plServerPath: " + plServerPath);
		}

		if (programFilesPath == null) {
			throw new FilesAndPathsException("programFilesPath cannot be null.");
		}

		if (plServerPath == null) {
			throw new FilesAndPathsException("plServerPath cannot be null.");
		}
	}

	public String getProgramFilesPath() throws FilesAndPathsException {
		if (programFilesPath == null)
			throw new FilesAndPathsException("programFilesPath cannot be null.");
		return programFilesPath;
	}

	public String getPlServerPath() throws FilesAndPathsException {
		if (plServerPath == null)
			throw new FilesAndPathsException("plServerPath cannot be null.");
		return plServerPath;
	}

	private synchronized void setProgramFilesPath(String tmpProgramFilesPath) throws FilesAndPathsException {
		if (programFilesPath == null) {
			programFilesPath = tmpProgramFilesPath;
		}
	}

	private synchronized void setPlServerPath(String tmpPlServerPath) throws FilesAndPathsException {
		if (plServerPath == null) {
			plServerPath = tmpPlServerPath;
		}
	}

	/**
	 * Returns which one of the programFilesValidPaths is the adequate one. It
	 * is recommended not to run this method more than once.
	 * 
	 * @param programFilesValidPaths
	 *            is a list with the paths to test.
	 */
	private String determineProgramFilesValidPath(String[] programFilesValidPaths) throws FilesAndPathsException {
		String programFilesValidPath = null;
		int index = 0;
		KConstants.PathsMgmt.loadConfig();

		while (((programFilesValidPath == null) || ("".equals(programFilesValidPath)))
				&& (index < programFilesValidPaths.length)) {
			try {
				LOG.info("determineProgramFilesValidPath: checking " + programFilesValidPaths[index]);
				if (PathsUtils.testIfFolderExists(programFilesValidPaths[index], true)) {
					programFilesValidPath = programFilesValidPaths[index];
				}
			} catch (Exception e) {
				programFilesValidPath = null;
			}
			if (programFilesValidPath == null) {
				if ((programFilesValidPaths != null) && (index < programFilesValidPaths.length)
						&& (programFilesValidPaths[index] != null))
					LOG.info("determineProgramFilesValidPath: invalid: " + programFilesValidPaths[index]);
				index++;
			}
		}

		if ((programFilesValidPath == null) || ("".equals(programFilesValidPath)))
			throw new FilesAndPathsException("programFilesValidPath cannot be null.");

		LOG.info("determineProgramFilesValidPath: ok: " + programFilesValidPath);
		return programFilesValidPath;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Looks which one of the proposed subPaths for the plServer is the correct
	 * one.
	 * 
	 * @param plServerValidSubPaths
	 *            are the new proposed subpaths for the plServer.
	 * @throws FilesAndPathsException
	 *             when none is valid.
	 * 
	 */
	private String determinePlServerValidPath(String[] plServerValidSubPaths) throws FilesAndPathsException {
		String plServerPath = null;
		int index = 0;

		while (((plServerPath == null) || ("".equals(plServerPath))) && (index < plServerValidSubPaths.length)) {
			LOG.info(plServerValidSubPaths[index]);

			plServerPath = lookForPlServerFileInSubDir(plServerValidSubPaths[index]);

			if (plServerPath == null)
				index++;
		}

		if (plServerPath == null) {
			throw new FilesAndPathsException("plServerPath cannot be null.");
		}
		return plServerPath;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private String lookForPlServerFileInSubDir(String subPath) {
		String result = null;

		if (subPath != null) {
			File file = new File(subPath);

			if ((file.exists()) && (file.canRead()) || (file.canExecute())) {
				if (file.isFile()) {
					if (KConstants.PathsMgmt.plServerProgramFileName.equals(file.getName())) {
						result = subPath;
					}
				} else {
					if (file.isDirectory()) {
						File[] subFiles = file.listFiles();
						int index = 0;
						while ((result == null) && (index < subFiles.length)) {
							result = lookForPlServerFileInSubDir(subFiles[index].getAbsolutePath());
							
						}
					} else {
						LOG.info("Impossible to process path (not a file nor a directory): " + subPath);
					}
				}
			} else {
				LOG.info("Impossible to process path (not exists, not readable or not executable): " + subPath);
			}
		}
		return result;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void createFolder(String subPath, boolean exceptionIfExists) throws FilesAndPathsException {

		if (subPath == null) {
			throw new FilesAndPathsException("subPath cannot be null.");
		}

		String folderPath = null;

		if (!subPath.startsWith(programFilesPath)) {
			folderPath = programFilesPath + subPath;
		} else {
			folderPath = subPath;
		}
		Path dir = Paths.get(folderPath);

		if (Files.exists(dir)) {
			if (exceptionIfExists) {
				throw new FilesAndPathsException("Cannot create folder " + folderPath + " because it exists.");
			}
		} else {
			try {
				Files.createDirectory(dir);
			} catch (IOException e) {
				e.printStackTrace();
				throw new FilesAndPathsException("Cannot create folder " + folderPath);
			}
		}
	}

}
