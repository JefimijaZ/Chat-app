package message;

import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import model.User;

@Stateless
public class UserResponseJMS {
	
	public void sendRequest(String message) {
		try {
			System.out.println("OGIN USER RESPONSE JMS  MESSAGE  " + message);
			Context context = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory) context.lookup("jms/RemoteConnectionFactory");
			final Queue queue = (Queue) context.lookup("jms/queue/mojTopic");
			context.close();
			Connection connection = cf.createConnection();
			final QueueSession session = (QueueSession) connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			connection.start();
			QueueSender sender = session.createSender(queue);
			TextMessage msg = session.createTextMessage();
			
			msg.setText(message);			
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
//	public void sendRequestObject(User message) {
//		try {
//			System.out.println("OGIN USER RESPONSE JMS  MESSAGE  " + message);
//			Context context = new InitialContext();
//			ConnectionFactory cf = (ConnectionFactory) context.lookup("jms/RemoteConnectionFactory");
//			final Queue queue = (Queue) context.lookup("jms/queue/mojTopic");
//			context.close();
//			Connection connection = cf.createConnection();
//			final QueueSession session = (QueueSession) connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//			connection.start();
//			QueueSender sender = session.createSender(queue);
//			ObjectMessage msg = session.createObjectMessage();
//			
//			msg.setObject(message);			
//			sender.send(msg);		
//			Thread.sleep(1000);
//			sender.close();
//			session.close();
//			
//			connection.stop();
//			connection.close();
//			
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}


}
