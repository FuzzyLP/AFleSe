package prologConnector.moreInfo;

import prologConnector.CiaoPrologTermInJava;
import prologConnector.PredicateInfoException;

public interface PredMoreInfoInterface {

	public void setMoreInfo(String predMoreInfoType, CiaoPrologTermInJava predMoreInfoInfo) throws PredicateInfoException;

	public String getType();

	public String[] generateVariablesNames(String initialPredicate);
	
	public String[][] getMoreInfo();
	
	public String [][] getOperatorsFor(String [][] types);
	
	public String [] getValuesFor(String database);
}
