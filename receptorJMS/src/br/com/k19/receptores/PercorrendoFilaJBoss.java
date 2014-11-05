package br.com.k19.receptores;

import java.util.Enumeration;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class PercorrendoFilaJBoss {
	public static void main(String[] args) throws Exception {
		//servicos de nomes JNDI
		Properties props = new Properties();
		
		props.setProperty("java.naming.factory.initial", 
				"org.jboss.naming.remote.client.InitialContextFactory");
		props.setProperty("java.naming.provider.url", "remote://localhost:4447");
		props.setProperty("java.naming.security.principal", "k19");
		props.setProperty("java.naming.security.credentials", "1234");
		
		InitialContext ic = new InitialContext(props);
		
		//fabrica de conexoes jms
		ConnectionFactory factory = (ConnectionFactory) ic.lookup("jms/K19Factory");
		
		//fila
		Queue queue = (Queue) ic.lookup("jms/queue/pedidos");
		
		//conexao jms
		Connection connection = factory.createConnection("k19", "1234");
		
		//sessao jms
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		QueueBrowser queueBrowser = session.createBrowser(queue);
		
		Enumeration<TextMessage> messages = queueBrowser.getEnumeration();
		
		int count = 1;
		
		while (messages.hasMoreElements()) {
			TextMessage message = (TextMessage) messages.nextElement();
			System.out.println(count + " : " + message.getText());
			count++;
		}
		
		//fechando
		queueBrowser.close();
		session.close();
		connection.close();
		
		System.out.println("FIM");
		
		System.exit(0);
	}
}