package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.controllers;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.MessageHandler;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.MessageListener;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.MessageSender;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.MessageType;

public class PurchaseController implements MessageHandler {

	private MessageSender messageSender;
	private MessageListener messageListener;
	private Socket socket;

	/**
	 * Initialize TCP end points to communicate with auction's manger agent.
	 * 
	 * @param host
	 *            The host address of the auction's manger agent.
	 * @param port
	 *            The port of the auction's manger agent.
	 */
	public PurchaseController(String host, int port) {
		try {
			socket = new Socket(host, port);
			messageSender = new MessageSender(socket);
			messageListener = new MessageListener(socket, this);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void handle(String jsonMessage) {

		try {
			JSONObject messageObject = new JSONObject(jsonMessage);
			String action = messageObject.getString("action");
			MessageType type = MessageType.valueOf(action);
			processMessage(type, messageObject);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void processMessage(MessageType type, JSONObject messageObject) {
		switch (type) {

		}
	}

}
