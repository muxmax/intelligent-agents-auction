package com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.client;

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
 * This class handles the outgoing communication to another TCP end point.The
 * aim of this class is only to handle messages and not to controll the
 * connection. That is to say the connection should be maintained outside this
 * class.
 * 
 * @author Max Wielsch
 * 
 */
public class ClientMessageSender {

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
	public ClientMessageSender(OutputStream output) throws IOException {
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
	public ClientMessageSender(OutputStream output, char messageDelimiter)
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
	 * send a JSON message that declares to pause the actual round.
	 * 
	 * @param round
	 *            The number of the actual auction round.
	 * @throws IOException
	 *             Will be thrown, if there occurs an error with the socket
	 *             connection. This might happen when the socket was closed and
	 *             a message ought to be sent via the socket.
	 * @throws InvalidJsonMessageException
	 *             Will be thrown when the created message could not be parsed
	 *             into a JSON object.
	 */
	public void sendPauseMessage(int round) throws IOException,
			InvalidJsonMessageException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("round", String.valueOf(round));

		String jsonMessage = AuctionMessageBuilder.build(MessageType.PAUSE,
				parameters);
		sendMessage(jsonMessage);
	}

	/**
	 * Send a JSON message that declares the participation of you with a
	 * concrete offer.
	 * 
	 * @param round
	 *            The number of the actual auction round.
	 * @param offer
	 *            The price that shall be offered to the auction manager in cents.
	 * @throws IOException
	 *             Will be thrown, if there occurs an error with the socket
	 *             connection. This might happen when the socket was closed and
	 *             a message ought to be sent via the socket.
	 * @throws InvalidJsonMessageException
	 *             Will be thrown when the created message could not be parsed
	 *             into a JSON object.
	 */
	public void sendParticipateMessage(int round, long offer)
			throws IOException, InvalidJsonMessageException {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("round", String.valueOf(round));
		parameters.put("offer", String.valueOf(offer));

		String jsonMessage = AuctionMessageBuilder.build(
				MessageType.PARTICIPATE, parameters);
		sendMessage(jsonMessage);
	}

}
