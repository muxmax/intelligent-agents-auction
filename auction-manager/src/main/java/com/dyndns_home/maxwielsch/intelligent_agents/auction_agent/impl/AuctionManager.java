package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.impl;

import java.io.IOException;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.AuctionHandler;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.ConsoleHandler;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.ConsoleListener;

public class AuctionManager implements AuctionHandler, ConsoleHandler {

	private NetworkController networkController;
	private ConsoleListener consoleListener;
	private AuctionSettings settings;
	private int round;

	public AuctionManager(int portToListenTo) throws IOException {
		System.out.println("... auction manager started");
		consoleListener = new ConsoleListener(this);
		consoleListener.start();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void participantsComplete() {
		System.out.println("... all participants connected\n... begin auction");
		round = 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleParticipation(int round, double offer,
			String participantID) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handlePausing(String participantID) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void declareAuctionSettings(AuctionSettings settings) {
		System.out.println("... waiting for auction's participants");
		this.settings = settings;
		networkController = new NetworkController(settings.port, settings.participants, this);
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

}
