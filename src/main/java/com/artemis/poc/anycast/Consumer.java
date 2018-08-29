package com.artemis.poc.anycast;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class Consumer {

	public static void main(final String[] args) throws Exception {
		Connection connection = null;
		InitialContext initialContext = null;
		try {
			initialContext = new InitialContext();

			Queue queue = (Queue) initialContext.lookup("queue/exampleQueue");
			ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
			connection = cf.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			MessageConsumer messageConsumer = session.createConsumer(queue);
			connection.start();
			for (int i = 0; i < 10; i++) {
				TextMessage messageReceived = (TextMessage) messageConsumer.receive(5000);
				System.out.println("Received message: " + messageReceived.getText());
				Thread.sleep(2000);
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
