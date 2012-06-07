package com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.exceptions;

public class InvalidParametersException extends Exception {

	private static final long serialVersionUID = 5501813344702928747L;

	public InvalidParametersException() {
	}

	public InvalidParametersException(String message) {
		super(message);
	}

	public InvalidParametersException(Throwable cause) {
		super(cause);
	}

	public InvalidParametersException(String message, Throwable cause) {
		super(message, cause);
	}

}
