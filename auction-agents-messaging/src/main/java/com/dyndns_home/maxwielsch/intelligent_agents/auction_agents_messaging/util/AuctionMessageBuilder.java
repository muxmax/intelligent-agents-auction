package com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.util;

import java.io.IOException;
import java.util.Map;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.exceptions.InvalidJsonMessageException;

public class AuctionMessageBuilder {

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
	 *            <td>AUCTION_END</td>
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
	public static String build(MessageType messageType, Map<String, String> parameters)
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

		return jsonMessageBuilder.toString();
	}
}
