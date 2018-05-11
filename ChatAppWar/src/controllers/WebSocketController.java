package controllers;

import javax.ejb.EJB;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import model.Configuration;
import model.User;
import service.UserService;

@ServerEndpoint("/usersEndPoint")
public class WebSocketController {
	
	@EJB
	UserService userService;
	
	@OnMessage
    public String onMessage(String message) {      
		String request = message.split(",")[0];
		String username, password,firstName,lastName,host,response;
		User user;
		switch(request){
			case "Login": 
				if (!Configuration.masterAdress.equals(Configuration.localAdress)) {
					username = message.split(",")[1]; 
					password = message.split(",")[2];
					user = new User(username, password);
					System.out.println("ussao");
					response = ((ResteasyWebTarget)ClientBuilder.newClient().
							target("http://"+"localhost:8080"+"/UserAppWar/rest/user/login")).request().post(Entity.json(user)).readEntity(String.class);;	
							System.out.println("RESPONSE:" + response);
							userService.login(user);
					return response;
				}
				
				
				break;
			case "Register": 
				username = message.split(",")[1]; 
				password = message.split(",")[2];
				firstName = message.split(",")[3];
				lastName = message.split(",")[4];
				host = message.split(",")[5];
				user = new User(username, password);
				System.out.println("ussao");
				response = ((ResteasyWebTarget)ClientBuilder.newClient().
						target("http://"+"localhost:8080"+"/UserAppWar/rest/user/login")).request().post(Entity.json(user)).readEntity(String.class);;	
						System.out.println("RESPONSE:" + response);
				return response;
			
			
		}
        return "heej";
    }
	@OnOpen
    public void onOpen(Session session) {
		System.out.println("onOpen::" + session.getId());
       System.out.println("otvara ");
    }
    @OnClose
    public void onClose(Session session) {
    	
    	System.out.println("zatvara ");
    }
}