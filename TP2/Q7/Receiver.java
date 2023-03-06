import javax.ejb.*;
import javax.jms.*;

@MessageDriven(mappedName="jms/TP1")
public class Receiver implements MessageListener {

    public void onMessage(Message message) {
        try {
            TextMessage msg = (TextMessage) message;
            System.out.println(msg.getText());
        } catch (JMSException e) {

        }
    }
}
