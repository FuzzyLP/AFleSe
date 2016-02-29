package prologConnector.moreInfo;

import java.util.ArrayList;

import constants.KConstants;
import prologConnector.CiaoPrologTermInJava;

public class PredMoreInfoDefinedOperators extends PredMoreInfoAbstract {

	public PredMoreInfoDefinedOperators() {
	}

	@Override
	public void setMoreInfo(String predMoreInfoType, CiaoPrologTermInJava predMoreInfoInfo) {
		setType(predMoreInfoType);
		setPredMoreInfoInfo(predMoreInfoInfo);
	}

	private void setPredMoreInfoInfo(CiaoPrologTermInJava predMoreInfoInfo) {

		if (predMoreInfoInfo == null)
			return;
		if (predMoreInfoInfo.isList()) {
			int length = predMoreInfoInfo.length();
			moreInfo = new String[length][];
			for (int i = 0; i < length; i++) {
				CiaoPrologTermInJava term = predMoreInfoInfo.atPosition(i);
				if ((term != null) && (term.isArray())) {
					int numOperatorFields = term.length();
					moreInfo[i] = new String[numOperatorFields];
					for (int j = 0; j < numOperatorFields; j++) {
						CiaoPrologTermInJava subTerm = term.atPosition(j);
						if (subTerm != null) {
							moreInfo[i][j] = subTerm.toString();
						}
					}
				}
			}
		}
	}

	@Override
	public String[][] getOperatorsFor(String[][] types) {
		ArrayList<String[]> result = new ArrayList<String[]>();

		if ((types == null) || (types[0].length <= 0) || (types[0][types[0].length -1] == null)) {
			return new String[0][];
		}

		for (int i = 0; i < moreInfo.length; i++) {
			boolean valid = false;
			for (int j = 0; j < types.length; j++) {

				valid = testIfOperatorIsOkForType(moreInfo[i], types[j]);
			}

			if (valid) {
				result.add(moreInfo[i]);
			}
		}
		
		return result.toArray(new String [result.size()][]);
	}

	private boolean testIfOperatorIsOkForType(String[] typeOp, String[] typeIn) {

		String typeInType = typeIn[typeIn.length -1];
		String typeOpType = typeOp[typeOp.length -1];
		
		if (KConstants.PrologTypes.rfuzzy_any_type.equals(typeOpType))
			return true;
		if (KConstants.PrologTypes.rfuzzy_any_type.equals(typeInType))
			return true;

		if (KConstants.PrologTypes.rfuzzy_number_type.equals(typeOpType)) {
			if (KConstants.PrologTypes.rfuzzy_number_type.equals(typeInType))
				return true;
			if (KConstants.PrologTypes.rfuzzy_integer_type.equals(typeInType))
				return true;
			if (KConstants.PrologTypes.rfuzzy_float_type.equals(typeInType))
				return true;

		}
		
		if (typeOpType.equals(typeInType)) 
			return true;
		return false;

	}

}
