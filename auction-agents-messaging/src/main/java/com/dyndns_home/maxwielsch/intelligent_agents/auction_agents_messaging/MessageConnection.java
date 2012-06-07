package com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging;

import java.io.IOException;
import java.net.Socket;

public class MessageConnection {

	private Socket socket;
	private MessageListener messageListener;
	private MessageSender messageSender;

	/**
	 * Construct a message connection.
	 * 
	 * @param socket
	 *            A socket that is already open.
	 * @param messageHandler
	 *            The handler that will be called when a message comes in.
	 * @throws IOException
	 *             Will be thrown when an error occurs while listening.
	 */
	public MessageConnection(Socket socket, MessageHandler messageHandler)
			throws IOException {
		this.socket = socket;
		messageSender = new MessageSender(socket.getOutputStream());
		messageListener = new MessageListener(socket.getInputStream(),
				messageHandler);
		messageListener.startListenting();
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
