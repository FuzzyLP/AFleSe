package authProviders;

import java.util.ArrayList;

import auxiliar.NextStep;

public class AuthenticationResult {

	private NextStep nextStep;
	private ArrayList<String> messages;

	public AuthenticationResult() {
		this.nextStep = null;
		this.messages = new ArrayList<String>();
	}

	public void setNextStep(NextStep nextStep) {
		this.nextStep = nextStep;
	}

	public void addMessage(String message) {
		this.messages.add(message);
	}

	public NextStep getNextStep() {
		return this.nextStep;
	}

	public String[] getMessages() {
		return this.messages.toArray(new String[0]);
	}
}
