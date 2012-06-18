package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent;

public interface AuctionHandler {

	public void participantsComplete();
	
	public void handleParticipation(int round, double offer, String participantID);
	
	public void handlePausing(String participantID);
}
