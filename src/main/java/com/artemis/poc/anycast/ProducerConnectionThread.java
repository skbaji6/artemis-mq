package com.artemis.poc.anycast;

import javax.jms.Connection;
import javax.jms.Queue;
import javax.jms.Session;

class ProducerConnectionThread extends Thread {

	Connection connection;
	Queue queue;

	public ProducerConnectionThread(Connection connection, Queue queue) {
		this.connection = connection;
		this.queue = queue;
	}

	public void run() {
		try {
			for (int i = 0; i < 5; i++) {
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				ProducerSessionThread sessionthread = new ProducerSessionThread(session, queue,connection.toString(),i);
				sessionthread.start();
				//sessionthread.join();
			}
			Thread.sleep(2000);
		} catch (Exception e) {
			// Throwing an exception
			System.out.println("Exception is caught in ProducerConnectionThread");
		} finally {
			try {

				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}