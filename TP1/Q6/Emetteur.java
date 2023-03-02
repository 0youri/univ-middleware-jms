import javax.jms.*;
import javax.naming.*;

public class Emetteur {

    public static void main(String[] args) throws NamingException, JMSException {
        if (args.length < 1) {
            System.err.println("Usage: java Emetteur <message> <destinataire>");
            return;
        }
        String messageText = args[0];
        String destinataire = args.length > 1 ? args[1] : null;

        InitialContext messaging = new InitialContext();
        QueueConnectionFactory connectionFactory = (QueueConnectionFactory) messaging.lookup("jms/CFTP1");
        Queue queue = (Queue) messaging.lookup("jms/TP1");
        QueueConnection connection = connectionFactory.createQueueConnection();
        QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        QueueSender sender = session.createSender(queue);

        
        Queue temporaryQueue = session.createTemporaryQueue();
        QueueSender replySender = session.createSender(null);

        connection.start();

        for (int i = 0; i < 3; i++) {
            TextMessage message = session.createTextMessage();
            message.setText(i + " : " + messageText);
            if (destinataire != null) {
                message.setStringProperty("destinataire", destinataire);
            }
            message.setJMSReplyTo(temporaryQueue);
            String correlationID = i + "-message";

            message.setJMSCorrelationID(correlationID);
           
            sender.send(message);
            TextMessage replyMsg = (TextMessage) session.receive();
            if (replyMsg != null) {
                System.out.println("Réponse reçue : " + replyMsg.getText());
            }
            replySender.close();
            
        }


        
        connection.close();
    }
}
