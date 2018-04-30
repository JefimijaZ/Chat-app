package controller;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.bson.Document;

import com.mongodb.client.MongoCursor;

import database.DatabaseConnection;
import database.UserRepository;
import model.User;

@Path("/user")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

	@EJB
	UserRepository repository;
	
	private static List<User> activeUsers = new ArrayList<User>();

	@GET
	@Path("/active")
	public List<User> activeUsers(){		
		return activeUsers;
	}
	
	@POST
	@Path("/login")
	public boolean login(@Context HttpServletRequest request, User user) {
		List<User> users = repository.getUsers();
		for(User u: activeUsers) {
			if(u.getUsername().equals(user.getUsername()))
				return false;
		}
		for(User u: users) {
			if(u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword())) {
				activeUsers.add(user);
				return true;
			}
		}
		return false;				
	}

	@POST
	@Path("/logout")
	public boolean logout(@Context HttpServletRequest request, User user) {
		for (User temp : activeUsers) {
			if (temp.getUsername().equals(user.getUsername())) {
				activeUsers.remove(temp);
//				request.getSession().invalidate();
				return true;
			}
		}
		return false;
	}

	@GET
	@Path("/users")
	public List<User> getUsers(){		
		return repository.getUsers();
	}

}
