package uk.singular.dfs.provider.sandbox.services;

import uk.singular.dfs.provider.sandbox.model.messages.LogMessage;

import java.util.List;

public interface LogMessageService {

    List<LogMessage> findAll();
    LogMessage findById(Integer id);
    LogMessage save(String message);
    LogMessage save(LogMessage logMessage);

}
