package com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.client.ClientMessageHandler;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.exceptions.InvalidJsonMessageException;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.util.MessageType;

/**
 * This class handles the incoming communication from another TCP end point. The
 * aim of this class is only to handle messages and not to control the
 * connection. That is to say the connection should be maintained outside this
 * class.
 * 
 * @author Max Wielsch
 * 
 */
public class ServerMessageListener extends Thread {

	private char MESSAGE_DELIMITER = '%';

	private BufferedInputStream in;
	private ServerMessageHandler messageHandler;
	private final String clientID;
	private boolean listen;

	/**
	 * Expects a socket whose connection is alive.
	 * 
	 * @param input
	 *            An input stream to listen to.
	 * @param messageHandler
	 *            An hander implementing the interface
	 *            {@link ClientMessageHandler}. It is called when a message
	 *            arrives at this end point.
	 * @param clientID
	 *            An unique value to identify a client connection. This could be
	 *            the client port.
	 */
	public ServerMessageListener(InputStream input,
			ServerMessageHandler messageHandler, String clientID) {

		in = new BufferedInputStream(input);
		this.messageHandler = messageHandler;
		this.clientID = clientID;
		listen = true;
	}

	/**
	 * Expects a socket whose connection is alive.
	 * 
	 * @param input
	 *            An input stream to listen to.
	 * @param messageHandler
	 *            An hander implementing the interface
	 *            {@link ClientMessageHandler}. It is called when a message
	 *            arrives at this end point.
	 * @param messageDelimiter
	 *            a token that will be appended after all messages to send to
	 *            signal the end of the message. As it has a special meaning and
	 *            must be unique it mustn't be used in the message. The default
	 *            delimiter is the char %.
	 * @param clientID
	 *            An unique value to identify a client connection. This could be
	 *            the client port.
	 */
	public ServerMessageListener(InputStream input,
			ServerMessageHandler messageHandler, char messageDelimiter,
			String clientID) {
		this(input, messageHandler, clientID);
		MESSAGE_DELIMITER = messageDelimiter;
	}

	/**
	 * Begin listening to the auction managers messages.
	 * 
	 * @throws InvalidJsonMessageException
	 *             Will be thrown when the communication partner sends an
	 *             invalid JSON message. This doesn't necessarily mean that the
	 *             message is not JSOn message. It only doesn't have the
	 *             required key value pairs.
	 */
	@Override
	public void run() {
		while (listen) {
			try {
				listen();
			} catch (IOException e) {
				e.printStackTrace();
				listen = false;
			} catch (InvalidJsonMessageException e) {
				e.printStackTrace();
				listen = false;
			}
		}
	}

	/**
	 * Stop the MessageListener listening to the input stream.
	 */
	public void stopListening() {
		listen = false;
	}

	/**
	 * Listen to the inputStream and handle the the message. When the message
	 * was read it is given to the message handler to decide how to deal with
	 * this message.
	 * 
	 * @throws IOException
	 *             Will be thrown when an error occurs with the socket
	 *             connection.
	 * @throws InvalidJsonMessageException
	 *             Will be thrown when the communication partner sends an
	 *             invalid JSON message. This doesn't necessarily mean that the
	 *             message is not JSOn message. It only doesn't have the
	 *             required key value pairs.
	 */
	private void listen() throws IOException, InvalidJsonMessageException {

		String message = getNextIncomingMessage();

		try {
			JSONObject jsonObject = new JSONObject(message);
//			System.out.println("####");
//			System.out.println(jsonObject.toString());
//			System.out.println("####");
			switch (MessageType.valueOf(jsonObject.getString("action"))) {
			case PARTICIPATE:
				handleParticipation(jsonObject);
				break;
			case PAUSE:
				handlePausing(jsonObject);
				break;
			default:
				throw new JSONException(
						"JSON message couldn't be handled due to bad format.");
			}
		} catch (JSONException e) {
			throw new InvalidJsonMessageException(
					"The Client send an invalid JSON message!", e);
		}
	}

	/**
	 * Call the message handler and tell it the message parameters.
	 * 
	 * @param jsonObject
	 *            The message object that could be parsed out of the JSON
	 *            message.
	 * @throws JSONException
	 *             Will be thrown when the communication partner sends an
	 *             invalid JSON message. This doesn't necessarily mean that the
	 *             message is not JSOn message. It only doesn't have the
	 *             required key value pairs.
	 */
	private void handlePausing(JSONObject jsonObject) throws JSONException {
		int round = jsonObject.getInt("round");
		messageHandler.handlePausing(round, clientID);
	}

	/**
	 * Call the message handler and tell it the message parameters.
	 * 
	 * @param jsonObject
	 *            The message object that could be parsed out of the JSON
	 *            message.
	 * @throws JSONException
	 *             Will be thrown when the communication partner sends an
	 *             invalid JSON message. This doesn't necessarily mean that the
	 *             message is not JSOn message. It only doesn't have the
	 *             required key value pairs.
	 */
	private void handleParticipation(JSONObject jsonObject)
			throws JSONException {
		int round = jsonObject.getInt("round");
		long offer = jsonObject.getLong("offer");
		messageHandler.handleParticipation(round, offer, clientID);
	}

	/**
	 * Read the next message that is transmitted via the inputStream. As long as
	 * no message is there this method waits for it.
	 * 
	 * @return The message read from the socket connection.
	 * @throws IOException
	 *             Will be thrown when an error occurs with the socket
	 *             connection.
	 */
	private String getNextIncomingMessage() throws IOException {

		StringBuffer buffer = new StringBuffer();
		char charackter;

		while ((charackter = (char) in.read()) != MESSAGE_DELIMITER) {
			if (charackter == -1 || charackter == 65535) {
				listen = false;
				break;
			}
			buffer.append(((char) charackter));
		}
		return buffer.toString();
	}

}
