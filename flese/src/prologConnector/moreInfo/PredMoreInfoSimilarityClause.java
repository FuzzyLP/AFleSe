package prologConnector.moreInfo;

import prologConnector.CiaoPrologTermInJava;

public class PredMoreInfoSimilarityClause extends PredMoreInfoAbstract {

	public PredMoreInfoSimilarityClause() {
	}

	@Override
	public void setMoreInfo(String predMoreInfoType, CiaoPrologTermInJava predMoreInfoInfo) {
		setType(predMoreInfoType);
		setPredMoreInfoInfo(predMoreInfoInfo);		
	}
	
	private void setPredMoreInfoInfo(CiaoPrologTermInJava predMoreInfoInfo) {
		
	}
}


