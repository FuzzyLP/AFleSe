package prologConnector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import CiaoJava.PLStructure;
import CiaoJava.PLVariable;
import filesAndPaths.ProgramFileInfo;

public abstract class CiaoPrologQueryAbstract implements CiaoPrologQueryInterface {

	final static protected Log LOG = LogFactory.getLog(CiaoPrologQueryAbstract.class);

	public static final class Constants {
		public static final String ChangeWorkingFolderQuery = "ChangeWorkingFolderQuery";
		public static final String NormalQuery = "NormalQuery";
		public static final String ProgramIntrospectionQuery = "ProgramIntrospectionQuery";
		public static final String TestingQuery = "TestingQuery";
	}

	private PLStructure query = null;
	private ProgramFileInfo programFileInfo = null;

	private PLVariable[] variables = null;

	protected String[] variablesNames = null;
	private CiaoPrologQueryAnswer[] queryAnswers = null;
	protected boolean isProgramIntrospectionQuery = false;

	protected CiaoPrologQueryAbstract(ProgramFileInfo programFileInfo) throws CiaoPrologConnectorException {

		if (programFileInfo == null)
			throw new CiaoPrologConnectorException("programFileInfo cannot be null.");

		this.programFileInfo = programFileInfo;

		this.queryAnswers = new CiaoPrologQueryAnswer[0];
	}

	protected void setRealQuery(PLStructure query, PLVariable[] variables, String[] variablesNames)
			throws CiaoPrologConnectorException {
		if (query == null)
			throw new CiaoPrologConnectorException("query cannot be null.");
		if (variables == null)
			throw new CiaoPrologConnectorException("variables cannot be null.");
		if (variablesNames == null)
			throw new CiaoPrologConnectorException("variablesNames cannot be null.");

		for (int i = 0; i < variables.length; i++) {
			if (variables[i] == null) {
				throw new CiaoPrologConnectorException("variables[" + i + "] is null.");
			}
		}

		for (int i = 0; i < variablesNames.length; i++) {
			if (variablesNames[i] == null) {
				throw new CiaoPrologConnectorException("variablesNames[" + i + "] is null.");
			}
		}

		if (variables.length != variablesNames.length) {
			throw new CiaoPrologConnectorException("variables and variablesNames have different length.");
		}

		this.query = query;
		this.variables = variables;
		this.variablesNames = variablesNames;

		LOG.info(query.toString());
	}

	public PLStructure getQuery() throws CiaoPrologConnectorException {
		if (this.query == null)
			throw new CiaoPrologConnectorException("query cannot be null.");
		return query;
	}

	public ProgramFileInfo getProgramFileInfo() {
		return programFileInfo;
	}

	public PLVariable[] getVariables() {
		return variables;
	}

	public String[] getVariablesNames() {
		return variablesNames;
	}

	public int getVariablesLength() {
		return variablesNames.length;
	}

	public void setQueryAnswers(CiaoPrologQueryAnswer[] ciaoPrologQueryAnswers) {
		this.queryAnswers = ciaoPrologQueryAnswers;
		adequationOfQueryAnswers();
	}

	public CiaoPrologQueryAnswer[] getQueryAnswers() {
		return this.queryAnswers;
	}

	public String toString() {
		return this.query.toString();
	}

}
