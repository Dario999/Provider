package uk.singular.dfs.provider.sandbox.jms.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import uk.singular.dfs.provider.sandbox.exceptions.InvalidMessageException;
import uk.singular.dfs.provider.sandbox.model.messages.Message;
import uk.singular.dfs.provider.sandbox.services.MessageService;

@Component
@EnableJms
@Configuration
public class MessageQueueListener {

    @Autowired
    private MessageService messageService;

    private static final Logger LOG = LoggerFactory.getLogger(MessageQueueListener.class);


    @JmsListener(destination = "${jms.queue.lock}",containerFactory = "defaultJmsListenerContainerFactory")
    public void receiveMessage(Message message) {
        try {
            messageService.sendMessage(message);
        }catch(InvalidMessageException e){
            LOG.warn(e.getMessage());
        }
    }

}