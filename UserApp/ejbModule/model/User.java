package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
	

	private String username;
	private String firstName;
	private String lastName;
	private Host host;
	private String password;
	
	private List<String> friends;
	
	
	public User() {
		this.friends = new ArrayList<String>();
	}


	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	



	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public Host getHost() {
		return host;
	}


	public void setHost(Host host) {
		this.host = host;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}
	

	public List<String> getFriends(){
		return friends;
	}
	

	
	public void setFriends(List<String> friends) {
		this.friends = friends;
	}


	public User(String username, String firstName, String lastName, String password) {
		super();
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		//this.host = host;
		this.password = password;
		this.friends = new ArrayList<>();
	}


	public String toString() {
		return username + "  " + password + "  " + firstName + "  " + lastName;
	}
	
	

}
