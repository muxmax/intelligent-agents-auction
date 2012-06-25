package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.model;

public class Offer {

	public int auctionRound;
	public String offerand;
	/**
	 * price in cents
	 */
	public long price;
	public Good good;

	public Offer(Good good, int auctionRound) {
		this.good = good;
		this.auctionRound = auctionRound;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (offerand != null) {
			sb.append("++++++++++++++++++++++++++++++++++++++\n");
			sb.append("auction round: " + auctionRound);
			sb.append("\nofferand: " + offerand);
			sb.append("\nprice: " + price / 100.00 + "\n");
			sb.append("---- related good ----\n");
			sb.append(good.toString());
			sb.append("++++++++++++++++++++++++++++++++++++++\n");
		} else {
			sb.append("++++++++++++++++++++++++++++++++++++++\n");
			sb.append("No offer for auction round " + auctionRound + "\n");
			sb.append("++++++++++++++++++++++++++++++++++++++\n");
		}
		return sb.toString();
	}
}
