package xyz.fullstacks.demo.awsdev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteQueueResult;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

@RestController
@RequestMapping("/sqs")
public class SqsController {
	@Autowired
	private AmazonSQS amazonSQS;
	
	@Autowired
    private AmazonS3 amazonS3;

	@PostMapping(value = "/create-queue/{queueName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreateQueueResult> createQueue(@PathVariable String queueName) {
		CreateQueueResult createQueue = amazonSQS.createQueue(queueName);
		return ResponseEntity.ok().body(createQueue);
	}
	
	@DeleteMapping(value = "/delete-queue/{queueName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DeleteQueueResult> deleteQueue(@PathVariable String queueName) {
		DeleteQueueResult deleteQueue = amazonSQS.deleteQueue(queueName);
		return ResponseEntity.ok().body(deleteQueue);
	}
	
	@GetMapping(value = "/list-queues", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ListQueuesResult> listQueues() {
		ListQueuesResult listQueues = amazonSQS.listQueues();
		return ResponseEntity.ok().body(listQueues);
	}

	@GetMapping(value = "/send-message", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SendMessageResult> sendMessage(@RequestParam String queueUrl, @RequestParam String message) {
		SendMessageResult sendMessage = amazonSQS.sendMessage(queueUrl, message);
		return ResponseEntity.ok().body(sendMessage);
	}
	
	@GetMapping(value = "/send-message-attr", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SendMessageResult> sendMessageWithAttributes(@RequestParam String queueUrl, @RequestParam String message, @RequestParam String messageAttributes) {
		final SendMessageRequest sendMessageRequest = new SendMessageRequest();
		sendMessageRequest.withMessageBody(message);
		sendMessageRequest.withQueueUrl(queueUrl);
		sendMessageRequest.withMessageBody(messageAttributes);
		sendMessageRequest.setMessageGroupId("FIFO-GROUP");
		
		SendMessageResult sendMessage = amazonSQS.sendMessage(sendMessageRequest);
		return ResponseEntity.ok().body(sendMessage);
	}
}
