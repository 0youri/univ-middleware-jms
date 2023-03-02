import javax.jms.*;
import javax.naming.*;

public class Receiver {

    public static void main(String[] args) throws NamingException, JMSException {
        InitialContext messaging = new InitialContext();
        QueueConnectionFactory connectionFactory = (QueueConnectionFactory) messaging.lookup("jms/CFTP1");
        Queue queue = (Queue) messaging.lookup("jms/TP1");
        QueueConnection connection = connectionFactory.createQueueConnection();
        QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        QueueSender sender = session.createSender(queue);
        connection.start();

        QueueReceiver receiver = session.createReceiver(queue);
        TextMessage msg = (TextMessage) receiver.receive();

        System.out.println(msg.getText());

        connection.close();
    }
}
