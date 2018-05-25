package database;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.bson.Document;

import com.mongodb.client.MongoCursor;

import model.Message;
import model.MessageDTO;
import model.User;

@Stateless
public class ConversationRepository {

	@EJB
	DatabaseConnection connection;
	
	@EJB
	UserRepository repository;
	
	public List<Message> getConversations(){
		MongoCursor<Document> collection =connection.getMessages().find().iterator();
		List<Message> msgs = new ArrayList<Message>();
		Message message = null;
		Document obj = null;
		while (collection.hasNext()) {
			  obj = collection.next();
			  message = new Message(obj.getString("username"), obj.getString("friendsUsername"), obj.getString("userMessage"));
			  msgs.add(message);
			}		
			return msgs;	
	}
	public List<MessageDTO> getUserConversations(String username) {
		List<Message> messages =getConversations();
		List<MessageDTO> request = new ArrayList<MessageDTO>();
		User user = new User();
		User friend = new User();
		
		for(Message message : messages){
			if(message.getUsername().equals(username)){
				user = repository.getUser(message.getUsername());
				friend = repository.getUser(message.getFriendsUsername());
				request.add(new MessageDTO(user, friend, message.getUsersMessage()));
			}else if(message.getFriendsUsername().equals(username)){
				user = repository.getUser(message.getUsername());
				friend = repository.getUser(message.getFriendsUsername());
				request.add(new MessageDTO(user, friend, message.getUsersMessage()));
			}
		}
		return request;
	}

	public List<MessageDTO> addConversation(String username, String friendsUsername, String usersMessage) {
		Document doc = new Document();
		doc.put("username", username);
		doc.put("friendsUsername", friendsUsername);
		doc.put("userMessage", usersMessage);
		connection.getMessages().insertOne(doc);
		return getUserConversations(username);
		
	}
	
}
