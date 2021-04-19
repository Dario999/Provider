package uk.singular.dfs.provider.sandbox.jms.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import uk.singular.dfs.provider.sandbox.model.messages.Message;

@Component
public class MessageSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${jms.queue.lock}")
    private String QUEUE_NAME;

    public void sendMessage(Message message) {
        jmsTemplate.convertAndSend(QUEUE_NAME,message);
    }

}