package com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.client;

import java.io.IOException;
import java.net.Socket;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.exceptions.InvalidJsonMessageException;

public class ClientMessageConnection {

	private Socket socket;
	private ClientMessageListener messageListener;
	private ClientMessageSender messageSender;

	/**
	 * Construct a message connection.
	 * 
	 * @param socket
	 *            A socket that is already open.
	 * @param messageHandler
	 *            The handler that will be called when a message comes in.
	 * @throws IOException
	 *             Will be thrown when an error occurs while listening.
	 * @throws InvalidJsonMessageException
	 *             Will be thrown when the communication partner sends an
	 *             invalid JSON message. This doesn't necessarily mean that the
	 *             message is not JSOn message. It only doesn't have the
	 *             required key value pairs.
	 */
	public ClientMessageConnection(Socket socket,
			ClientMessageHandler messageHandler) throws IOException,
			InvalidJsonMessageException {
		this.socket = socket;
		messageSender = new ClientMessageSender(socket.getOutputStream());
		messageListener = new ClientMessageListener(socket.getInputStream(),
				messageHandler);
		messageListener.startListenting();
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
	 * Call to stop listening to this connection and terminate the socket
	 * connection.
	 */
	public void close() {
		try {
			messageListener.stopListening();
			socket.close();
		} catch (IOException e) {
			// Connection seems to be closed already.
			e.printStackTrace();
		}
	}
}
