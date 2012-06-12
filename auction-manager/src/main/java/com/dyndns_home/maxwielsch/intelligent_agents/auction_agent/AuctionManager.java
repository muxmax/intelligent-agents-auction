package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.controllers.NetworkController;

public class AuctionManager {

	private NetworkController networkController;
	
	public AuctionManager(int portToListenTo) throws IOException {
		System.out.println("server running");

		networkController = new NetworkController(portToListenTo);
		networkController.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line = reader.readLine().trim();
		
		while (!line.equals("q")) {
			if (line.equals("n")) {
				networkController.sendNewRoundMessage(1, 33, 44.4);
			}
			line = reader.readLine().trim();
		}
		networkController.shutDown();
		System.out.println("server stopped");
	}
	
	
}
