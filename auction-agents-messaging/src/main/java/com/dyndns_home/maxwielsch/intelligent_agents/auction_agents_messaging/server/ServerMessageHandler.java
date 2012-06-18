package com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.server;

/**
 * Implement this interface to deal with messages arriving at the end point
 * where the {@link ServerMessageListener} listens at.
 * 
 * @author Max Wielsch
 * 
 */
public interface ServerMessageHandler {

	/**
	 * Handle the message for participation of a client.
	 * 
	 * @param round
	 *            The auction's round to that the offer relates to.
	 * @param offer
	 *            The price that a client would pay.
	 * @param participantID
	 *            An unique value to identify a participant. This could be the
	 *            client port.
	 */
	public void handleParticipation(int round, double offer,
			String participantID);

	/**
	 * Handle the message for pausing a round.
	 * 
	 * @param round
	 *            The auction's round to that the client wants to pause.
	 * @param participantID
	 *            An unique value to identify a participant. This could be
	 *            the client port.
	 */
	public void handlePausing(int round, String participantID);
}
