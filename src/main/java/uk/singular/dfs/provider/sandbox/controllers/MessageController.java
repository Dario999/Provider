package uk.singular.dfs.provider.sandbox.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uk.singular.dfs.provider.sandbox.exceptions.InvalidMessageException;
import uk.singular.dfs.provider.sandbox.jms.producer.LogMessageSender;
import uk.singular.dfs.provider.sandbox.jms.producer.MessageSender;
import uk.singular.dfs.provider.sandbox.model.messages.AuditMessage;
import uk.singular.dfs.provider.sandbox.model.messages.LogMessage;
import uk.singular.dfs.provider.sandbox.model.messages.Message;
import uk.singular.dfs.provider.sandbox.services.AuditMessageService;
import uk.singular.dfs.provider.sandbox.services.LogMessageService;
import uk.singular.dfs.provider.sandbox.services.MessageService;

import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;
    private final AuditMessageService auditMessageService;
    private final LogMessageService logMessageService;
    private final MessageSender messageSender;

    public MessageController(MessageService messageService, AuditMessageService auditMessageService,
                             LogMessageService logMessageService, MessageSender messageSender) {
        this.messageService = messageService;
        this.auditMessageService = auditMessageService;
        this.logMessageService = logMessageService;
        this.messageSender = messageSender;
    }

    @PostMapping("/send")
    @ApiOperation(value = "Send new message")
    public ResponseEntity<String> sendMessage(@RequestBody Message message) {
        messageSender.sendMessage(message);
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @ApiOperation(value = "Find all messages for event packed in one zip file",
            notes = "Provide an id to look up specific event")
    @RequestMapping(value = "/event/{id}", produces="application/zip",method = RequestMethod.GET)
    public byte[] getMessages(@ApiParam(value = "id of the event",required = true)
                              @PathVariable("id") Integer id) {
        return auditMessageService.getMessagesForEvent(id);
    }

    @ApiOperation(value = "Find message by id",
            notes = "Provide an id to look up specific message",
            response = AuditMessage.class)
    @GetMapping("/{id}")
    public AuditMessage getMessage(@ApiParam(value = "id of the message",required = true)
                                       @PathVariable("id") Integer id) {
        return auditMessageService.getMessage(id);
    }

    @ApiOperation(value = "Find log message by id",
            notes = "Provide an id to look up specific log message",
            response = LogMessage.class)
    @GetMapping("/logs/{id}")
    public LogMessage getLogMessage(@ApiParam(value = "id of the message",required = true)
                                   @PathVariable("id") Integer id){
        return logMessageService.findById(id);
    }

    @ApiOperation(value = "Find all log messages",
            notes = "Provide an id to look up specific log message",
            response = LogMessage[].class)
    @GetMapping("/logs/all")
    public List<LogMessage> getAllLogMessages(){
        return logMessageService.findAll();
    }

}
