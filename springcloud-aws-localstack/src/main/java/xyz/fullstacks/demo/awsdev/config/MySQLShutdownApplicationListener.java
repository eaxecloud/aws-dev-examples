package xyz.fullstacks.demo.awsdev.config;



import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MySQLShutdownApplicationListener implements ApplicationListener<ContextClosedEvent> {
//	private static final Logger log = LoggerFactory.getLogger(Application.class);

	@Override
	public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
		log.info("AbandonedConnectionCleanupThread - Shutdown initiated...");
//		com.mysql.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
		log.info("AbandonedConnectionCleanupThread - Completed");
	}
}