import javax.jms.*;
import javax.naming.*;

public class Emetteur {

    public static void main(String[] args) throws NamingException, JMSException {
        if (args.length < 1) {
            System.err.println("Usage: java Emetteur <message>");
            return;
        }
        String messageText = args[0];
        
        InitialContext messaging = new InitialContext();
        QueueConnectionFactory connectionFactory = (QueueConnectionFactory) messaging.lookup("jms/CFTP1");
        Queue queue = (Queue) messaging.lookup("jms/TP1");
        QueueConnection connection = connectionFactory.createQueueConnection();
        QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        QueueSender sender = session.createSender(queue);
        connection.start();

        for (int i = 0; i < 5; i++) {
            TextMessage message = session.createTextMessage();
            message.setText(i + " : " + messageText);
            sender.send(message);
        }
        connection.close();

    }
}
