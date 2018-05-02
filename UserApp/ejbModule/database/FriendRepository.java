package database;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;

import model.Friends;
import model.User;

@Stateless
public class FriendRepository {

	
	@EJB
	DatabaseConnection connection;
	
	@EJB
	UserRepository repository;
	
	
	public List<Friends> getFriends(){
		MongoCursor<Document> collection =connection.getFriends().find().iterator();
		List<Friends> users = new ArrayList<Friends>();
		Friends user = null;
		Document obj = null;
		
		while (collection.hasNext()) {
		  obj = collection.next();
		  user = new Friends(obj.getString("usernameOne"), obj.getString("usernameTwo"));
		  users.add(user);
		}		
		return users;		
	}
	
	public List<User> getUserFriends(String username){
		List<Friends> friends =getFriends();
		List<String > usernames = new ArrayList<>();
		List<User> users = new ArrayList<>();
		
		for(Friends friend: friends) {
			if(friend.getUserOne().equals(username) && !usernames.contains(friend.getUserTwo())) {
				users.add(repository.getUser(friend.getUserTwo()));
				usernames.add(friend.getUserTwo());
			}else if( friend.getUserTwo().equals(username) && !usernames.contains(friend.getUserOne())) {
				users.add(repository.getUser(friend.getUserOne()));
				usernames.add(friend.getUserOne());
			}
		}
		return users;		
	}
	
	public boolean addFriend(String userOne, String userTwo) {
		Document doc = new Document();
		doc.put("userOne", userOne);
		doc.put("userTwo", userTwo);
		connection.getUsers().insertOne(doc);
		return true;
	}
	
	public boolean removeFriend(String userOne, String userTwo) {
		List<Friends> friends =getFriends();
		BasicDBObject doc = new BasicDBObject();
		for(Friends friend: friends) {
			if(friend.getUserOne().equals(userOne) && friend.getUserTwo().equals(userTwo) ||
					friend.getUserOne().equals(userTwo) && friend.getUserTwo().equals(userOne)) {
					doc.put("userOne", userOne);
					doc.put("userTwo", userTwo);
					connection.getFriends().deleteOne(doc);
					return true;
			}
		}
		return false;
	}
	
}
