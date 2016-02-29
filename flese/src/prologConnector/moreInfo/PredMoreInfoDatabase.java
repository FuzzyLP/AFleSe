package prologConnector.moreInfo;

import prologConnector.CiaoPrologTermInJava;
import prologConnector.PredicateInfoException;

public class PredMoreInfoDatabase extends PredMoreInfoAbstract {

	String[] variablesNames = null;

	public PredMoreInfoDatabase() {
		variablesNames = null;
	}

	@Override
	public void setMoreInfo(String predMoreInfoType, CiaoPrologTermInJava predMoreInfoInfo) throws PredicateInfoException {
		setType(predMoreInfoType);
		setPredMoreInfoInfo(predMoreInfoInfo);
	}

	private void setPredMoreInfoInfo(CiaoPrologTermInJava predMoreInfoInfo) throws PredicateInfoException {
		if (predMoreInfoInfo == null) {
			throw new PredicateInfoException("predMoreInfoInfo cannot be null");
		}
		if (!predMoreInfoInfo.isList()) {
			throw new PredicateInfoException("predMoreInfoInfo is not a list");
		}

		int length = predMoreInfoInfo.length();
		variablesNames = new String[length];
		for (int i = 0; i < length; i++) {
			String varName = predMoreInfoInfo.atPosition(i).toString();
			if (varName == null) {
				throw new PredicateInfoException("varName cannot be null");
			}
			if ("".equals(varName)) {
				throw new PredicateInfoException("varName cannot be empty string");
			}
			variablesNames[i] = varName;
		}
	}

	public String[] generateVariablesNames(String initialPredicate) {
		String[] allVariablesNames = new String[variablesNames.length + 1];
		allVariablesNames[0] = initialPredicate;
		for (int i = 0; i < variablesNames.length; i++) {
			allVariablesNames[i+1] = variablesNames[i];
		}
		return allVariablesNames;
	}
}
