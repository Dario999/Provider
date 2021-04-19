package uk.singular.dfs.provider.sandbox.services;

import uk.singular.dfs.provider.sandbox.exceptions.InvalidMessageException;
import uk.singular.dfs.provider.sandbox.model.messages.Message;

public interface MessageService {

    void sendMessage(Message message) throws InvalidMessageException;

}
