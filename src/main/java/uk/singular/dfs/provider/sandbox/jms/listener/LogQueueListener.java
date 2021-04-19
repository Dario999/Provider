package uk.singular.dfs.provider.sandbox.jms.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import uk.singular.dfs.provider.sandbox.services.LogMessageService;

@Component
@EnableJms
@Configuration
public class LogQueueListener {

    @Autowired
    private LogMessageService logMessageService;

    @JmsListener(destination = "${jms.queue}",containerFactory = "defaultJmsListenerContainerFactory")
    public void receiveMessage(String message) {
//        try {
//            Thread currentThread = Thread.currentThread();
//            System.out.println(currentThread.getName());
//            currentThread.sleep(5000);
//        }catch (InterruptedException e){
//            System.out.println(e.getMessage());
//        }
        logMessageService.save(message);
    }

}
