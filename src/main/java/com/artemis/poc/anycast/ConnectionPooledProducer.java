package com.artemis.poc.anycast;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.naming.InitialContext;

import org.apache.activemq.jms.pool.PooledConnectionFactory;

public class ConnectionPooledProducer {

	public static void main(final String[] args) throws Exception {
		InitialContext initialContext = null;
		try {
			initialContext = new InitialContext();
			
			Queue queue = (Queue) initialContext.lookup("queue/exampleQueue");
			ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
			PooledConnectionFactory pooledConnectionFactory=new PooledConnectionFactory();
			pooledConnectionFactory.setConnectionFactory(cf);
			pooledConnectionFactory.setMaxConnections(3);
	        pooledConnectionFactory.setMaximumActiveSessionPerConnection(2);
			
	        
			
			for(int i=0;i<5;i++){
				System.out.println("\n#########################################################################");
				System.out.println("########################## Start::Connection "+i+" #######################");
				System.out.println("#########################################################################");
				Connection connection = pooledConnectionFactory.createConnection();
				ProducerConnectionThread producerThread=new ProducerConnectionThread(connection,queue);
				producerThread.start();
				producerThread.join();
				System.out.println("#########################################################################");
				System.out.println("########################## End::Connection "+i+" #######################");
				System.out.println("#########################################################################\n");
				
			}
			
	} finally {
			if (initialContext != null) {
				initialContext.close();
			}
		}
	}
}
