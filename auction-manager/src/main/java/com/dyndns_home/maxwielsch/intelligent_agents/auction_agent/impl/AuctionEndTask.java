package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.impl;

import java.util.TimerTask;


public class AuctionEndTask extends TimerTask {

	private AuctionManager auctionManager;
	
	public AuctionEndTask(AuctionManager auctionManager) {
		this.auctionManager = auctionManager;
	}
	
	@Override
	public void run() {
		auctionManager.finishActualRound();
		auctionManager.beginNextRound();
		this.cancel();
	}

}
