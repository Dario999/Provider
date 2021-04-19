package uk.singular.dfs.provider.sandbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import uk.singular.dfs.provider.sandbox.components.AuditMessageConsumer;
import uk.singular.dfs.provider.sandbox.components.AuditMessageQueue;
import uk.singular.dfs.provider.sandbox.services.AuditMessageService;

import java.util.concurrent.ExecutorService;

@SpringBootApplication
@EnableSwagger2
public class SandboxApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(SandboxApplication.class, args);
		AuditMessageQueue auditMessageQueue = ctx.getBean(AuditMessageQueue.class);
		AuditMessageService auditMessageService = ctx.getBean(AuditMessageService.class);
		ExecutorService executorService = ctx.getBean(ExecutorService.class);
		new Thread(new AuditMessageConsumer(auditMessageQueue,auditMessageService,executorService)).start();
	}

}
