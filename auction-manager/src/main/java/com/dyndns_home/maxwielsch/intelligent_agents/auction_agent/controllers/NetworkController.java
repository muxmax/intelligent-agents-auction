package com.dyndns_home.maxwielsch.intelligent_agents.auction_agent.controllers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.MessageConnection;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.MessageHandler;
import com.dyndns_home.maxwielsch.intelligent_agents.auction_agents_messaging.MessageType;

public class NetworkController implements MessageHandler {

	private List<MessageConnection> messageConnections;
	private ServerSocket socket;
	private boolean listen = true;

	/**
	 * Initialize TCP end points to communicate with auction's manger agent.
	 * 
	 * @param host
	 *            The host address of the auction's manger agent.
	 * @param port
	 *            The port of the auction's manger agent.
	 */
	public NetworkController(int port) {
		try {
			socket = new ServerSocket(port);
			messageConnections = new ArrayList<MessageConnection>();
			listen();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void listen() {
		while (listen) {
			try {
				Socket clientSocket = socket.accept();
				MessageConnection connection = new MessageConnection(clientSocket,this);
				messageConnections.add(connection);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
		System.out.println(messageObject.toString());
	}

}
