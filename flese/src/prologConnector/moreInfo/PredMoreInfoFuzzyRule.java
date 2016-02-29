package prologConnector.moreInfo;

import prologConnector.CiaoPrologTermInJava;

public class PredMoreInfoFuzzyRule extends PredMoreInfoAbstract {

	public PredMoreInfoFuzzyRule() {
	}

	@Override
	public void setMoreInfo(String predMoreInfoType, CiaoPrologTermInJava predMoreInfoInfo) {
		setType(predMoreInfoType);
		setPredMoreInfoInfo(predMoreInfoInfo);		
	}
	
	private void setPredMoreInfoInfo(CiaoPrologTermInJava predMoreInfoInfo) {
		
	}
}


