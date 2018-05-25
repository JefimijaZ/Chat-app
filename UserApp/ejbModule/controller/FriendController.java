package controller;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
	@Path("/all/{username}")
	public List<User> getFriends(@PathParam("username") String username) {
		return friendRepository.getUserFriends(username);
	}
	
	@POST
	@Path("/add")
	public int addFriend(Friends friend) {
		friendRepository.addFriend(friend.getUserOne(), friend.getUserTwo());
		
		return HttpServletResponse.SC_OK;
	}
	
	@DELETE
	@Path("/remove/{friendUsername}/{username}")
	public int removeFriend(@PathParam("friendUsername") String friendsUsername, @PathParam("username") String username) {
		friendRepository.removeFriend(friendsUsername, username);
		friendRepository.removeFriend(username, friendsUsername);
		return HttpServletResponse.SC_OK;
	}
	
	
	
}
