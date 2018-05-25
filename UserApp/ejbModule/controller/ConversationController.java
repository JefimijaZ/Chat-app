package controller;

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

import database.ConversationRepository;
import model.Message;
import model.MessageDTO;

@Path("/conversation")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConversationController {
	
	@EJB
	ConversationRepository conversationRepository;
	
	@GET
	@Path("/all/{username}")
	public List<MessageDTO> getFriends(@PathParam("username") String username) {
		return conversationRepository.getUserConversations(username);
		
	}
	@POST
	@Path("/add")
	public List<MessageDTO> addConversation(Message msg){
		return conversationRepository.addConversation(msg.getUsername(), msg.getFriendsUsername(), msg.getUsersMessage());
	}
}
