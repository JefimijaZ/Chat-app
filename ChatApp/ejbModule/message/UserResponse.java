package message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.websocket.EncodeException;
import javax.websocket.Session;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.google.gson.Gson;

//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import controllers.WebSocketController;
import model.User;
import service.UserService;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/mojTopic") })
public class UserResponse implements MessageListener {

	@EJB
	UserService userService;

	@EJB
	WebSocketController socket;

	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			Session session;
			TextMessage tm = (TextMessage) message;
			String text;
			try {
				text = tm.getText();

				String type = text.split(",")[0];
				String response, username;
				System.out.println("TYPE :  " + text);
				switch (type) {
				case "LoginResponse":
					username = text.split(",")[2];
//					User user = ((ResteasyWebTarget) ClientBuilder.newClient()
//							.target("http://" + "localhost:8080" + "/UserAppWar/rest/user/" + username)).request()
//									.get(User.class);
					response = text.split(",")[1];
					session = socket.getSession(text.split(",")[3]);
					session.getBasicRemote().sendText("login," + response + "," + username);
					break;
				case "LogoutResponse":
					username = text.split(",")[2];
					response = text.split(",")[1];
					session = socket.getSession(text.split(",")[3]);
					session.getBasicRemote().sendText("logout," + response);
					break;
				case "RegisterResponse":
					response = text.split(",")[1];
					session = socket.getSession(text.split(",")[2]);
					session.getBasicRemote().sendText(response);
					break;
				
				case "addFriendResponse":
					response = text.split(",")[1];
					session = socket.getSession(text.split(",")[2]);
					session.getBasicRemote().sendText(response + "");
					break;
				case "removeFriendResponse":
					response = text.split(",")[1];
					session = socket.getSession(text.split(",")[2]);
					session.getBasicRemote().sendText(response + "");
					break;
				case "SearchResponse":
					session = socket.getSession(text.split(",")[1]);
					break;
				case "LoginNotify":
					System.out.println("Login notify case about to call all");
					username = text.split(",")[1];
					userService.login(username);
					break;
				case "LogoutNotify":
					System.out.println("Logout notify case about to call all");
					username = text.split(",")[1];
					userService.logout(username);
					break;

				default:
					return;

				}

			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (message instanceof ObjectMessage) {
			ObjectMessage tm = (ObjectMessage) message;
			Session session;
			
			// 1 - getUser,2 - searchResult, 3 - getFriends

			try {
				int type = tm.getIntProperty("type");
				session = socket.getSession(tm.getStringProperty("sessionId"));
				if(type == 1) {
					User user = (User) tm.getObject();					
					String gson = new Gson().toJson(user);
					session.getBasicRemote().sendText(gson);
				}else if (type == 2) {
					List<User> listData = null;
					listData = (ArrayList) tm.getObject();					
					String gson = new Gson().toJson(listData);
					session.getBasicRemote().sendText("searchResults/" + gson);
				} else if (type==3) {
					List<User> listData = null;
					listData = (ArrayList) tm.getObject();					
					String gson = new Gson().toJson(listData);
					System.out.println("GSON friends ALL " + gson);
					session.getBasicRemote().sendText("friends/" + gson);
				}
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
