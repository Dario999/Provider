package uk.singular.dfs.provider.sandbox.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import uk.singular.dfs.provider.sandbox.model.messages.Message;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

@Component
public class AuditMessageQueue extends LinkedBlockingDeque<Message> {

    private static Logger logger = LoggerFactory.getLogger(AuditMessageQueue.class);

    @SuppressWarnings("unchecked")
    public boolean add(Message msg) {
        return this.offer(msg);
    }

    public Message get() {
        try {
            return this.poll(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("[{}] Exception: {}",
                    AuditMessageQueue.class.getSimpleName(), e.getStackTrace());
            Thread.currentThread().interrupt();
            return null;
        }
    }

}
