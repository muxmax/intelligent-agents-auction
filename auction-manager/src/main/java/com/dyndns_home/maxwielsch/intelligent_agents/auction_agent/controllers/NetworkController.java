package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.controllers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.exceptions.InvalidJsonMessageException;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.server.ServerMessageConnection;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.server.ServerMessageHandler;

public class NetworkController extends Thread implements ServerMessageHandler {

	private List<ServerMessageConnection> messageConnections;
	private ServerSocket socket;
	private boolean listen = true;

	/**
	 * Initialize TCP end points to communicate with auction's manger agent.
	 * 
	 * @param host
	 *            The host address of the auction's manger agent.
	 * @param port
	 *            The port of the auction's manger agent.
	 */
	public NetworkController(int port) {
		try {
			socket = new ServerSocket(port);
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
		ServerMessageConnection connection = new ServerMessageConnection(clientSocket, this);
		messageConnections.add(connection);
	}

	public synchronized void shutDown() {
		listen = false;
	}
	
	public synchronized void sendNewRoundMessage(int round, int amount, double price) {
		for (ServerMessageConnection con : messageConnections) {
			try {
				con.getMessageSender().sendNewRoundMessage(round, amount, price);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidJsonMessageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void handleParticipation(int round, double offer, String clientID) {
		
		
	}

	@Override
	public void handlePausing(int round, String clientID) {
		System.out.println("client " + clientID + " pauses this round");
	}
	
	public void propagateDecision(String clientID, double priceAccepted) {
		for (ServerMessageConnection connection : messageConnections) {
			try {
				connection.getMessageSender().sendLastAcceptedOffer(clientID, priceAccepted);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidJsonMessageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
