package xyz.fullstacks.demo.awsdev.listener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import xyz.fullstacks.demo.awsdev.model.OrderEvent;

/**
 * 这个listener演示，从Queue中 获取消息后，存入S3中
 * 
 * @author Frank
 *
 */
@Slf4j
@Component
public class SimpleMessageListener {
	private final AmazonS3 amazonS3;
	private final ObjectMapper objectMapper;
	private final String orderEventBucket;

	public SimpleMessageListener(@Value("${test.message.listener.bucket}") String orderEventBucket,
			AmazonS3 amazonS3, ObjectMapper objectMapper) {
		this.amazonS3 = amazonS3;
		this.objectMapper = objectMapper;
		this.orderEventBucket = orderEventBucket;
	}

	@SqsListener(value = "${test.message.listener.queue}")
	public void processMessage(@Payload OrderEvent orderEvent) throws JsonProcessingException {
		log.info("Incoming order: '{}'", orderEvent);

		amazonS3.putObject(orderEventBucket, orderEvent.getId(), objectMapper.writeValueAsString(orderEvent));

		log.info("Successfully uploaded order to S3");
	}
}
