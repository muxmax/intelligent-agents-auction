package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AuctionAgentStarter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new AuctionAgent(InetAddress.getLocalHost(), 50000).start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
