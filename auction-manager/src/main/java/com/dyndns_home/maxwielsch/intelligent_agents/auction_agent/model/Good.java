package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.model;

public class Good {

	public int amount;
	/**
	 * price in cents
	 */
	public long price;

	public Good(int amount, long price) {
		this.amount = amount;
		this.price = price;
	}

	@Override
	public String toString() {
		return "(amount: " + amount + ", price: " + price / 100.00 + ")\n";
	}
}
