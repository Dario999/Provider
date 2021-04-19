package uk.singular.dfs.provider.sandbox.components;

import lombok.SneakyThrows;
import uk.singular.dfs.provider.sandbox.model.messages.Message;
import uk.singular.dfs.provider.sandbox.services.AuditMessageService;

public class Consumer implements Runnable{

    private final AuditMessageService auditMessageService;

    private Message message;

    public Consumer(Message message,AuditMessageService auditMessageService){
        this.message = message;
        this.auditMessageService = auditMessageService;
    }

    @SneakyThrows
    @Override
    public void run() {
        auditMessageService.saveMessage(message);
    }
}
