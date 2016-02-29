package prologConnector;

import CiaoJava.PLAtom;
import CiaoJava.PLStructure;
import CiaoJava.PLTerm;
import CiaoJava.PLVariable;
import filesAndPaths.FilesAndPathsException;
import filesAndPaths.ProgramFileInfo;

public class CiaoPrologCheckWorkingFolderQuery extends CiaoPrologQueryAbstract {

	public boolean isOfType(String type) {
		return CiaoPrologQueryAbstract.Constants.ChangeWorkingFolderQuery.equals(type);
	}

	private CiaoPrologCheckWorkingFolderQuery(ProgramFileInfo programFileInfo) throws CiaoPrologConnectorException, FilesAndPathsException {
		super(programFileInfo);

		String programFileFolderName = ".";

		// Prepare the variables to get the result
		PLVariable[] variables = new PLVariable[1];
		variables[0] = new PLVariable();

		// Prepare the variables names
		String[] variablesNames = new String[1];
		variablesNames[0] = "result";

		// Prepare the "change working folder" query.
		PLStructure query = new PLStructure("working_directory", new PLTerm[] { variables[0], new PLAtom(programFileFolderName) });

		setRealQuery(query, variables, variablesNames);

	}

	public static CiaoPrologCheckWorkingFolderQuery getInstance(ProgramFileInfo programFileInfo) throws CiaoPrologConnectorException,
			FilesAndPathsException {
		return new CiaoPrologCheckWorkingFolderQuery(programFileInfo);
	}

	@Override
	public void adequationOfQueryAnswers() {
	}

}
