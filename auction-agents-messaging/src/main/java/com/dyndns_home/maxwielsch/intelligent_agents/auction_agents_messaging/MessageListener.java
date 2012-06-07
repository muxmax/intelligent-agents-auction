package com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class handles the incoming communication from another TCP end point. The
 * aim of this class is only to handle messages and not to control the
 * connection. That is to say the connection should be maintained outside this
 * class.
 * 
 * @author Max Wielsch
 * 
 */
public class MessageListener {

	private char MESSAGE_DELIMITER;

	private BufferedInputStream in;
	private MessageHandler messageHandler;
	private boolean listen;

	/**
	 * Expects a socket whose connection is alive.
	 * 
	 * @param input
	 *            An input stream to listen to.
	 * @param messageHandler
	 *            An hander implementing the interface {@link MessageHandler}.
	 *            It is called when a message arrives at this end point.
	 */
	public MessageListener(InputStream input, MessageHandler messageHandler) {

			in = new BufferedInputStream(input);
			this.messageHandler = messageHandler;
			listen = true;
	}

	/**
	 * Expects a socket whose connection is alive.
	 * 
	 * @param input
	 *            An input stream to listen to.
	 * @param messageHandler
	 *            An hander implementing the interface {@link MessageHandler}.
	 *            It is called when a message arrives at this end point.
	 * @param messageDelimiter
	 *            a token that will be appended after all messages to send to
	 *            signal the end of the message. As it has a special meaning and
	 *            must be unique it mustn't be used in the message. The default
	 *            delimiter is the char %.
	 */
	public MessageListener(InputStream input, MessageHandler messageHandler,
			char messageDelimiter) {
		this(input, messageHandler);
		MESSAGE_DELIMITER = messageDelimiter;
	}

	public void startListenting() {
		while (listen) {
			try {
				listen();
			} catch (IOException e) {
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
	 * Listen to the inputStream and handle the the message. When the
	 * message was read it is given to the message handler to decide how to deal
	 * with this message.
	 * 
	 * @throws IOException
	 *             Will be thrown when an error occurs with the socket
	 *             connection.
	 */
	private void listen() throws IOException {

		String message = getNextIncomingMessage();
		messageHandler.handle(message);
	}

	/**
	 * Read the next message that is transmitted via the inputStream. As
	 * long as no message is there this method waits for it.
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
			buffer.append(((char) charackter));
		}
		return buffer.toString();
	}

}
