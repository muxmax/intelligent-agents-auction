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
	 * Handle the message for last accepted offer.
	 * 
	 * @param participant
	 *            The participant whose offer was accept by the manager.
	 * @param price
	 *            The highest price that was offered.
	 */
	public void handleLastAcceptedOffer(String participant, double price);

	/**
	 * Handle the message for the end of the actual auction round.
	 * 
	 * @param winner
	 *            <ul>
	 *            <li>The auction agent that won the round, if there is a
	 *            winner.</li>
	 *            <li>null, if there is no winner.</li>
	 *            </ul>
	 */
	public void handleEndAuctionRound(String winner);

	/**
	 * Handle the message for end of complete auction.
	 */
	public void handleAuctionEnd();

}
