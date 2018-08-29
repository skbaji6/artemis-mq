package com.artemis.poc.multicast;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;

public class MessageSubscriber2 {

	public static void main(final String[] args) throws Exception {
		Connection connection = null;
		InitialContext initialContext = null;
		try {

			initialContext = new InitialContext();

			Topic topic = (Topic) initialContext.lookup("topic/exampleTopic");

			ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");

			connection = cf.createConnection();
			connection.setClientID("durable-client2");
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			TopicSubscriber subscriber = session.createDurableSubscriber(topic, "subscriber-2");
			for (;;) {

				TextMessage messageReceived = (TextMessage) subscriber.receive();
				System.out.println("Received message: subscriber-2 ** " + messageReceived.getText());

				if (messageReceived.getText().equalsIgnoreCase("HALT")) {
					break;
				}
				Thread.sleep(1500);
			}
			subscriber.close();

			session.unsubscribe("subscriber-2");
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
