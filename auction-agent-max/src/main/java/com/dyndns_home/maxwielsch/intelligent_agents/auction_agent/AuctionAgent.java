package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.client.ClientMessageConnection;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.client.ClientMessageHandler;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.exceptions.InvalidJsonMessageException;

public class AuctionAgent implements ClientMessageHandler {

	ClientMessageConnection connection;

	public AuctionAgent(InetAddress managerAddress, int managerPort) {
		try {
			// Starts listening automatically and blocks
			connection = new ClientMessageConnection(managerAddress, managerPort, this);
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
			System.out.println("send offer: " + price);
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
		sendOffer(price + 3);
	}

	@Override
	public void handleEndAuctionRound(String winner) {
		System.out.println("end auction round - winner: " + winner);
	}

	@Override
	public void handleAuctionEnd() {
		System.out.println("finish!");
	}

	@Override
	public void handleLastAcceptedOffer(String participant, double price) {
		if (!participant.equals(connection.getPort())) {
			sendOffer(price + 2);
		}
		
	}

	
}
