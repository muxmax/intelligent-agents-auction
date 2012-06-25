package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.AuctionRoundHandler;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.model.AuctionSettings;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.model.Offer;

public class AuctionRound implements AuctionRoundHandler {

	private AuctionSettings settings;
	private NetworkController networkController;
	private Offer acceptedOffer;
	private Map<String, Double> participantsOfferRecived;
	private List<String> pausingParticipants;
	private int offersRecived;

	/**
	 * Initialize a new auction round.
	 * 
	 * @param settings
	 *            The global settings collected at setup from user.
	 * @param networkController
	 *            The controller that deals with the message connections.
	 */
	public AuctionRound(AuctionSettings settings,
			NetworkController networkController) {
		this.settings = settings;
		this.networkController = networkController;

		networkController.setAuctionRoundHandler(this);

		acceptedOffer = new Offer(settings.goods.get(settings.actualRound - 1));

		participantsOfferRecived = new HashMap<String, Double>();
		offersRecived = 0;

		pausingParticipants = new ArrayList<String>();

		networkController.sendNewRoundMessage(settings.actualRound,
				acceptedOffer.good.amount, acceptedOffer.good.price);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleParticipation(int round, double offer,
			String participantID) {
		System.out.println("--> recived offer: " + offer
				+ " from participant with id: " + participantID);

		if (pausingParticipants.contains(participantID)) {
			pausingParticipants.remove(pausingParticipants
					.indexOf(participantID));
		}
		if (offersRecived < (settings.participants - pausingParticipants.size())) {
			participantsOfferRecived.put(participantID, offer);
			offersRecived++;
		}
		if (offersRecived == settings.participants) {
			lookUpAcceptedOffer();
		}
	}

	/**
	 * Look up for the best offers and propagate it to the participants.
	 */
	private void lookUpAcceptedOffer() {
		for (String key : participantsOfferRecived.keySet()) {
			double price = participantsOfferRecived.get(key);
			if (price > acceptedOffer.price) {
				acceptedOffer.price = price;
				acceptedOffer.offerand = key;
			}
			offersRecived = 0;
		}
		networkController.sendAcceptedOffer(acceptedOffer.offerand,
				acceptedOffer.price);
		System.out.println("<-- propagate accepted offer: "
				+ acceptedOffer.price + ", participant with id: "
				+ acceptedOffer.offerand);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handlePausing(String participantID) {
		if (!pausingParticipants.contains(participantID)) {
			pausingParticipants.add(participantID);
		}
		System.out.println("--> participant with id: " + participantID
				+ " pauses.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Offer getResult() {
		return acceptedOffer;
	}

}
