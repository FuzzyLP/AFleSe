package logs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import auxiliar.Dates;
import constants.KConstants;
import filesAndPaths.FilesAndPathsException;
import filesAndPaths.PathsMgmt;
import filesAndPaths.PathsUtils;

public class LogsFiles {

	private File file;

	LogsFiles(String fileName) {
		if ((fileName == null) || ("".equals(fileName))) {
			fileName = KConstants.Application.LogsDefaultName + Dates.getStringOfCurrentDate();
		}

		String fileFullPath = null;
		LogsFilesPaths logsFilesPaths = new LogsFilesPaths();
		String logsFolderPath = logsFilesPaths.getLogsFolderPath();

		if (logsFolderPath != null) {
			fileFullPath = PathsUtils.concatPathsStrings(logsFolderPath, fileName);
		} else {
			fileFullPath = null;
		}

		if (fileFullPath != null) {
			if (!existsFile(fileFullPath)) {
				createFile(fileFullPath, logsFolderPath);
			}
		}
	}

	protected void finalize() throws Throwable {
		// Invoke the finalizer of our superclass
		// We haven't discussed superclasses or this syntax yet
		super.finalize();

		// Now get rid of the resources you do not need any more.
		// closeFile();
	}

	private boolean existsFile(String fileFullPath) {
		if ((fileFullPath == null) || ("".equals(fileFullPath))) {
			return false;
		}
		File file = new File(fileFullPath);
		boolean retVal = file.exists();
		return retVal;
	}

	private void createFile(String fileFullPath, String folderFullPath) {
		boolean error = false;
		try {
			PathsMgmt pathsMgmt = new PathsMgmt();
			pathsMgmt.createFolder(folderFullPath, false);
		} catch (FilesAndPathsException e) {
			e.printStackTrace();
			error = true;
		}

		if (!error) {
			createFileAux(fileFullPath);
		}
	}

	synchronized private void createFileAux(String fileFullPath) {
		this.file = new File(fileFullPath);
		try {
			this.file.createNewFile();
		} catch (IOException e) {
			this.file = null;
		}

		if (this.file != null) {
			FileWriter fw = openFileWriter();
			writeLogos(fw);
			closeFileWriter(fw);
		}
	}

	private FileWriter openFileWriter() {
		FileWriter fw = null;
		if (this.file != null) {
			try {
				fw = new FileWriter(this.file.getAbsoluteFile(), true);
			} catch (IOException e) {
				e.printStackTrace();
				fw = null;
			}
		}
		return fw;
	}

	private void closeFileWriter(FileWriter fw) {
		if (fw != null) {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void writeLogos(FileWriter fw) {
		if (this.file != null) {
			String fileName = this.file.getName();
			try {
				fw.write("\n");
				fw.write("### - FleSe Application Logs -- " + fileName + " - ###");
				fw.write("\n");
				fw.write("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	synchronized private void appendAux(FileWriter fw, String msg) {
		try {
			fw.write(msg);
			fw.write("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void append(String msg) {
		if ((msg != null) && (!"".equals(msg))) {
			FileWriter fw = openFileWriter();
			appendAux(fw, msg);
			closeFileWriter(fw);
		}
	}

}

/*
 * 
 */

/* EOF */