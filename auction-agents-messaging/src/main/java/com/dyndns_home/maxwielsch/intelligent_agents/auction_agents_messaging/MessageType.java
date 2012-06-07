package com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging;

/**
 * Use this enumeration to declare the type of the message to create.
 * 
 */
public enum MessageType {

	LOGON("LOGON"), LOGOFF("LOGOFF"), NEW_ROUND("NEW_ROUND"), PAUSE("PAUSE"), PARTICIPATE(
			"PARTICIPATE"), END_ROUND("END_ROUND");

	private String action;

	MessageType(String action) {
		this.action = action;
	}

	public String getAction() {
		return action;
	}

}
