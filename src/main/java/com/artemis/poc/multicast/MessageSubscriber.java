package com.artemis.poc.multicast;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;

public class MessageSubscriber {

	public static void main(final String[] args) throws Exception {
		Connection connection = null;
		InitialContext initialContext = null;
		try {
			initialContext = new InitialContext();

			Topic topic = (Topic) initialContext.lookup("topic/exampleTopic");

			ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");

			connection = cf.createConnection();
			connection.setClientID("durable-client1");
			connection.start();
			
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			TopicSubscriber subscriber = session.createDurableSubscriber(topic, "subscriber-1");
			for (;;) {

				TextMessage messageReceived = (TextMessage) subscriber.receive();
				System.out.println("Received message: subscriber-1 ** " + messageReceived.getText());

				if (messageReceived.getText().equalsIgnoreCase("HALT")) {
					break;
				}
				Thread.sleep(1000);
			}
			subscriber.close();
			session.unsubscribe("subscriber-1");
		} finally {
			if (connection != null) {
				// Step 19. Be sure to close our JMS resources!
				connection.close();
			}
			if (initialContext != null) {
				// Step 20. Also close the initialContext!
				initialContext.close();
			}
		}
	}
}
