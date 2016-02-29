package prologConnector;

import CiaoJava.PLStructure;
import CiaoJava.PLVariable;
import filesAndPaths.ProgramFileInfo;

public interface CiaoPrologQueryInterface {

	public boolean isOfType(String type);
	
	public PLStructure getQuery() throws CiaoPrologConnectorException;

	public ProgramFileInfo getProgramFileInfo();

	public PLVariable[] getVariables();

	public String[] getVariablesNames();

	public int getVariablesLength();

	public void setQueryAnswers(CiaoPrologQueryAnswer [] ciaoPrologQueryAnswers);
	
	public void adequationOfQueryAnswers();
	
	public CiaoPrologQueryAnswer[] getQueryAnswers();

}
