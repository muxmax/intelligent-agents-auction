package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent;

import java.io.IOException;

public class AuctionManagerStarter {

	public static void main(String[] args) {
		try {
			new AuctionManager(50000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}
}
