package com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.exceptions.InvalidJsonMessageException;

/**
 * This class handles the outgoing communication to another TCP end point.The
 * aim of this class is only to handle messages and not to controll the
 * connection. That is to say the connection should be maintained outside this
 * class.
 * 
 * @author Max Wielsch
 * 
 */
public class MessageSender {

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
	public MessageSender(OutputStream output) throws IOException {
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
	public MessageSender(OutputStream output, char messageDelimiter)
			throws IOException {
		this(output);
		MESSAGE_DELIMITER = messageDelimiter;
	}

	/**
	 * Write a message of the given type, that will be send to the receiver
	 * immediately.
	 * 
	 * @param messageType
	 *            The enumeration MessageType specifies the the type of the
	 *            message to be sent.
	 * @param parameters
	 *            The parameters map holds the arguments to create the message
	 *            with. According to a special message type special arguments
	 *            are needed. These arguments have to be put into the map with a
	 *            special key. Which keys for which arguments and messages to
	 *            use, you can see in the table below.
	 *            <table>
	 *            <tr>
	 *            <th>MessageType</th>
	 *            <th>key</th>
	 *            <th>parameter data type</th>
	 *            <th>comment</th>
	 *            </tr>
	 *            <tr>
	 *            <td>LOGON</td>
	 *            <td>none</td>
	 *            <td>none</td>
	 *            <td>No parameters are needed. So the argument can be null.</td>
	 *            </tr>
	 *            <tr>
	 *            <td>LOGOFF</td>
	 *            <td>none</td>
	 *            <td>none</td>
	 *            <td>No parameters are needed. So the argument can be null.</td>
	 *            </tr>
	 *            <tr>
	 *            <td>NEW_ROUND</td>
	 *            <td>round</td>
	 *            <td>int</td>
	 *            <td></td>
	 *            </tr>
	 *            <tr>
	 *            <td></td>
	 *            <td>amount</td>
	 *            <td>int</td>
	 *            <td></td>
	 *            </tr>
	 *            <tr>
	 *            <td></td>
	 *            <td>price</td>
	 *            <td>double</td>
	 *            <td></td>
	 *            </tr>
	 *            <tr>
	 *            <td>PAUSE</td>
	 *            <td>round</td>
	 *            <td>int</td>
	 *            <td></td>
	 *            </tr>
	 *            <tr>
	 *            <td>PARTICIPATE</td>
	 *            <td>round</td>
	 *            <td>int</td>
	 *            <td></td>
	 *            </tr>
	 *            <tr>
	 *            <td></td>
	 *            <td>offer</td>
	 *            <td>double</td>
	 *            <td></td>
	 *            </tr>
	 *            <tr>
	 *            <td>END_ROUND</td>
	 *            <td>winner</td>
	 *            <td>String</td>
	 *            <td>The participant, who won the auction round.</td>
	 *            </tr>
	 *            </table>
	 * @throws InvalidJsonMessageException
	 *             Will be thrown when the created message could not be parsed
	 *             into a JSON object.
	 * @throws IOException
	 *             Will be thrown, if there occurs an error with the socket
	 *             connection. This might happen when the socket was closed and
	 *             a message ought to be sent via the socket.
	 * @see MessageType
	 */
	private void write(MessageType messageType, Map<String, String> parameters)
			throws IOException, InvalidJsonMessageException {
		
		StringBuilder jsonMessageBuilder = new StringBuilder("{action:'"
				+ messageType.getAction() + "'");

		if (parameters != null) {
			for (String key : parameters.keySet()) {
				jsonMessageBuilder.append(",").append(key).append(":'")
						.append(parameters.get(key)).append("'");
			}
		}
		jsonMessageBuilder.append("}");

		sendMessage(jsonMessageBuilder.toString());
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
	 * Create a JSON message telling the auction manager to log on to the
	 * current auction session.
	 * 
	 * @throws IOException
	 *             Will be thrown, if there occurs an error with the socket
	 *             connection. This might happen when the socket was closed and
	 *             a message ought to be sent via the socket.
	 * @throws InvalidJsonMessageException
	 *             Will be thrown when the created message could not be parsed
	 *             into a JSON object.
	 */
	public void sendLogOnMessage() throws IOException,
			InvalidJsonMessageException {
		write(MessageType.LOGON, null);
	}

	/**
	 * Create a JSON message telling the auction manager to log off of the
	 * current auction session.
	 * 
	 * @throws IOException
	 *             Will be thrown, if there occurs an error with the socket
	 *             connection. This might happen when the socket was closed and
	 *             a message ought to be sent via the socket.
	 * @throws InvalidJsonMessageException
	 *             Will be thrown when the created message could not be parsed
	 *             into a JSON object.
	 */
	public void sendLogOffMessage() throws IOException,
			InvalidJsonMessageException {
		write(MessageType.LOGOFF, null);
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
	 *            The price for the given amount of goods.
	 * @throws IOException
	 *             Will be thrown, if there occurs an error with the socket
	 *             connection. This might happen when the socket was closed and
	 *             a message ought to be sent via the socket.
	 * @throws InvalidJsonMessageException
	 *             Will be thrown when the created message could not be parsed
	 *             into a JSON object.
	 */
	public void sendNewRoundMessage(int round, int amount, double price)
			throws IOException, InvalidJsonMessageException {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("round", String.valueOf(round));
		parameters.put("amount", String.valueOf(amount));
		parameters.put("price", String.valueOf(price));

		write(MessageType.NEW_ROUND, parameters);
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

		write(MessageType.END_ROUND, parameters);
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

		write(MessageType.PAUSE, parameters);

	}

	/**
	 * Send a JSON message that declares the participation of you with a
	 * concrete offer.
	 * 
	 * @param round
	 *            The number of the actual auction round.
	 * @param offer
	 *            The price that shall be offered to the auction manager.
	 * @throws IOException
	 *             Will be thrown, if there occurs an error with the socket
	 *             connection. This might happen when the socket was closed and
	 *             a message ought to be sent via the socket.
	 * @throws InvalidJsonMessageException
	 *             Will be thrown when the created message could not be parsed
	 *             into a JSON object.
	 */
	public void sendParticipateMessage(int round, double offer)
			throws IOException, InvalidJsonMessageException {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("round", String.valueOf(round));
		parameters.put("offer", String.valueOf(offer));

		write(MessageType.PARTICIPATE, parameters);
	}

}
