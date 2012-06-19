package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.model;

public class Offer {

	public int auctionRound;
	public String offerand;
	public double price;
	public Good good;

	public Offer(Good good) {
		this.good = good;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (offerand != null) {
			sb.append("++++++++++++++++++++++++++++++++++++++\n");
			sb.append("auction round: " + auctionRound);
			sb.append("\nofferand: " + offerand);
			sb.append("\nprice: " + price + "\n");
			sb.append("---- related good ----\n");
			sb.append(good.toString());
			sb.append("++++++++++++++++++++++++++++++++++++++\n");
		} else {
			sb.append("++++++++++++++++++++++++++++++++++++++\n");
			sb.append("No offer for auction round " + auctionRound);
			sb.append("++++++++++++++++++++++++++++++++++++++\n");
		}
		return sb.toString();
	}
}