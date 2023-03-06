import javax.jms.*;
import javax.naming.*;

public class Emetteur {

    public static void main(String[] args) throws NamingException, JMSException {
        
        InitialContext messaging = new InitialContext();
        QueueConnectionFactory connectionFactory = (QueueConnectionFactory) messaging.lookup("jms/CFTP1");
        Queue queue = (Queue) messaging.lookup("jms/TP1");
        QueueConnection connection = connectionFactory.createQueueConnection();
        QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        QueueSender sender = session.createSender(queue);
        connection.start();

        TextMessage message = session.createTextMessage();
        message.setText("Hello World !");
        sender.send(message);

        connection.close();

    }
}
