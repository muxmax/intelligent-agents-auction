package com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.server;

import java.io.IOException;
import java.net.Socket;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.client.ClientMessageSender;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.exceptions.InvalidJsonMessageException;

public class ServerMessageConnection {

	private Socket socket;
	private ServerMessageListener messageListener;
	private ServerMessageSender messageSender;

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
	public ServerMessageConnection(Socket socket,
			ServerMessageHandler messageHandler) throws IOException,
			InvalidJsonMessageException {
		this.socket = socket;
		messageSender = new ServerMessageSender(socket.getOutputStream());
		messageListener = new ServerMessageListener(socket.getInputStream(),
				messageHandler, String.valueOf(socket.getPort()));
		messageListener.start();
	}

	/**
	 * Get the message sender, to communicate with a client tcp end point.
	 * 
	 * @return A {@link ClientMessageSender} object.
	 */
	public ServerMessageSender getMessageSender() {
		return messageSender;
	}

	/**
	 * Get the port of the connected client.
	 * 
	 * @return A port number of the client.
	 */
	public int getConnectionPort() {
		return socket.getPort();
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
