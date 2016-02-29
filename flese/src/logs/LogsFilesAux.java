package logs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import auxiliar.FileNamesComparator;

public class LogsFilesAux {

	public String getContentsOfAllFilesInFolderStartingBy(String folderName, String prefix) {
		StringBuilder filesContentsSB = new StringBuilder();
		if (folderName != null) {
			File file = new File(folderName);

			if ((file.exists()) && (file.canRead()) || (file.canExecute())) {
				if (file.isDirectory()) {
					File[] subFiles = file.listFiles();
					Arrays.sort(subFiles, FileNamesComparator.PERSONALIZED);

					for (File subFile : subFiles) {
						if (subFile.getName().contains(prefix)) {
							String fileContents = getFileContents(subFile);
							filesContentsSB.append(fileContents);
						}
					}
				}
			}
		}
		String filesContents = filesContentsSB.toString();
		return filesContents;
	}

	private String getFileContents(File subFile) {
		StringBuilder fileContentsSB = new StringBuilder();
		if (subFile.isFile()) {
			String line;
			try {
				BufferedReader reader = new BufferedReader(new FileReader(subFile));
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (! "".equals(line)) {
						String lineAux = replaceProblematicChars(line);
						fileContentsSB.append(lineAux);
					}
					fileContentsSB.append(" \\n");
				}
				reader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String fileContents = fileContentsSB.toString();
		return fileContents;
	}

	private String replaceProblematicChars(String line) {
		String lineAux = line.replace("'", "\\'");
		return lineAux;
	}

}

/*
 * 
 */

/* EOF */