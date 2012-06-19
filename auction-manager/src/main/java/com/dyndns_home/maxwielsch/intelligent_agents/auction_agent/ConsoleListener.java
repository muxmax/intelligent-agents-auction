package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.model.AuctionSettings;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.model.Good;

/**
 * This class handles the initial console communication with the user to set up
 * the auction's conditions.
 * 
 * @author Max Wielsch
 * 
 */
public class ConsoleListener extends Thread {

	private BufferedReader inputReader;
	private ConsoleHandler consoleHandler;
	private AuctionSettings settings;
	private String line;

	public ConsoleListener(ConsoleHandler consoleHandler) {
		this.consoleHandler = consoleHandler;
		inputReader = new BufferedReader(new InputStreamReader(System.in));
		settings = new AuctionSettings();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {

		System.out.println("... setup auction conditions");

		try {
			executeConfigurationDialog();
			finishConfiguration();
		} catch (IOException e) {
			System.err.println("Error while reading from system input stream!");
			consoleHandler.handleQuit(true);
		}

	}

	/**
	 * Run through the set up process.
	 * 
	 * @throws IOException
	 *             Will be thrown when there occurs an error with input or
	 *             output stream of the console.
	 */
	private void executeConfigurationDialog() throws IOException {

		System.out
				.println("On which port should be listened for auction participants? The default port is 50000.\n"
						+ "Type only enter if you wnat to choose the default port or type in a number to choose a different one.");
		do {
			System.out.print("port to listen on (enter a number): ");
			line = inputReader.readLine().trim();
		} while (!line.matches("[1-9][0-9]*") && line.length() != 0);
		if (line.length() != 0) {
			settings.port = Integer.parseInt(line);
		}

		do {
			System.out.print("participants amount (enter a number): ");
			line = inputReader.readLine().trim();
		} while (!line.matches("[1-9][0-9]*"));
		settings.participants = Integer.parseInt(line);

		do {
			System.out.print("auction rounds (enter a number): ");
			line = inputReader.readLine().trim();
		} while (!line.matches("[1-9][0-9]*"));
		settings.rounds = Integer.parseInt(line);

		System.out
				.println("Please enter the goods that you want to sell as follows '<amount> <price>' when you are asked for.");

		int rounds = Integer.parseInt(line);
		Pattern goodsPattern = Pattern
				.compile("([1-9][0-9]*) ([1-9][0-9]*(\\.[0-9]{1,2})?)");
		for (int i = 1; i <= rounds; i++) {
			System.out.print("good " + i + ": ");
			line = inputReader.readLine().trim();
			Matcher matcher = goodsPattern.matcher(line);
			if (matcher.matches()) {
				settings.goods.add(new Good(Integer.parseInt(matcher
						.group(1)), Double.parseDouble(matcher.group(2))));
			} else {
				System.out
						.println("Please use the form (<amount>,<price>) to enter a good!\n"
								+ "<amount> must be an integer number and <price> must be a double number!");
				i--;
			}
		}
	}

	/**
	 * Finish the set up process.
	 * 
	 * @throws IOException
	 *             Will be thrown when there occurs an error with input or
	 *             output stream of the console.
	 */
	private void finishConfiguration() throws IOException {

		do {
			System.out.println("---------------------");
			System.out.println(settings);
			System.out.println("---------------------");
			System.out
					.println("Is the given configuration correct?\nEnter 'y' or 'n'):");
			line = inputReader.readLine().trim();
		} while (!line.matches("[y|n]"));

		if (line.equals("n")) {
			System.out
					.println("Then restart the application and configure again.");
			consoleHandler.handleQuit(true);
		} else if (line.equals("y")) {
			consoleHandler.declareAuctionSettings(settings);

			int wrongInput = -1;
			do {
				wrongInput++;
				if (wrongInput > 0) {
					printHelp();
				}
				line = inputReader.readLine().trim();
			} while (!line.equals("-q") && !line.equals("--quit"));

			consoleHandler.handleQuit(false);
		}
	}

	/**
	 * Print out the help page of the console.
	 */
	private void printHelp() {
		System.out.println("Usage of AuctionManager");
		System.out.println("-q | --quit      Quit the application.");
	}
}
