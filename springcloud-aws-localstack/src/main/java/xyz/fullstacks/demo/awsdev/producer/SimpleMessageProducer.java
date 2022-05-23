package xyz.fullstacks.demo.awsdev.producer;

import static xyz.fullstacks.demo.awsdev.config.AWSConfigConstants.ORDER_CREATED_TOPIC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.awspring.cloud.messaging.core.NotificationMessagingTemplate;
import lombok.extern.slf4j.Slf4j;
import xyz.fullstacks.demo.awsdev.model.Event;

@Slf4j
@Service
public class SimpleMessageProducer {

	@Autowired
	private NotificationMessagingTemplate notificationMessagingTemplate;

	public void publish(Event event) {
		log.info(String.valueOf(event));
		notificationMessagingTemplate.convertAndSend(ORDER_CREATED_TOPIC, event);
	}
}