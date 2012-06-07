package com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging;

/**
 * Implement this interface to deal with messages arriving at the end point
 * where the {@link MessageListener} listens at.
 * 
 * @author Max Wielsch
 * 
 */
public interface MessageHandler {

	/**
	 * This method will be called of the {@link MessageListener} when a new
	 * Message arrives at the end point. To get the message type of this message
	 * you could create a JSON object out if this message and then get the value
	 * of the key "action". This value can be parsed to an object of
	 * {@link MessageType}.
	 * 
	 * @param jsonMessage
	 *            A JSON message having a special form.
	 */
	public void handle(String jsonMessage);

}
