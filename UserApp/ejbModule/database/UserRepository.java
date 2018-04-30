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
		  user = new User(obj.getString("username"), obj.getString("password"));
		  users.add(user);
		}
		
		return users;
		
	}

}
