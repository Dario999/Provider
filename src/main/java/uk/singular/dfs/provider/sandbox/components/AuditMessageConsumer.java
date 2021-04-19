package uk.singular.dfs.provider.sandbox.components;

import org.springframework.stereotype.Component;
import uk.singular.dfs.provider.sandbox.model.messages.Message;
import uk.singular.dfs.provider.sandbox.services.AuditMessageService;

import java.util.concurrent.ExecutorService;

@Component
public class AuditMessageConsumer implements Runnable{

    private AuditMessageQueue auditMessageQueue;

    private final AuditMessageService auditMessageService;

    private final ExecutorService executorService;

    public AuditMessageConsumer(AuditMessageQueue auditMessageQueue, AuditMessageService auditMessageService, ExecutorService executorService) {
        this.auditMessageQueue = auditMessageQueue;
        this.auditMessageService = auditMessageService;
        this.executorService = executorService;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("consumer-thread");
        while (true) {
            if (!auditMessageQueue.isEmpty()) {
                Message message = auditMessageQueue.get();
                auditMessageService.saveMessage(message);
                //executorService.execute(new Consumer(message,auditMessageService));
            }
        }
    }

}
