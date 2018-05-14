package message;

import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

@Stateless
public class UserRequestsJMS {

	
	public void sendRequest(String message) {
		try {
			System.out.println("LOGIN SEND REQUEST");
			System.out.println(message);
			System.out.println("LOGIN SEND REQUEST");
			Context context = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory) context.lookup("jms/RemoteConnectionFactory");
			final Queue queue = (Queue) context.lookup("jms/queue/mojQueue");
			context.close();
			Connection connection = cf.createConnection();
			final QueueSession session = (QueueSession) connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			connection.start();
			QueueSender sender = session.createSender(queue);
			TextMessage msg = session.createTextMessage();
			
			msg.setText(message);
			System.out.println("MSG  " + msg.getText());
			sender.send(msg);	
			Thread.sleep(1000);
			sender.close();
			session.close();
			connection.stop();
			connection.close();
			

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
