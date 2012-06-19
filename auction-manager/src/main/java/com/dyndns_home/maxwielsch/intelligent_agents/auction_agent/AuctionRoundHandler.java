package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.model.Offer;

public interface AuctionRoundHandler {

	public void handleParticipation(int round, double offer,
			String participantID);

	public void handlePausing(String participantID);

	/**
	 * Get the highest price that was offered by a participant.
	 * 
	 * @return An offer that was made in an auction round.
	 */
	public Offer getResult();

}
