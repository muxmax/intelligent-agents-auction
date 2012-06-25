package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.AuctionHandler;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.AuctionRoundHandler;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.exceptions.InvalidJsonMessageException;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.server.ServerMessageConnection;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.server.ServerMessageHandler;

public class NetworkController extends Thread implements ServerMessageHandler {

	private List<ServerMessageConnection> messageConnections;
	private ServerSocket socket;
	private boolean listen = true;
	private int expectedParticipantAmount;
	private AuctionHandler auctionHandler;
	private AuctionRoundHandler auctionRoundHandler;

	/**
	 * Initialize TCP end points to communicate with auction's manger agent.
	 * 
	 * @param port
	 *            The port of the auction's manger agent.
	 * @param expectedParticipantAmount
	 *            The amount of participants that are expected to connect to the
	 *            manager.
	 * @param auctionHandler
	 *            The initial {@link AuctionHandler}. The host address of the
	 *            auction's manger agent.
	 */
	public NetworkController(int port, int expectedParticipantAmount,
			AuctionHandler auctionHandler) {
		try {
			socket = new ServerSocket(port);
			this.expectedParticipantAmount = expectedParticipantAmount;
			this.auctionHandler = auctionHandler;
			messageConnections = new ArrayList<ServerMessageConnection>();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		try {
			while (listen) {
				listen();
			}
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidJsonMessageException e) {
			e.printStackTrace();
		}
	}

	private void listen() throws IOException, InvalidJsonMessageException {
		Socket clientSocket = socket.accept();
		ServerMessageConnection connection = new ServerMessageConnection(
				clientSocket, this);
		messageConnections.add(connection);

		if (expectedParticipantAmount == messageConnections.size()) {
			auctionHandler.participantsComplete();
		}
	}

	public synchronized void shutDown() {
		listen = false;
	}

	public void sendNewRoundMessage(int round, int amount, double price) {
		for (ServerMessageConnection connection : messageConnections) {
			try {
				connection.getMessageSender().sendNewRoundMessage(round,
						amount, price);
			} catch (IOException e) {
				System.err.println("Error with socket output stream:\n" + e);
			} catch (InvalidJsonMessageException e) {
				System.err.println("Error with message protocoll:\n" + e);
			}
		}
	}

	public void sendAcceptedOffer(String participantID, double priceAccepted) {
		for (ServerMessageConnection connection : messageConnections) {
			try {
				connection.getMessageSender().sendLastAcceptedOffer(
						participantID, priceAccepted);
			} catch (IOException e) {
				System.err.println("Error with socket output stream:\n" + e);
			} catch (InvalidJsonMessageException e) {
				System.err.println("Error with message protocoll:\n" + e);
			}
		}
	}

	/**
	 * Send a message to participants about finishing actual round.
	 * 
	 * @param winner
	 *            The winner of the actual round.
	 */
	public void sendAuctionRoundEnd(String winner) {
		for (ServerMessageConnection connection : messageConnections) {
			try {
				connection.getMessageSender().sendEndRoundMessage(winner);
			} catch (IOException e) {
				System.err.println("Error with socket output stream:\n" + e);
			} catch (InvalidJsonMessageException e) {
				System.err.println("Error with message protocoll:\n" + e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleParticipation(int round, double offer,
			String participantID) {
		auctionRoundHandler.handleParticipation(round, offer, participantID);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handlePausing(int round, String participantID) {
		auctionRoundHandler.handlePausing(participantID);
	}

	/**
	 * Propagate the decision which offer is the highest one to the auction's
	 * participants.
	 * 
	 * @param participantID
	 *            The participant whose offer is accepted
	 * @param priceAccepted
	 *            The price that was offered.
	 */
	public void propagateDecision(String participantID, double priceAccepted) {
		for (ServerMessageConnection connection : messageConnections) {
			try {
				connection.getMessageSender().sendLastAcceptedOffer(
						participantID, priceAccepted);
			} catch (IOException e) {
				System.err.println("Error with socket output stream:\n" + e);
			} catch (InvalidJsonMessageException e) {
				System.err.println("Error with message protocoll:\n" + e);
			}
		}
	}

	/**
	 * Set the handler for the actual auction round.
	 * 
	 * @param auctionRoundHandler
	 *            A handler for the auction round.
	 */
	public void setAuctionRoundHandler(AuctionRoundHandler auctionRoundHandler) {
		this.auctionRoundHandler = auctionRoundHandler;
	}

}
