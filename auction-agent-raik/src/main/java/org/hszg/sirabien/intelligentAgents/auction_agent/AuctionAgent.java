package org.hszg.sirabien.intelligentAgents.auction_agent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.client.ClientMessageConnection;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.client.ClientMessageHandler;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.exceptions.InvalidJsonMessageException;

public class AuctionAgent implements ClientMessageHandler {
	
	/**
	 * Time of one round in milliseconds.
	 */
	private static final int ROUND_TIME = 1 * 60 * 1000;
	private static final long START_MONEY = 100000;
	
	private ClientMessageConnection connection;
	private InetAddress server;
	private long currentMoney;

	private AuctionAgent(String[] args) {
		currentMoney = START_MONEY;
		handleArgs(args);
	}

	private void handleArgs(String[] args) {
		if(args.length != 1)
			printHelpAndExit();
		try {
			server = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e) {
			System.err.println("You've entered an invalid server address.");
			printHelpAndExit();
		}
			
	}

	private void printHelpAndExit() {
		System.out.println("Usage: java -jar auctionAgent.jar <Server-Address>");
		System.exit(-1);
	}

	private void start() {
		try {
			connection = new ClientMessageConnection(server, this);
			connection.establish();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidJsonMessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void handleNewAuctionRound(int roundNumber, int amount, long price) {
		// TODO Auto-generated method stub
		System.out.println("new auction round");
	}

	@Override
	public void handleLastAcceptedOffer(String participant, long price) {
		// TODO Auto-generated method stub
		System.out.println("last accepted offer");
	}

	@Override
	public void handleEndAuctionRound(String winner) {
		// TODO Auto-generated method stub
		System.out.println("end auction round");
	}

	@Override
	public void handleAuctionEnd() {
		// TODO Auto-generated method stub
		System.out.println("end auction");
		
	}
	
	public static void main(String[] args) {
		new AuctionAgent(args).start();
	}
}
