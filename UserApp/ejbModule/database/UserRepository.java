package database;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.bson.Document;
import com.mongodb.client.MongoCursor;
import model.User;

@Stateless
public class UserRepository {
	
	@EJB
	DatabaseConnection connection;
	
	
	public List<User> getUsers(){
		MongoCursor<Document> collection =connection.getUsers().find().iterator();
		List<User> users = new ArrayList<User>();
		User user = null;
		Document obj = null;
		
		while (collection.hasNext()) {
		  obj = collection.next();
		  user = new User(obj.getString("username"),obj.getString("firstName"), obj.getString("lastName"), obj.getString("password"));
		  users.add(user);
		}		
		return users;		
	}
	
	public User getUser(String username) {
		List<User> users = getUsers();
		for(User u: users)
			if(u.getUsername().equals(username))
				return u;
		return null;
		
	}
	
	public List<User> getFriends(String username) {
		User user = getUser(username);
		List<String> friends = user.getFriends();
		List<User> users = new ArrayList<User>();
		for(String s: friends)
			users.add(getUser(s));
		return users;
	}
	
	

}
