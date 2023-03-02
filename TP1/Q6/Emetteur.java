import javax.jms.*;
import javax.naming.*;

public class Emetteur {

    public static void main(String[] args) throws NamingException, JMSException {
        if (args.length < 1) {
            System.err.println("Usage: java Emetteur <message> <destinataire>");
            return;
        }
        String messageText = args[0];
        String destinataire = args[1] != null ? args[1] : "user";

        InitialContext messaging = new InitialContext();
        QueueConnectionFactory connectionFactory = (QueueConnectionFactory) messaging.lookup("jms/CFTP1");
        Queue queue = (Queue) messaging.lookup("jms/TP1");
        QueueConnection connection = connectionFactory.createQueueConnection();
        QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        QueueSender sender = session.createSender(queue);

        
        Queue tempQueue = session.createTemporaryQueue(); 
        MessageConsumer responseConsumer = session.createConsumer(tempQueue); 

        connection.start();

        TextMessage message = session.createTextMessage();
        message.setText(messageText);
        if (destinataire != null) {
            message.setStringProperty("destinataire", destinataire);
        }

        // Q6
        message.setJMSReplyTo(tempQueue);
        message.setJMSCorrelationID("#idJMS-" + destinataire);
        sender.send(message);
        System.out.println("Message envoyé : " + message.getText());
        
        Message response = responseConsumer.receive();
        if (response != null) {
            System.out.println("Réponse reçue : " + ((TextMessage) response).getText());
        }

        connection.close();
    }
}
