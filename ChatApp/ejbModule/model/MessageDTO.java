package model;

public class MessageDTO {
	User user;
	User friend;
	String content;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public User getFriend() {
		return friend;
	}
	public void setFriend(User friend) {
		this.friend = friend;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public MessageDTO(User user, User friend, String content) {
		super();
		this.user = user;
		this.friend = friend;
		this.content = content;
	}
	
	public MessageDTO(){
		
	}
}
