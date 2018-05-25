package controllers;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
	
	@GET
	@Path("/login/{username}")
	public void login(@PathParam("username")String username) {
		boolean exists = false;
		User user = null;
		List<User> activeUsers = clusterInit.getActiveUsers();
		
		for(User u: activeUsers) {
			if(u.getUsername().equals(username)) {
				exists = true;
				user = u;
				break;
			}				
		}
		if(!exists) {
			System.out.println("Dodat u aktivne userse  "+ user);
			activeUsers.add(user);
//			userRequestsJMS.sendRequest("LoginNotify," + user.getUsername());
		}
		System.out.println("ACTIVE USERS AFTER ADDING " + activeUsers);
	}
	
	@GET
	@Path("/logout/{username}")
	public void logout(@PathParam("username") String username) {
		List<User> activeUsers = clusterInit.getActiveUsers();
		for(User u: activeUsers) {
			if(u.getUsername().equals(username)) {
				activeUsers.remove(u);
//				userRequestsJMS.sendRequest("LogoutNotify," + u.getUsername());
			}				
		}
	}
	

}
