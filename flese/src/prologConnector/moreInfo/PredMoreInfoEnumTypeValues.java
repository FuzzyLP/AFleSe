package prologConnector.moreInfo;

import java.util.ArrayList;

import prologConnector.CiaoPrologTermInJava;

public class PredMoreInfoEnumTypeValues extends PredMoreInfoAbstract {

	public PredMoreInfoEnumTypeValues() {
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

	public String[] getValuesFor(String database) {
		if ((database == null) || ("".equals(database)))
			return new String[0];

		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < moreInfo.length; i++) {
			if ((moreInfo[i][0] != null) && (database.equals(moreInfo[i][0]))) {
				if ((moreInfo[i][1] != null) && (!"".equals(moreInfo[i][1]))) {
					result.add(moreInfo[i][1]);
				}
			}
		}
		return result.toArray(new String[result.size()]);
	}
}
