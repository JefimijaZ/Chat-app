package model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

@Singleton
public class ActiveUsers {
	
	private  List<User> activeUsers;
	
	@PostConstruct
	public void init() {
		activeUsers= new ArrayList<User>();
	}

	
	public List<User> getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(List<User> activeUsers) {
		this.activeUsers = activeUsers;
	}

	@Lock(LockType.WRITE)
	public void login(User user) {
		activeUsers.add(user);
	}
	
	@Lock(LockType.WRITE)
	public void logout(User user) {
		activeUsers.remove(user);
	}

	
}
