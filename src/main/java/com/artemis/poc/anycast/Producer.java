package com.artemis.poc.anycast;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class Producer {

	public static void main(final String[] args) throws Exception {
		Connection connection = null;
		InitialContext initialContext = null;
		try {
			initialContext = new InitialContext();
			
			Queue queue = (Queue) initialContext.lookup("queue/exampleQueue");
			ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
			connection = cf.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(queue);

			for (int i = 0; i < 10; i++) {
				TextMessage message = session.createTextMessage("This is a text message " + i);
				System.out.println("Sent message: " + message.getText());
				producer.send(message);
			}

		} finally {
			if (initialContext != null) {
				initialContext.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}
}
