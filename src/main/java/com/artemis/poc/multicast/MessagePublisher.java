package com.artemis.poc.multicast;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;

public class MessagePublisher {

	public static void main(final String[] args) throws Exception {
		Connection connection = null;
		InitialContext initialContext = null;
		try {
			initialContext = new InitialContext();
			Topic topic = (Topic) initialContext.lookup("topic/exampleTopic");

			ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
			connection = cf.createConnection();
			connection.setClientID("durable-client");
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			MessageProducer messageProducer = session.createProducer(topic);
			for (int i = 0; i < 1000; i++) {
				TextMessage message1 = session.createTextMessage("Message "+i);
				messageProducer.send(message1);
				System.out.println("Sent message: " + message1.getText());
				Thread.sleep(500);
			}
			messageProducer.send(session.createTextMessage("HALT"));
		} finally {
			if (connection != null) {
				connection.close();
			}
			if (initialContext != null) {
				initialContext.close();
			}
		}
	}
}
