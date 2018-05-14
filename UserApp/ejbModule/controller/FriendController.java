package controller;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import database.FriendRepository;
import model.Friends;
import model.User;

@Path("/friend")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FriendController {

	@EJB
	FriendRepository friendRepository;
	

	@GET
	@Path("/all")
	public List<User> getFriends(String username) {
		return friendRepository.getUserFriends(username);
	}
	
	@PUT
	@Path("/add")
	public boolean addFriend(Friends friend) {
		friendRepository.addFriend(friend.getUserOne(), friend.getUserTwo());
		
		return true;
	}
	
	@DELETE
	@Path("/remove")
	public boolean removeFriend(Friends friend) {
		friendRepository.addFriend(friend.getUserOne(), friend.getUserTwo());
		return true;
	}
	
	
	
}
