package controller;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.context.SessionScoped;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import database.UserRepository;
import message.UserResponseJMS;
import model.ActiveUsers;
import model.User;
import service.ClusterService;

@Path("/user")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

	@EJB
	UserRepository repository;
	@EJB
	ClusterService clusterService;

	@EJB
	ActiveUsers activeUsers;
	
	@EJB
	UserResponseJMS userresponseJMS;

	@SessionScoped
	User activeUser;


	@GET
	@Path("/active")
	public List<User> activeUsers() {
		return activeUsers.getActiveUsers();
	}

	@POST
	@Path("/login")
	public boolean login(User user) {
//		Host host = new Host(request.getRemoteAddr() + ":" + request.getServerPort(), "");
//		host.setAlias(clusterService.getHost(host));
//		user.setHost(host);

//		System.out.println(user);
		List<User> users = repository.getUsers();
		for (User temp : activeUsers()) {
			if (temp.getUsername().equals(user.getUsername())) {
				activeUser = user;
				return false;
			}
		}
		for (User u : users) {
			if (u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword())) {
				activeUsers.login(user);
				activeUser = user;
				System.out.println("U i User + " + user + "           " + u.getUsername());
				userresponseJMS.sendRequest("LoginNotify," + u.getUsername());
				return true;
			}
		}
		activeUser = null;
		return false;
	}

	@GET
	@Path("/logout/{username}")
	public boolean logout(@PathParam("username") String username) {
		for (User temp : activeUsers.getActiveUsers()) {
			if (temp.getUsername().equals(username)) {
				activeUsers.logout(temp);
				activeUser = null;
				userresponseJMS.sendRequest("LogoutNotify," + temp.getUsername());
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

	@GET
	@Path("/{username}")
	public User getUser(@PathParam("username") String username) {
		System.out.println("Dobijeni korisnik:" + repository.getUser(username));
		return repository.getUser(username);
	}

	@POST
	@Path("/register")
	public int register(User user) {
		repository.save(user);
		return HttpServletResponse.SC_OK;
	}

	@GET
	@Path("/search/{criteria}")
	public List<User> search(@PathParam("criteria")String criteria) {
		List<User> users = getUsers();
		List<User> ret = new ArrayList<>();

		for(User user: users) {
			if(user.getUsername().contains(criteria) || user.getFirstName().contains(criteria)
					|| user.getLastName().contains(criteria))
				ret.add(user);
		}
		return ret;
	}

}
