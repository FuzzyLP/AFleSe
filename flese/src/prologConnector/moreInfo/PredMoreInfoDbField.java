package prologConnector.moreInfo;

import prologConnector.CiaoPrologTermInJava;

public class PredMoreInfoDbField extends PredMoreInfoAbstract {

	public PredMoreInfoDbField() {
	}

	@Override
	public void setMoreInfo(String predMoreInfoType, CiaoPrologTermInJava predMoreInfoInfo) {
		setType(predMoreInfoType);
		setPredMoreInfoInfo(predMoreInfoInfo);		
	}
	
	private void setPredMoreInfoInfo(CiaoPrologTermInJava predMoreInfoInfo) {
		
	}
}


