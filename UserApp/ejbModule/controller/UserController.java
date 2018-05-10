package controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.context.SessionScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import database.UserRepository;
import model.ActiveUsers;
import model.User;

@Path("/user")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

	@EJB
	UserRepository repository;

	@EJB
	ActiveUsers activeUsers;

	@SessionScoped
	User activeUser;

	@GET
	@Path("/active")
	public List<User> activeUsers() {
		return activeUsers.getActiveUsers();
	}

	@POST
	@Path("/login")
	public boolean login(@Context HttpServletRequest request, User user) {
		System.out.println(user);
		List<User> users = repository.getUsers();
		for (User u : activeUsers.getActiveUsers()) {
			if (u.getUsername().equals(user.getUsername())) {
				activeUser = user;
				System.out.println(false);
				return false;
			}
		}
		for (User u : users) {
			if (u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword())) {
				activeUsers.login(user);
				activeUser = user;
				System.out.println(true);
				return true;
			}
		}
		activeUser = null;
		System.out.println(false);
		return false;
	}

	@POST
	@Path("/logout")
	public boolean logout(@Context HttpServletRequest request, User user) {
		for (User temp : activeUsers.getActiveUsers()) {
			if (temp.getUsername().equals(user.getUsername())) {
				activeUsers.logout(temp);
				activeUser = null;
				return true;
			}
		}
		return false;
	}

	@GET
	@Path("/users")
	public List<User> getUsers() {
		return repository.getUsers();
	}

	@PUT
	@Path("/save")
	public int save(User user) {
		//repository.save(user);
		return HttpServletResponse.SC_OK;
	}

}
