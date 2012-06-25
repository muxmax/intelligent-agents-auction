package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.AuctionHandler;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.ConsoleHandler;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.ConsoleListener;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.model.AuctionSettings;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.model.Offer;

public class AuctionManager implements AuctionHandler, ConsoleHandler {

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
	 * A finished auction round is responsible to let begin the next round.
	 */
	public void beginNextRound() {
		settings.actualRound++;
		if (settings.actualRound != 1) {
			dealtOffers.add(actualAuctionRound.getResult());
		}
		if (settings.actualRound <= settings.rounds) {
			actualAuctionRound = new AuctionRound(settings, networkController);
			Timer timer = new Timer();
			timer.schedule(new AuctionEndTask(this), 1000 * 60);
		} else {
			printResult();
			handleQuit(false);
		}
	}

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
