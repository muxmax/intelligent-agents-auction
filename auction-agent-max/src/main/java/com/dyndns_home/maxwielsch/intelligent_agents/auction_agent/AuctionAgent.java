package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.MessageConnection;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.MessageHandler;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.exceptions.InvalidJsonMessageException;

public class AuctionAgent implements MessageHandler {

	Socket managerSocket;
	MessageConnection connection;

	public AuctionAgent(int managerPort) {
		try {
			managerSocket = new Socket(InetAddress.getLocalHost(), managerPort);
			connection = new MessageConnection(managerSocket, this);
			connection.getMessageSender().sendLogOnMessage();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("auction manager isn't online!");
		} catch (InvalidJsonMessageException e) {
			System.out.println("JSON Message Error!\n");
			e.printStackTrace();
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
	public void handle(String jsonMessage) {
		System.out.println("Incomming from auction manager:\n" + jsonMessage);
	}
}
