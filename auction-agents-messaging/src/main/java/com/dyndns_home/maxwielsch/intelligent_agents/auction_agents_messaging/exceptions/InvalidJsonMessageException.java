package com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.exceptions;

public class InvalidJsonMessageException extends Exception {

	private static final long serialVersionUID = 3046862898715092991L;

	public InvalidJsonMessageException() {
	}

	public InvalidJsonMessageException(String message) {
		super(message);
	}

	public InvalidJsonMessageException(Throwable cause) {
		super(cause);
	}

	public InvalidJsonMessageException(String message, Throwable cause) {
		super(message, cause);
	}

}
