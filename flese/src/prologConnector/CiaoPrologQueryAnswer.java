package prologConnector;

import java.util.HashMap;

public class CiaoPrologQueryAnswer {

	private HashMap<String, CiaoPrologTermInJava> varsAnswers = null;
	public CiaoPrologQueryAnswer() {
		varsAnswers = new HashMap<String, CiaoPrologTermInJava>();
	}
	
	public void addCiaoPrologVariableAnswer(String variableName, CiaoPrologTermInJava variableAnswer) throws CiaoPrologConnectorException {
		if (variableName == null) {
			throw new CiaoPrologConnectorException("variableName cannot be null");
		}
		if ("".equals(variableName)) {
			throw new CiaoPrologConnectorException("variableName cannot be empty string.");
		}
		if (variableAnswer == null) {
			throw new CiaoPrologConnectorException("variableAnswer cannot be null");
		}
		
		this.varsAnswers.put(variableName, variableAnswer);
	}

	public CiaoPrologTermInJava getCiaoPrologQueryVariableAnswer(String variableName) throws CiaoPrologConnectorException {
		if (variableName == null) {
			throw new CiaoPrologConnectorException("variableName cannot be null");
		}
		if ("".equals(variableName)) {
			throw new CiaoPrologConnectorException("variableName cannot be empty string.");
		}

		return this.varsAnswers.get(variableName);
	}
	
	public String [] getCiaoPrologQueryAnswerVariablesNames() {
		return varsAnswers.keySet().toArray(new String[varsAnswers.keySet().size()]);
	}
}
