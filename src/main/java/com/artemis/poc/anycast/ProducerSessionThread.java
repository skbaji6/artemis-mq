package com.artemis.poc.anycast;

import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class ProducerSessionThread extends Thread {
	Session session;
	Queue queue;
	int count;
	String connectionString;

	public ProducerSessionThread(Session session, Queue queue,String connectionString, int count) {
		this.session = session;
		this.queue = queue;
		this.count=count;
		this.connectionString=connectionString;
	}

	public void run() {
		try {
			MessageProducer producer = session.createProducer(queue);
			TextMessage message = session.createTextMessage("This is a text message " + count);
			producer.send(message);
			System.out.println(connectionString);
			System.out.println(session);
			System.out.println("Sent message: " + message.getText() + "\n");
		} catch (Exception e) {
			System.out.println("Exception is caught in ProducerSessionThread");
		} finally {
			try {

				if (session != null) {
					session.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
