package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.main;

import java.io.IOException;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.impl.AuctionManager;

public class AuctionManagerStarter {

	public static void main(String[] args) {
		try {
			new AuctionManager(50000);
		} catch (IOException e) {
			System.err.println(e);
			System.exit(-1);
		}
	}
}
