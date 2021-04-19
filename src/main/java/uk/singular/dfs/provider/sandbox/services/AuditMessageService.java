package uk.singular.dfs.provider.sandbox.services;

import uk.singular.dfs.provider.sandbox.model.messages.AuditMessage;
import uk.singular.dfs.provider.sandbox.model.messages.Message;

public interface AuditMessageService {

    void saveAuditMessage(AuditMessage auditMessage);
    void saveMessage(Message message);
    byte[] getMessagesForEvent(Integer id);
    AuditMessage getMessage(Integer id);

}
