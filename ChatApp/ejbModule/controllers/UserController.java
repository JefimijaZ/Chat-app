package controllers;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cluster.ClusterInit;
import model.User;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class UserController {
	
	@EJB
	ClusterInit clusterInit;
	
	
	@POST
	@Path("/login")
	public void login(User user) {
		boolean exists = false;
		List<User> activeUsers = clusterInit.getActiveUsers();
		for(User u: activeUsers) {
			if(u.getUsername().equals(user.getUsername())) {
				exists = true;
				break;
			}				
		}
		if(!exists)
			activeUsers.add(user);
	}
	
	@GET
	@Path("/logout/{username}")
	public void logout(@PathParam("username") String username) {
		List<User> activeUsers = clusterInit.getActiveUsers();
		for(User u: activeUsers) {
			if(u.getUsername().equals(username)) {
				activeUsers.remove(u);
			}				
		}
	}
	

}
