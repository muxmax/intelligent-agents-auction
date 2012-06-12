package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.client.ClientMessageConnection;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.client.ClientMessageHandler;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.exceptions.InvalidJsonMessageException;

public class AuctionAgent implements ClientMessageHandler {

	Socket managerSocket;
	ClientMessageConnection connection;

	public AuctionAgent(int managerPort) {
		try {
			managerSocket = new Socket(InetAddress.getLocalHost(), managerPort);
			// Starts listening automatically and blocks
			connection = new ClientMessageConnection(managerSocket, this);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("auction manager isn't online!");
		} catch (InvalidJsonMessageException e) {
			System.err.println("auction manager complains about wrong message format!");
		}
	}

	public void sendOffer(double price) {
		try {
			connection.getMessageSender().sendParticipateMessage(1, price);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidJsonMessageException e) {
			System.out.println("JSON Message Error!\n");
			e.printStackTrace();
		}
	}

	@Override
	public void handleNewAuctionRound(int roundNumber, int amount, double price) {
		System.out.println("new auction round:" + roundNumber + " amount, price: " + amount + ", " + price);
	}

	@Override
	public void handleEndAuctionRound(String winner) {
		System.out.println("end auction round - winner: " + winner);
	}

	@Override
	public void handleAuctionEnd() {
		System.out.println("finish!");
	}

	
}
