import javax.jms.*;
import javax.naming.*;

public class Receiver {

    public static void main(String[] args) throws NamingException, JMSException, InterruptedException {
        if (args.length != 2) {
            System.out.println("Usage: Receiver <waitTime> <receiverName>");
            return;
        }

        int waitTime = Integer.parseInt(args[0]);
        String receiverName = args[1];
        
        InitialContext messaging = new InitialContext();
        QueueConnectionFactory connectionFactory = (QueueConnectionFactory) messaging.lookup("jms/CFTP1");
        Queue queue = (Queue) messaging.lookup("jms/TP1");
        QueueConnection connection = connectionFactory.createQueueConnection();
        QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        QueueSender sender = session.createSender(queue);
        QueueReceiver receiver = session.createReceiver(queue);

        connection.start();

        while (true) {
            TextMessage msg = (TextMessage) receiver.receive();
            System.out.println("Received message for receiver " + receiverName + ": " + msg.getText());
            Thread.sleep(waitTime);
        }

        connection.close();
    }
}
