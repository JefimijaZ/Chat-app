package controllers;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/usersEndPoint")
public class UserController {
	
	@OnMessage
    public String onMessage(String message) {
        if (message != null) {
            if (message.equals("HELLO SERVER")) {
                return "{\"hello\": \"Hello, if you need the ATP list just press the button ...\"}";
            } else if (message.equals("allUsers")) {
                return "[{\"name\": \"Nadal, Rafael (ESP)\", \"email\": \"nadalrafael@gmail.com\", \"rank\": \"1\"},"
                        + "{\"name\": \"Djokovic, Novak (SRB)\", \"email\": \"djokovicnovak@gmail.com\", \"rank\": \"2\"},"
                        + "{\"name\": \"Federer, Roger (SUI)\", \"email\": \"federerroger@gmail.com\", \"rank\": \"3\"},"
                        + "{\"name\": \"Wawrinka, Stan (SUI)\", \"email\": \"wawrinkastan@gmail.com\", \"rank\": \"4\"},"
                        + "{\"name\": \"Ferrer, David (ESP)\", \"email\": \"ferrerdavid@gmail.com\", \"rank\": \"5\"}]";
            }
        }
        return "";
    }
	@OnOpen
    public void onOpen() {
       System.out.println("otvara ");
    }
    @OnClose
    public void onClose() {
    	System.out.println("zatvara ");
    }
}
