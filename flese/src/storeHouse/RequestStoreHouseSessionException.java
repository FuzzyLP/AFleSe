package storeHouse;

import auxiliar.FleSeException;

public class RequestStoreHouseSessionException extends FleSeException {

	private static final long serialVersionUID = 1L;
	public RequestStoreHouseSessionException(String reason) {
		super(reason);
	}
}
