package uk.singular.dfs.provider.sandbox.jms.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class LogMessageSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${jms.queue}")
    private String QUEUE_NAME;

    public void sendMessage(String message) {
        jmsTemplate.convertAndSend(QUEUE_NAME,message);
    }

}