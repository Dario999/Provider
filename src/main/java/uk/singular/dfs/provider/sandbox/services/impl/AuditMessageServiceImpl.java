package uk.singular.dfs.provider.sandbox.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uk.singular.dfs.provider.sandbox.model.messages.AuditMessage;
import uk.singular.dfs.provider.sandbox.model.messages.Message;
import uk.singular.dfs.provider.sandbox.repository.AuditMessageRepository;
import uk.singular.dfs.provider.sandbox.services.AuditMessageService;

import java.io.*;
import java.util.List;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class AuditMessageServiceImpl implements AuditMessageService {

    private final AuditMessageRepository auditMessageRepository;

    private final ObjectMapper mapper;

    private static final Logger LOG = LoggerFactory.getLogger(AuditMessageServiceImpl.class);

    public AuditMessageServiceImpl(AuditMessageRepository auditMessageRepository) {
        this.auditMessageRepository = auditMessageRepository;
        mapper = new ObjectMapper();
    }

    @Override
    public void saveAuditMessage(AuditMessage auditMessage) {
        auditMessageRepository.save(auditMessage);
    }


    @Override
    public void saveMessage(Message message) {
        AuditMessage auditMessage = new AuditMessage();
        auditMessage.setEventId(message.getEventId());
        //System.out.println(message);
        try{
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
            byte[] strToBytes = json.getBytes();
            auditMessage.setContent(strToBytes);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }

        auditMessageRepository.save(auditMessage);
    }

    @Override
    public byte[] getMessagesForEvent(Integer id) {
        List<AuditMessage> auditMessages = auditMessageRepository.findAllByEventId(id);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);


        for (AuditMessage auditMessage : auditMessages) {
            String fileName = "src/main/resources/messages/" + auditMessage.getId().toString() + ".txt";
            File file = new File(fileName);
            try(FileOutputStream outputStream = new FileOutputStream(fileName)){
                outputStream.write(auditMessage.getContent());
                zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                FileInputStream fileInputStream = new FileInputStream(file);
                IOUtils.copy(fileInputStream, zipOutputStream);
                fileInputStream.close();
                zipOutputStream.closeEntry();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                file.delete();
            }
        }
        try {
            zipOutputStream.finish();
            zipOutputStream.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
        IOUtils.closeQuietly(zipOutputStream);
        IOUtils.closeQuietly(bufferedOutputStream);
        IOUtils.closeQuietly(byteArrayOutputStream);


        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public AuditMessage getMessage(Integer id) {
        AuditMessage auditMessage = auditMessageRepository.findById(id).orElse(null);
        if(auditMessage != null){
            try {
                String messageContent = new String(auditMessage.getContent());
                auditMessage.setContentUncompressed(mapper.readValue(messageContent, Message.class));
            }catch (JsonProcessingException e){
                LOG.warn(e.getMessage());
            }
            return auditMessage;
        }else{
            return null;
        }
    }

}
