package authProviders;

import auxiliar.FleSeException;

public class AuthProviderException extends FleSeException {

	private static final long serialVersionUID = 1L;

	public AuthProviderException(String reason) {
		super(reason);
	}
}
