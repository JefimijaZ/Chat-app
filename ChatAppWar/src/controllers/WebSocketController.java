package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import message.UserRequestsJMS;
import model.Configuration;
import model.Friends;
import model.Message;
import model.User;
import service.UserService;

@ServerEndpoint("/usersEndPoint")
@Singleton
public class WebSocketController {

	@EJB
	UserService userService;
	@EJB
	UserRequestsJMS userRequests;
	
	List<Session> sessions = new ArrayList<Session>();

	@OnMessage
	public String onMessage(Session session, String message) {
		String request = message.split(",")[0];
		String password, firstName, lastName, friendUsername, userMessage;
		String response = "", username = "";
		int resp;
		User user;
		switch (request) {
		case "Login":
			if (!Configuration.masterAdress.equals(Configuration.localAdress)) {
				System.out.println("Usao je u if kao da je na slave cvoru");
				username = message.split(",")[1];
				password = message.split(",")[2];
				user = new User(username, password);
				response = ((ResteasyWebTarget) ClientBuilder.newClient()
						.target("http://" + "localhost:8080" + "/UserAppWar/rest/user/login")).request()
								.post(Entity.json(user)).readEntity(String.class);
//						userService.login(user);
				return "login," + response + "," + username;

			} else {
				System.out.println("START LOGIN MESSAGE:  " + message);
				System.out.println("STARTING LOGIN");
				userRequests.sendRequest(message + "," + session.getId());
				System.out.println("JMS response :  ");
				break;
				
			}
			
//			break;

		case "Register":
			if (!Configuration.masterAdress.equals(Configuration.localAdress)) {
				System.out.println("User:"+message);
				username = message.split(",")[1];
				password = message.split(",")[2];
				firstName = message.split(",")[3];
				lastName = message.split(",")[4];
				user = new User(username, firstName, lastName, password);
				System.out.println("usao u register");
				resp = ((ResteasyWebTarget) ClientBuilder.newClient()
						.target("http://" + "localhost:8080" + "/UserAppWar/rest/user/register")).request()
								.post(Entity.json(user)).readEntity(Integer.class);

				System.out.println("RESPONSE:" + resp);
				return resp + "";
			} else {
				userRequests.sendRequest(message+ "," + session.getId());
			}
		case "addFriend":
			if (!Configuration.masterAdress.equals(Configuration.localAdress)) {
				friendUsername = message.split(",")[1];
				username = message.split(",")[2];
				Friends friend = new Friends(friendUsername, username);
				resp = ((ResteasyWebTarget) ClientBuilder.newClient()
						.target("http://" + "localhost:8080" + "/UserAppWar/rest/friend/add")).request()
								.post(Entity.json(friend)).readEntity(Integer.class);
				System.out.println("add friend respo: " + resp);
				return resp + "";
			} else {
				userRequests.sendRequest(message+ "," + session.getId());
			}
		case "removeFriend":
			if (!Configuration.masterAdress.equals(Configuration.localAdress)) {
				friendUsername = message.split(",")[1];
				username = message.split(",")[2];
				Friends f = new Friends(friendUsername, username);
				resp = ((ResteasyWebTarget) ClientBuilder.newClient()
						.target("http://" + "localhost:8080" + "/UserAppWar/rest/friend/remove/"+friendUsername+"/"+username)).request()
								.delete().readEntity(Integer.class);
				System.out.println("delete friend respo: " + resp);
			} else {
				userRequests.sendRequest(message+ "," + session.getId());
			}

		case "User":
			if (!Configuration.masterAdress.equals(Configuration.localAdress)) {
			response = ((ResteasyWebTarget) ClientBuilder.newClient()
					.target("http://" + "localhost:8080" + "/UserAppWar/rest/user/" + message.split(",")[1])).request()
							.get(String.class);
			System.out.println("RESPONSE:" + response);
			return response;
			} else {
				userRequests.sendRequest(message+ "," + session.getId());
			}
		case "Search":
			if (!Configuration.masterAdress.equals(Configuration.localAdress)) {
				response =  ((ResteasyWebTarget) ClientBuilder.newClient()
						.target("http://" + "localhost:8080" + "/UserAppWar/rest/user/search/" + message.split(",")[1]))
								.request().get(String.class);
				System.out.println("RESPONSE:" + response);
				return "searchResults/"+response;
			} else {
				userRequests.sendRequest(message+ "," + session.getId());
			}

		case "Logout":
			if (!Configuration.masterAdress.equals(Configuration.localAdress)) {
				System.out.println("Usao u logout i gadjam" + " http://" + "localhost:8080"
						+ "/UserAppWar/rest/user/logout" + message.split(",")[1]);
				username = message.split(",")[1];
				response = ((ResteasyWebTarget) ClientBuilder.newClient()
						.target("http://" + "localhost:8080" + "/UserAppWar/rest/user/logout/" + username)).request()
								.get(String.class);
				System.out.println("RESPONSE:" + response);
				userService.logout(username);
				return "logout," + response;
			} else {
				userRequests.sendRequest(message + "," + session.getId());
			}
		
		case "getUserFriends":
			if (!Configuration.masterAdress.equals(Configuration.localAdress)) {
//				System.out.println("Usao u logout i gadjam" + " http://" + "localhost:8080"
//						+ "/UserAppWar/rest/user/logout" + message.split(",")[1]);
				username = message.split(",")[1];
				response = ((ResteasyWebTarget) ClientBuilder.newClient()
						.target("http://" + "localhost:8080" + "/UserAppWar/rest/friend/all/" + username)).request()
								.get(String.class);
				System.out.println("RESPONSE:" + response);
				
				return "friends/"+response;
			} else {
				userRequests.sendRequest(message + "," + session.getId());
			}
		case "getConversations":
			if (!Configuration.masterAdress.equals(Configuration.localAdress)) {
//				System.out.println("Usao u logout i gadjam" + " http://" + "localhost:8080"
//						+ "/UserAppWar/rest/user/logout" + message.split(",")[1]);
				username = message.split(",")[1];
				response = ((ResteasyWebTarget) ClientBuilder.newClient()
						.target("http://" + "localhost:8080" + "/UserAppWar/rest/conversation/all/" + username)).request()
								.get(String.class);
				System.out.println("RESPONSE:" + response);
				
				return "conversations/"+response;
			} else {
				userRequests.sendRequest(message + "," + session.getId());
			}
		case "newConversation":
			if (!Configuration.masterAdress.equals(Configuration.localAdress)) {
//				System.out.println("Usao u logout i gadjam" + " http://" + "localhost:8080"
//						+ "/UserAppWar/rest/user/logout" + message.split(",")[1]);
				username = message.split(",")[1];
				friendUsername = message.split(",")[2];
				userMessage = message.split(",")[3];
				Message msg = new Message(username, friendUsername, userMessage);
				response = ((ResteasyWebTarget) ClientBuilder.newClient()
						.target("http://" + "localhost:8080" + "/UserAppWar/rest/conversation/add/")).request()
								.post(Entity.json(msg)).readEntity(String.class);
				System.out.println("RESPONSE:" + response);
				
				return "conversations/"+response;
			} else {
				userRequests.sendRequest(message + "," + session.getId());
			}
		}
			
		return "heej";
	}

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("onOpen::" + session.getId());
		if (!sessions.contains(session)) {
			sessions.add(session);
		}
		System.out.println("otvara ");
	}

	@OnClose
	public void onClose(Session session) {
		sessions.remove(session);
		System.out.println("zatvara ");
	}
	
	public Session getSession(String id) {
		for(Session s: sessions) {
			if(s.getId().equals(id))
				return s;
		}
		return null;
	}
}
