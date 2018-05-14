package message;

import java.io.IOException;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.websocket.Session;
import javax.ws.rs.client.ClientBuilder;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

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
		TextMessage tm = (TextMessage) message;
		String text;
		try {
			text = tm.getText();
			Session session;
			String type = text.split(",")[0];
			String response, username;
			System.out.println("TYPE :  " + text);
			switch (type) {
			case "LoginResponse":
				username = text.split(",")[2];
				System.out.println("*****************Login Response " + username);
				User user = ((ResteasyWebTarget) ClientBuilder.newClient()
						.target("http://" + "localhost:8080" + "/UserAppWar/rest/user/" + username)).request()
								.get(User.class);
				response = text.split(",")[1];
				System.out.println("************************LOGGED IN USER: " + user);
				userService.login(user);
				session = socket.getSession(text.split(",")[3]);
				session.getBasicRemote().sendText("login," + response + "," + username);
				break;

			case "LogoutResponse":
				username = text.split(",")[2];
				response = text.split(",")[1];
				System.out.println("************************LOGGED OUT USER: " + username);
				userService.logout(username);
				session = socket.getSession(text.split(",")[3]);
				session.getBasicRemote().sendText("logout," + response);
				break;
			case "RegisterResponse":
				response = text.split(",")[1];
				session = socket.getSession(text.split(",")[2]);
				session.getBasicRemote().sendText(response);
				break;
			case "UserResponse":
				response = text.split(",")[1];
				session = socket.getSession(text.split(",")[2]);
				session.getBasicRemote().sendText(response);
				break;
			case "addFriendResponse":
				response = text.split(",")[1];
				session = socket.getSession(text.split(",")[2]);
				session.getBasicRemote().sendText(response);
				break;
			case "removeFriendResponse":
				response = text.split(",")[1];
				session = socket.getSession(text.split(",")[2]);
				session.getBasicRemote().sendText(response);
				break;
			case "SearchResponse":
				response = text.split(",")[1];
				session = socket.getSession(text.split(",")[2]);
				session.getBasicRemote().sendText(response);
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

	}

}
