package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.model;

import java.util.ArrayList;
import java.util.List;


public class AuctionSettings {

	public int rounds;
	public int actualRound;
	public int participants;
	public List<Good> goods;
	public int port;

	public AuctionSettings() {
		goods = new ArrayList<Good>();
		port = 50000;
		actualRound = 0;
	}

	@Override
	public String toString() {
		return "auction rounds: " + rounds + ",\n" + "participants: "
				+ participants + ",\n" + goods.toString();
	}
}
