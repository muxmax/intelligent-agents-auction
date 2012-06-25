package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.model.Offer;

public interface AuctionRoundHandler {

	/**
	 * Handle the participation respective the offer of a participant.
	 * @param round The round to which the offer relates to.
	 * @param offer 
	 * @param participantID
	 */
	public void handleParticipation(int round, long offer, String participantID);

	/**
	 * Handle pausing of a participant.
	 * 
	 * @param participantID
	 *            The ID of the pausing participant.
	 */
	public void handlePausing(String participantID);

	/**
	 * Get the highest price that was offered by a participant.
	 * 
	 * @return An offer that was made in an auction round.
	 */
	public Offer getResult();

}
