package com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.client;

/**
 * Implement this interface to deal with messages arriving at the end point
 * where the {@link ClientMessageListener} listens at.
 * 
 * @author Max Wielsch
 * 
 */
public interface ClientMessageHandler {

	/**
	 * Handle the message for new auction round.
	 * 
	 * @param roundNumber
	 *            The round number of the auction.
	 * @param amount
	 *            The amount of the offered good.
	 * @param price
	 *            The price for the amount of the good.
	 */
	public void handleNewAuctionRound(int roundNumber, int amount, double price);

	/**
	 * Handle the message for the end of the actual auction round.
	 * 
	 * @param winner
	 *            The auction agent that won the round.
	 */
	public void handleEndAuctionRound(String winner);

	/**
	 * Handle the message for end of complete auction.
	 */
	public void handleAuctionEnd();

}
