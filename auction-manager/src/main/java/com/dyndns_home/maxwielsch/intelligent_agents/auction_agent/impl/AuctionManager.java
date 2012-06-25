package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.AuctionHandler;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.ConsoleHandler;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.ConsoleListener;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.model.AuctionSettings;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.model.Offer;

public class AuctionManager implements AuctionHandler, ConsoleHandler {

	private static final int SECONDS = 20;
	private NetworkController networkController;
	private ConsoleListener consoleListener;
	private AuctionSettings settings;
	private AuctionRound actualAuctionRound;
	private List<Offer> dealtOffers;

	public AuctionManager(int portToListenTo) throws IOException {
		System.out.println("... auction manager started");
		dealtOffers = new ArrayList<Offer>();
		consoleListener = new ConsoleListener(this);
		consoleListener.start();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void participantsComplete() {
		System.out.println("... all participants connected\n... begin auction");
		beginNextRound();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void declareAuctionSettings(AuctionSettings settings) {
		System.out.println("... waiting for auction's participants");
		this.settings = settings;
		networkController = new NetworkController(settings.port,
				settings.participants, this);
		networkController.start();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleQuit(boolean error) {
		if (networkController != null) {

			networkController.shutDown();
		}
		System.out.println("... auction manager stopped");
		if (error) {
			System.exit(-1);
		} else {
			System.exit(0);
		}
	}

	/**
	 * Begin a new auction round.
	 */
	public void beginNextRound() {
		settings.actualRound = settings.actualRound + 1;
		if (settings.actualRound != 1) {
			dealtOffers.add(actualAuctionRound.getResult());
		}
		if (settings.actualRound <= settings.rounds) {
			actualAuctionRound = new AuctionRound(settings, networkController);
			Timer timer = new Timer();
			timer.schedule(new AuctionEndTask(this), 1000 * SECONDS);
		} else {
			printResult();
			handleQuit(false);
		}
	}

	/**
	 * Finish the actual auction round.
	 */
	public void finishActualRound() {
		networkController
				.sendAuctionRoundEnd(actualAuctionRound.getResult().offerand);
	}

	/**
	 * Print out the auction result.
	 */
	private void printResult() {
		System.out.println("... auction finished");
		System.out.println("... result:");
		for (Offer offer : dealtOffers) {
			System.out.println(offer);
		}
		System.out.println("======================================");
		System.out.println("... summarized results by participant:");
		Map<String, Integer> summary = getSummary();
		for (String key : summary.keySet()) {
			System.out.println(key + ": " + summary.get(key) + " goods bought");
		}
	}

	/**
	 * Get a summarized result by each participant.
	 * 
	 * @return A map containing the participantID and the bought goods amount.
	 */
	private Map<String, Integer> getSummary() {
		Map<String, Integer> summary = new HashMap<String, Integer>();
		for (Offer offer : dealtOffers) {
			if (summary.containsKey(offer.offerand)) {
				int oldAmount = summary.get(offer.offerand);
				summary.put(offer.offerand, offer.good.amount + oldAmount);
			} else {
				summary.put(offer.offerand, offer.good.amount);
			}
		}
		return summary;
	}

	/**
	 * Get the network controller.
	 * 
	 * @return A NetworkController object.
	 */
	public NetworkController getNetworkController() {
		return networkController;
	}

}
