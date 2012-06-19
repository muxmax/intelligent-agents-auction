package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.model;

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
