package message;

import java.util.List;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import controller.FriendController;
import controller.UserController;
import model.Friends;
import model.User;


@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/mojQueue") })
public class UserRequest implements MessageListener {

	@EJB
	UserController userController;
	@EJB
	FriendController friendController;
	@EJB
	UserResponseJMS userResponse;
	
	@Override
	public void onMessage(Message message) {
		TextMessage tm = (TextMessage) message;
		String text;
		
		try {
			text = tm.getText();
			System.out.println("USER REQUEST MESSAGE RECEIVED  " + text);
			String type = text.split(",")[0];
			String username, password, firstName, lastName, friendUsername, search;
			int retF;
			User user;
			Friends friend;
			switch (type) {
			case "Login":
				System.out.println("Login");
				username = text.split(",")[1];
				password = text.split(",")[2];
				user = new User(username, password);
				System.out.println("USER REQUEST MESSAGE RECEIVED USER " + user);
				boolean response = userController.login(user);
				
				userResponse.sendRequest("LoginResponse," + response + "," + user.getUsername() + ","+text.split(",")[3] );
				break;
			case "Register":
				username = text.split(",")[1];
				password = text.split(",")[2];
				firstName = text.split(",")[3];
				lastName = text.split(",")[4];
				user = new User(username,firstName,lastName, password);
				int rez = userController.register(user);
				userResponse.sendRequest("RegisterResponse," + rez +  "," +text.split(",")[5] );
				break;
			case "addFriend":
				friendUsername =  text.split(",")[1];
				username =  text.split(",")[2];
				friend = new Friends(username,friendUsername);
				retF = friendController.addFriend(friend);
				userResponse.sendRequest("addFriendResponse," + retF +  "," +text.split(",")[3] );
				break;
			case "removeFriend":
				friendUsername =  text.split(",")[1];
				username =  text.split(",")[2];
				friend = new Friends(username,friendUsername);
				retF = friendController.removeFriend(friendUsername, username);
				userResponse.sendRequest("removeFriendResponse," + retF +  "," +text.split(",")[3] );
				break;
			case "getFriends":
				username =  text.split(",")[1];
				List<User> users = friendController.getFriends(username);
				//TODO: Poslati listu usera
				userResponse.sendRequest("getFriendsResponse," + users + "," +text.split(",")[3]  );
				break;
			case "User":
				username =  text.split(",")[1];
				User ret = userController.getUser(username);
				userResponse.sendRequest("UserResponse," + ret.toString() +  "," +text.split(",")[2] );
				break;
			case "Logout":				
				username = text.split(",")[1];
				boolean res = userController.logout(username);
				userResponse.sendRequest("LogoutResponse," + res + "," + username + "," +text.split(",")[2] );
				break;
			case "Search":
				search =  text.split(",")[1];
				List<User> searchUsers = userController.search(search);
				userResponse.sendRequest("SearchResponse," +searchUsers +  "," +text.split(",")[2] );
				break;
			default:
				return;

			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
