package model;

import java.util.Date;

public class Message {
	
	private String username;
	private String friendsUsername;
	private String usersMessage;
	
	
	public Message() {
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getFriendsUsername() {
		return friendsUsername;
	}


	public void setFriendsUsername(String friendsUsername) {
		this.friendsUsername = friendsUsername;
	}


	public String getUsersMessage() {
		return usersMessage;
	}


	public void setUsersMessage(String usersMessage) {
		this.usersMessage = usersMessage;
	}


	public Message(String username, String friendsUsername, String usersMessage) {
		super();
		this.username = username;
		this.friendsUsername = friendsUsername;
		this.usersMessage = usersMessage;
	}

}
