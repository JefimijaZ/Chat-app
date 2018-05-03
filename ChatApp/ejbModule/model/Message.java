package model;

import java.util.Date;

public class Message {
	
	private int id;
	private User sender;
	private User receiver;
	private Date date;
	private String content;
	
	
	public Message() {
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public User getSender() {
		return sender;
	}


	public void setSender(User sender) {
		this.sender = sender;
	}


	public User getReceiver() {
		return receiver;
	}


	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}
	
}
