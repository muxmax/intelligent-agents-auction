package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.impl;

import java.util.ArrayList;
import java.util.List;

public class AuctionSettings {

	public class Good {

		public int amount;
		public double price;

		public Good(int amount, double price) {
			this.amount = amount;
			this.price = price;
		}

		@Override
		public String toString() {
			return "(amount: " + amount + ", price: " + price + ")\n";
		}
	}

	public int rounds;
	public int participants;
	public List<Good> goods;
	public int port;

	public AuctionSettings() {
		goods = new ArrayList<AuctionSettings.Good>();
		port = 50000;
	}

	@Override
	public String toString() {
		return "auction rounds: " + rounds + ",\n" + "participants: "
				+ participants + ",\n" + goods.toString();
	}
}
