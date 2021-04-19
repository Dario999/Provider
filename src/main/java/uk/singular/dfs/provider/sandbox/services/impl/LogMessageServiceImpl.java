package uk.singular.dfs.provider.sandbox.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uk.singular.dfs.provider.sandbox.model.messages.LogMessage;
import uk.singular.dfs.provider.sandbox.repository.LogMessagesRepository;
import uk.singular.dfs.provider.sandbox.services.LogMessageService;

import java.util.List;

@Service
public class LogMessageServiceImpl implements LogMessageService {

    private final LogMessagesRepository logMessagesRepository;

    private final ObjectMapper mapper;

    private static final Logger LOG = LoggerFactory.getLogger(AuditMessageServiceImpl.class);


    public LogMessageServiceImpl(LogMessagesRepository logMessagesRepository) {
        this.logMessagesRepository = logMessagesRepository;
        mapper = new ObjectMapper();
    }

    @Override
    public List<LogMessage> findAll() {
        List<LogMessage> logMessages = logMessagesRepository.findAll();
        for(LogMessage message : logMessages){
            String messageContent = new String(message.getContent());
            message.setContentUncompressed(messageContent.substring(1,messageContent.length()-1));
        }
        return logMessages;
    }

    @Override
    public LogMessage findById(Integer id) {
        LogMessage logMessage =  logMessagesRepository.findById(id).orElse(null);
        if(logMessage != null){
            String messageContent = new String(logMessage.getContent());
            logMessage.setContentUncompressed(messageContent.substring(1,messageContent.length()-1));
            return logMessage;
        }else{
            return null;
        }
    }

    @Override
    public LogMessage save(String message) {
        LogMessage logMessage = new LogMessage();
        try{
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
            byte[] strToBytes = json.getBytes();
            logMessage.setContent(strToBytes);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return logMessagesRepository.save(logMessage);
    }

    @Override
    public LogMessage save(LogMessage logMessage) {
        return logMessagesRepository.save(logMessage);
    }
}
