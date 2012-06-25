package com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.exceptions.InvalidJsonMessageException;

public class ClientMessageConnection {

	private ClientMessageListener messageListener;
	private ClientMessageSender messageSender;
	private int port;
	private Socket socket;

	/**
	 * Construct a message connection using the default port (50000).
	 * 
	 * @see ClientMessageConnection#ClientMessageConnection(InetAddress, int,
	 *      ClientMessageHandler)
	 */
	public ClientMessageConnection(InetAddress address,
			ClientMessageHandler messageHandler) throws IOException,
			InvalidJsonMessageException {
		this(address, 50000, messageHandler);
	}

	/**
	 * Construct a message connection.
	 * 
	 * @param address
	 *            An Internet address where the server (auction manager) runs.
	 * @param port
	 *            A port that the server listens to.
	 * @param messageHandler
	 *            The handler that will be called when a message comes in.
	 * @throws IOException
	 *             Will be thrown when an error occurs while listening.
	 */
	public ClientMessageConnection(InetAddress address, int port,
			ClientMessageHandler messageHandler) throws IOException,
			InvalidJsonMessageException {
		socket = new Socket(address, port);
		this.port = port;
		messageSender = new ClientMessageSender(socket.getOutputStream());
		messageListener = new ClientMessageListener(socket.getInputStream(),
				messageHandler);
	}

	/**
	 * Get the message sender, to communicate with another tcp end point.
	 * 
	 * @return A {@link ClientMessageSender} object.
	 */
	public ClientMessageSender getMessageSender() {
		return messageSender;
	}

	/**
	 * Get the port number of the server connection.
	 * 
	 * @return A port number.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @throws InvalidJsonMessageException
	 *             Will be thrown when the communication partner sends an
	 *             invalid JSON message. This doesn't necessarily mean that the
	 *             message is not JSOn message. It only doesn't have the
	 *             required key value pairs.
	 */
	public void establish() throws InvalidJsonMessageException {
		messageListener.startListenting();
	}

	/**
	 * Checks if a string that stands for a partipiciant is the name of this
	 * agent.
	 * 
	 * @param client
	 *            the name of a client (e.g. the participant argument of the
	 *            {@link ClientMessageHandler#handleLastAcceptedOffer(String, long)}
	 *            or the winner argument of
	 *            {@link ClientMessageHandler#handleEndAuctionRound(String)}.
	 * @return
	 */
	public boolean isMe(String client) {
		return ("" + socket.getLocalPort()).equals(client);
	}
}
