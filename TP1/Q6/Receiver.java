import javax.jms.*;
import javax.naming.*;

public class Receiver {

    public static void main(String[] args) throws NamingException, JMSException, InterruptedException {

        String[] destinataires = args.length > 0 ? args : new String[] { null };

        InitialContext messaging = new InitialContext();
        QueueConnectionFactory connectionFactory = (QueueConnectionFactory) messaging.lookup("jms/CFTP1");
        Queue queue = (Queue) messaging.lookup("jms/TP1");
        QueueConnection connection = connectionFactory.createQueueConnection();
        QueueSession session = connection.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);

        connection.start();

        for (String destinataire : destinataires) {
            QueueReceiver receiver;
            if (destinataire != null) {
                receiver = session.createReceiver(queue, "destinataire='" + destinataire + "'");
            } else {
                receiver = session.createReceiver(queue);
            }
            connection.start();

            while (true) {
                TextMessage message = (TextMessage) receiver.receive();
                if (message == null) {
                    break;
                }
                if (destinataire != null ) {
                    System.out.println("Message reçu de " + destinataire + " : " + message.getText());
                } else {
                    System.out.println("Message reçu : " + message.getText());
                }
                Destination replyTo = message.getJMSReplyTo();
                if ( replyTo != null ){
                    String correlationId = message.getJMSCorrelationID();
                    QueueSender replySender = session.createSender((Queue) replyTo);
                    TextMessage replyMessage = session.createTextMessage("Message bien reçu !");
                    replyMessage.setJMSCorrelationID(correlationId);
                    replySender.send(replyMessage);
                    replySender.close();
                }

                message.acknowledge();
            }

            receiver.close();
        }
        connection.close();
    }
}
