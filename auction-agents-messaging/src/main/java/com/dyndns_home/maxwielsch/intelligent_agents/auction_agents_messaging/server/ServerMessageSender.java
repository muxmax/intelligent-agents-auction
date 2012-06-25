package com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.exceptions.InvalidJsonMessageException;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.util.AuctionMessageBuilder;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.util.MessageType;

/**
 * This class handles the outgoing communication to a client TCP end point.The
 * aim of this class is only to handle messages and not to controll the
 * connection. That is to say the connection should be maintained outside this
 * class.
 * 
 * @author Max Wielsch
 * 
 */
public class ServerMessageSender {

	private OutputStreamWriter out;
	private char MESSAGE_DELIMITER = '%';

	/**
	 * Expects a socket whose connection is alive.
	 * 
	 * @param output
	 *            An output stream to send messages over.
	 * @throws IOException
	 *             Will be thrown when the socket connection is closed or the
	 *             output stream can't be constructed from the socket.
	 */
	public ServerMessageSender(OutputStream output) throws IOException {
		out = new OutputStreamWriter(output);
	}

	/**
	 * Expects a socket whose connection is alive.
	 * 
	 * @param output
	 *            An output stream to send messages over.
	 * @param messageDelimiter
	 *            a token that will be appended after all messages to send to
	 *            signal the end of the message. As it has a special meaning and
	 *            must be unique it mustn't be used in the message. The default
	 *            delimiter is the char %.
	 * @throws IOException
	 *             Will be thrown when the socket connection is closed or the
	 *             output stream can't be constructed from the socket.
	 */
	public ServerMessageSender(OutputStream output, char messageDelimiter)
			throws IOException {
		this(output);
		MESSAGE_DELIMITER = messageDelimiter;
	}

	/**
	 * Send a valid JSON message via the socket.
	 * 
	 * @param jsonMessage
	 *            A message that has to be parsable into a valid JSON object.
	 * @throws InvalidJsonMessageException
	 *             Will be thrown when the created message could not be parsed
	 *             into a JSON object.
	 * @throws IOException
	 *             Will be thrown, if there occurs an error with the socket
	 *             connection. This might happen when the socket was closed and
	 *             a message ought to be sent via the socket.
	 */
	private void sendMessage(String jsonMessage)
			throws InvalidJsonMessageException, IOException {

		try {
			JSONObject.testValidity(new JSONObject(jsonMessage));
			out.write(jsonMessage + MESSAGE_DELIMITER);
			out.flush();
		} catch (JSONException e) {
			throw new InvalidJsonMessageException(jsonMessage);
		}
	}

	/**
	 * Create a JSON message declaring the offer conditions of a new auction
	 * round.
	 * 
	 * @param round
	 *            The number of the actual auction round.
	 * @param amount
	 *            The amount in pieces of a some good to be offered.
	 * @param price
	 *            The price for the given amount of goods in cents.
	 * @throws IOException
	 *             Will be thrown, if there occurs an error with the socket
	 *             connection. This might happen when the socket was closed and
	 *             a message ought to be sent via the socket.
	 * @throws InvalidJsonMessageException
	 *             Will be thrown when the created message could not be parsed
	 *             into a JSON object.
	 */
	public void sendNewRoundMessage(int round, int amount, long price)
			throws IOException, InvalidJsonMessageException {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("round", String.valueOf(round));
		parameters.put("amount", String.valueOf(amount));
		parameters.put("price", String.valueOf(price));

		String jsonMessage = AuctionMessageBuilder.build(MessageType.NEW_ROUND,
				parameters);
		sendMessage(jsonMessage);
	}

	/**
	 * Create a JSON message to announce the highest offer and the participant
	 * that is accepted.
	 * 
	 * @param clientID
	 *            The participant's name (must be unique) who made the highest
	 *            offer.
	 * @param priceAccepted
	 *            The price that is accepted by the manager in cents.
	 * @throws IOException
	 *             Will be thrown, if there occurs an error with the socket
	 *             connection. This might happen when the socket was closed and
	 *             a message ought to be sent via the socket.
	 * @throws InvalidJsonMessageException
	 *             Will be thrown when the created message could not be parsed
	 *             into a JSON object.
	 */
	public void sendLastAcceptedOffer(String clientID, long priceAccepted)
			throws IOException, InvalidJsonMessageException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("participant", clientID);
		parameters.put("price", String.valueOf(priceAccepted));
		String jsonMessage = AuctionMessageBuilder.build(
				MessageType.ACCEPT_OFFER, parameters);
		sendMessage(jsonMessage);
	}

	/**
	 * Create a JSON message that tells the winning participant.
	 * 
	 * @param winner
	 *            The participant's name who won the auction.
	 * @throws IOException
	 *             Will be thrown, if there occurs an error with the socket
	 *             connection. This might happen when the socket was closed and
	 *             a message ought to be sent via the socket.
	 * @throws InvalidJsonMessageException
	 *             Will be thrown when the created message could not be parsed
	 *             into a JSON object.
	 */
	public void sendEndRoundMessage(String winner) throws IOException,
			InvalidJsonMessageException {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("winner", winner);

		String jsonMessage = AuctionMessageBuilder.build(MessageType.END_ROUND,
				parameters);
		sendMessage(jsonMessage);
	}

	/**
	 * Create a JSON message telling the auction clients the end of the complete
	 * auction.
	 * 
	 * @throws IOException
	 *             Will be thrown, if there occurs an error with the socket
	 *             connection. This might happen when the socket was closed and
	 *             a message ought to be sent via the socket.
	 * @throws InvalidJsonMessageException
	 *             Will be thrown when the created message could not be parsed
	 *             into a JSON object.
	 */
	public void sendAuctionEndMessage() throws IOException,
			InvalidJsonMessageException {
		String jsonMessage = AuctionMessageBuilder.build(
				MessageType.END_AUCTION, null);
		sendMessage(jsonMessage);
	}

}
