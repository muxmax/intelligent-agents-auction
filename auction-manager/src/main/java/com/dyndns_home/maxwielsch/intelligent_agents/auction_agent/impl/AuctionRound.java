package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.impl;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.AuctionRoundHandler;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.model.AuctionSettings;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.model.Offer;

public class AuctionRound implements AuctionRoundHandler {

	private AuctionSettings settings;
	private NetworkController networkController;
	private Offer offer;
	private int participantsOfferRecived;

	public AuctionRound(AuctionSettings settings,
			NetworkController networkController) {
		this.settings = settings;
		this.networkController = networkController;
		
		networkController.setAuctionRoundHandler(this);
		
		offer = new Offer(settings.goods
				.get(settings.actualRound - 1));
		
		participantsOfferRecived = 0;

		networkController.sendNewRoundMessage(settings.actualRound,
				offer.good.amount, offer.good.price);
	}

	@Override
	public void handleParticipation(int round, double offer,
			String participantID) {

		
	}

	@Override
	public void handlePausing(String participantID) {
		// TODO Auto-generated method stub

	}

	@Override
	public Offer getResult() {
		return offer;
	}

}
