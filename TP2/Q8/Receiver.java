import javax.ejb.*;
import javax.jms.*;

@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(
            propertyName = "messageSelector",
            propertyValue = "destinataire='Youri'"
        )
    },
    mappedName = "jms/TP1")
public class Receiver implements MessageListener {

    public void onMessage(Message message) {
        try {
            TextMessage msg = (TextMessage) message;
            System.out.println(msg.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
