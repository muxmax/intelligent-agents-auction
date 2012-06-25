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
	private Map<String, Long> participantsOfferRecived;
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

		acceptedOffer = new Offer(settings.goods.get(settings.actualRound - 1),
				settings.actualRound);

		participantsOfferRecived = new HashMap<String, Long>();
		offersRecived = 0;

		pausingParticipants = new ArrayList<String>();

		networkController.sendNewRoundMessage(settings.actualRound,
				acceptedOffer.good.amount, acceptedOffer.good.price);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleParticipation(int round, long offer, String participantID) {
		System.out.println("--> recived offer: " + offer / 100.00
				+ " from participant with id: " + participantID);

		if (pausingParticipants.contains(participantID)) {
			pausingParticipants.remove(pausingParticipants
					.indexOf(participantID));
		}
		if (offersRecived < (settings.participants - pausingParticipants.size())) {
			if (!participantsOfferRecived.keySet().contains(participantID)) {
				participantsOfferRecived.put(participantID, offer);
				offersRecived++;
			}
		}
		if (offersRecived == settings.participants) {
			lookUpAcceptedOffer(participantsOfferRecived);
		}
	}

	/**
	 * Look up for the best offers and propagate it to the participants.
	 */
	private void lookUpAcceptedOffer(Map<String, Long> participantsOffers) {
		for (String key : participantsOffers.keySet()) {
			long price = participantsOffers.get(key);
			if (price > acceptedOffer.price) {
				acceptedOffer.price = price;
				acceptedOffer.offerand = key;
			}

		}
		offersRecived = 0;
		participantsOffers.clear();
		networkController.sendAcceptedOffer(acceptedOffer.offerand,
				acceptedOffer.price);
		System.out.println("<-- propagate accepted offer: "
				+ acceptedOffer.price / 100.00 + ", participant with id: "
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
