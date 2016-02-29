package prologConnector;

import CiaoJava.PLConnection;
import CiaoJava.PLGoal;
import auxiliar.LocalUserInfo;

public class PlConnectionEnvelopeData {

	private LocalUserInfo localUserInfo;
	private PLGoal evaluatedGoal = null;
	private PLConnection plConnection = null;

	public PlConnectionEnvelopeData(LocalUserInfo localUserInfo) {
		this.localUserInfo = localUserInfo;
		this.evaluatedGoal = null;
		this.plConnection = null;
	}
	
	public LocalUserInfo getLocalUserInfo() {
		return localUserInfo;
	}
	
	public void setEvaluatedGoal(PLGoal evaluatedGoal) {
		this.evaluatedGoal = evaluatedGoal;
	}

	public PLGoal getEvaluatedGoal() {
		return evaluatedGoal;
	}

	public void setPlConnection(PLConnection plConnection) {
		this.plConnection = plConnection;
	}

	public PLConnection getPlConnection() {
		return plConnection;
	}
}
