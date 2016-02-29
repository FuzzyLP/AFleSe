package prologConnector;

import CiaoJava.PLStructure;
import CiaoJava.PLTerm;
import CiaoJava.PLVariable;
import filesAndPaths.FilesAndPathsException;
import filesAndPaths.ProgramFileInfo;

public class CiaoPrologTestingQuery extends CiaoPrologQueryAbstract {

	public boolean isOfType(String type) {
		return CiaoPrologQueryAbstract.Constants.TestingQuery.equals(type);
	}

	private CiaoPrologTestingQuery(ProgramFileInfo programFileInfo) throws CiaoPrologConnectorException, FilesAndPathsException {
		super(programFileInfo);

		String testFileName = "restaurant.pl";
		LOG.info("testingQuery ...");
		if (testFileName.equals(programFileInfo.getFileName())) {
			PLVariable[] variables = new PLVariable[6];
			variables[0] = new PLVariable(); // X
			variables[1] = new PLVariable(); // V1
			variables[2] = new PLVariable(); // V2
			variables[3] = new PLVariable(); // V3
			variables[4] = new PLVariable(); // Condition -
												// rfuzzy_var_truth_value
			variables[5] = new PLVariable(); // V - rfuzzy_var_truth_value

			String[] variablesNames = { "X", "V1", "V2", "V3", "Condition", "V" };

			PLTerm[] args_expensive = { variables[0], variables[1] };
			PLStructure query_expensive = new PLStructure("expensive", args_expensive);
			PLTerm[] args_very = { query_expensive, variables[2] };
			PLStructure query_very_expensive = new PLStructure("very", args_very);
			PLTerm[] args_fnot = { query_very_expensive, variables[3] };
			PLStructure query_not_very_expensive = new PLStructure("fnot", args_fnot);

			// PLTerm[] dump_constraints_vars_java_list = {variables[3]};
			// PLList dump_constraints_vars_list = null;
			// try {
			// dump_constraints_vars_list= new
			// PLList(dump_constraints_vars_java_list);
			// } catch (PLException e) {}

			PLTerm[] args_rfuzzy_var_truth_value = { variables[3], variables[4], variables[5] };
			PLStructure query_dump_constraints = new PLStructure("rfuzzy_var_truth_value", args_rfuzzy_var_truth_value);

			PLTerm[] args_conjunction = { query_not_very_expensive, query_dump_constraints };
			PLStructure query = new PLStructure(",", args_conjunction);

			setRealQuery(query, variables, variablesNames);
		} else {
			LOG.info("Please use method overrideRealQuery to set your testing query.");
			// throw new CiaoPrologConnectorException("fileName is not " + testFileName);
		}

	}
	
	public void overrideRealQuery(PLStructure query, PLVariable[] variables, String [] variablesNames) throws CiaoPrologConnectorException {
		setRealQuery(query, variables, variablesNames);
	}

	public static CiaoPrologTestingQuery getInstance(ProgramFileInfo programFileInfo) throws CiaoPrologConnectorException,
			FilesAndPathsException {
		return new CiaoPrologTestingQuery(programFileInfo);
	}

	@Override
	public void adequationOfQueryAnswers() {
	}
}
