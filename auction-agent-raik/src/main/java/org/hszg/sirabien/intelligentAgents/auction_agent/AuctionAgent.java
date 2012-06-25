package org.hszg.sirabien.intelligentAgents.auction_agent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.client.ClientMessageConnection;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.client.ClientMessageHandler;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.exceptions.InvalidJsonMessageException;

public class AuctionAgent implements ClientMessageHandler {

	/**
	 * Time of one round in milliseconds.
	 */
	private static final int ROUND_TIME = 1 * 60 * 1000;
	private static final long START_MONEY = 100000;

	private ClientMessageConnection connection;
	private Random random;

	private InetAddress server;
	/**
	 * The remaining money.
	 */
	private long currentMoney;
	/**
	 * The amount of good that have already been won.
	 */
	private long currentAmount;

	private AuctionAgent(String[] args) {
		currentMoney = START_MONEY;
		random = new Random();
		handleArgs(args);
	}

	private void handleArgs(String[] args) {
		if (args.length != 1)
			printHelpAndExit();
		try {
			server = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e) {
			System.err.println("You've entered an invalid server address.");
			printHelpAndExit();
		}

	}

	private void printHelpAndExit() {
		System.out
				.println("Usage: java -jar auctionAgent.jar <Server-Address>");
		System.exit(-1);
	}

	private void start() {
		try {
			connection = new ClientMessageConnection(server, this);
			connection.establish();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidJsonMessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void handleNewAuctionRound(int roundNumber, int amount, long price) {
		long tenth = currentMoney / 10;
		long bet = 3 * tenth + random.nextInt(4 * (int) tenth);
		System.out.println("new auction round, bet " + toMoney(bet) + " for " + amount
				+ " units.");
		try {
			connection.getMessageSender().sendParticipateMessage(roundNumber,
					bet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidJsonMessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void handleLastAcceptedOffer(String participant, long price) {
		System.out.println("current offer: " + toMoney(price) + " from " + participant
				+ (connection.isMe(participant) ? " (that's me)" : ""));
	}

	@Override
	public void handleEndAuctionRound(String winner) {
		System.out.println("end auction round, "
				+ (connection.isMe(winner) ? "i've won" : "i haven't won"));
	}

	@Override
	public void handleAuctionEnd() {
		System.out.println("The auction has finished. I've won "
				+ currentAmount + " for " + toMoney(START_MONEY - currentMoney)
				+ ". I've " + toMoney(currentMoney) + " remaining.");

	}

	private String toMoney(long bet) {
		StringBuilder builder = new StringBuilder();
		builder.append(bet / 100);
		builder.append(".");
		builder.append(bet % 100);
		builder.append("€");
		return builder.toString();
	}

	public static void main(String[] args) {
		new AuctionAgent(args).start();
	}
}
