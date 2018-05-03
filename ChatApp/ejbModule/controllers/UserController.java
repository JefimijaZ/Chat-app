package controllers;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/usersEndPoint")
public class UserController {
	
	@OnMessage
    public String onMessage(String message) {       
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
