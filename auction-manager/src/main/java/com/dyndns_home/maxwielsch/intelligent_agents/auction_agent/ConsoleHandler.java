package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.model.AuctionSettings;

/**
 * This interface should be implemented by the class that needs to get the
 * settings for the auction from the user via Console.
 * 
 * @author Max Wielsch
 * 
 */
public interface ConsoleHandler {

	/**
	 * After set up process has finished declare the settings from it.
	 * 
	 * @param settings
	 *            A simple data object holding settings information.
	 */
	public void declareAuctionSettings(AuctionSettings settings);

	/**
	 * Tell the handler to quit the application.
	 * 
	 * @param error
	 *            A flag that determines whether an error occurred or not.
	 */
	public void handleQuit(boolean error);

}
