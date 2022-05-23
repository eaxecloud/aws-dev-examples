package xyz.fullstacks.demo.awsdev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.internal.SdkInternalMap;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.DeleteTopicResult;
import com.amazonaws.services.sns.model.ListSubscriptionsByTopicResult;
import com.amazonaws.services.sns.model.ListSubscriptionsResult;
import com.amazonaws.services.sns.model.ListTopicsResult;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.amazonaws.services.sns.model.UnsubscribeRequest;
import com.amazonaws.services.sns.model.UnsubscribeResult;

import xyz.fullstacks.demo.awsdev.config.AWSConfigConstants;

@RestController
@RequestMapping("/sns")
public class SnsController {
    @Autowired
    private AmazonSNS amazonSNS;
    
    @Value("${cloud.aws.sns.order-created.arn}")
    private String topicArn;
    
    @PostMapping(value = "/create-topic/{topicName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateTopicResult> createTopic(@PathVariable String topicName) {
    	CreateTopicResult createTopic = amazonSNS.createTopic(topicName);
        return ResponseEntity.ok().body(createTopic);
    }
    
    @GetMapping(value = "/list-topics", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ListTopicsResult> listTopics() {
    	ListTopicsResult listTopics = amazonSNS.listTopics();
        return ResponseEntity.ok().body(listTopics);
    }
    
    @GetMapping(value = "/publish-message", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PublishResult> publishMessage(@RequestParam String message) {
    	// 使用PublishRequest参数发布消息可以设置更多的消息属性，比如设置group等等
    	PublishResult publish = amazonSNS.publish(topicArn, message);
        return ResponseEntity.ok().body(publish);
    }
    
    /**
     * 等同于命令：aws sns subscribe...
     * aws --endpoint-url http://localhost:4566 sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:topic --protocol email --notification-endpoint example@email.com
     * 
     * @param topicArn
     * @param protocol
     * 
     * @param endpoint
     * @return
     */
    @GetMapping(value = "/subscribe", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubscribeResult> subscribe(@RequestParam String topicArn, @RequestParam String protocol, @RequestParam String endpoint ) {
//    	SubscribeResult subscribe = amazonSNS.subscribe(topicArn, protocol, endpoint);
    	
    	SubscribeRequest request = new SubscribeRequest();
    	request.setTopicArn(topicArn);
    	request.setProtocol(protocol);
    	request.setEndpoint(endpoint);
    	SubscribeResult subscribe = amazonSNS.subscribe(request);
        return ResponseEntity.ok().body(subscribe);
    }
    
	/**
     * 等同于命令：aws sns subscribe...
     * aws --endpoint-url http://localhost:4566 sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:topic --protocol email --notification-endpoint example@email.com
     * 
     * @param topicArn
     * @param protocol
     * @param endpoint
     * @param deadLetterQueueArn
     * @return
     */
    @GetMapping(value = "/subscribe-with-dlq", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubscribeResult> subscribeWithDlq(@RequestParam String topicArn, @RequestParam String protocol, @RequestParam String endpoint, @RequestParam String deadLetterQueueArn) {
    	String dlq = (deadLetterQueueArn == null || deadLetterQueueArn.trim().length() == 0) ? AWSConfigConstants.DLQ_DEFAULT : deadLetterQueueArn;
    	SdkInternalMap<String, String> attributes = new SdkInternalMap<String, String>();
    	attributes.put("RedrivePolicy", dlq);
    	
    	SubscribeRequest request = new SubscribeRequest();
    	request.setTopicArn(topicArn);
    	request.setProtocol(protocol);
    	request.setEndpoint(endpoint);
    	request.setAttributes(attributes);
    	
    	SubscribeResult subscribe = amazonSNS.subscribe(request);
        return ResponseEntity.ok().body(subscribe);
    }
    
    /**
     * aws --endpoint-url http://localhost:4566 sns unsubscribe --subscription-arn arn:aws:sns:us-east-1:000000000000:topic:6b6dccc9-baf6-4cbb-9d40-e55d737fb0b9
     * @param topicArn
     * @return
     */
    @GetMapping(value = "/unsubscribe/{topicArn}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UnsubscribeResult> unsubscribe(@PathVariable String topicArn) {
    	UnsubscribeRequest request = new UnsubscribeRequest();
    	request.setSubscriptionArn(topicArn);
    	UnsubscribeResult unsubscribe = amazonSNS.unsubscribe(request);
        return ResponseEntity.ok().body(unsubscribe);
    }
    
    /**
     * 等同于：aws --endpoint-url http://localhost:4566 sns list-subscriptions
     * @return
     */
    @GetMapping(value = "/list-subscriptions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ListSubscriptionsResult> listSubscriptions() {
    	ListSubscriptionsResult listSubscriptions = amazonSNS.listSubscriptions();;
        return ResponseEntity.ok().body(listSubscriptions);
    }
    
    /**
     * 等同于：aws --endpoint-url http://localhost:4566 sns list-subscriptions
     * @return
     */
    @GetMapping(value = "/list-subscriptions/{topicArn}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ListSubscriptionsByTopicResult> listSubscriptions(@PathVariable String topicArn) {
    	ListSubscriptionsByTopicResult listSubscriptionsByTopic = amazonSNS.listSubscriptionsByTopic(topicArn);
        return ResponseEntity.ok().body(listSubscriptionsByTopic);
    }
    
    /**
     * aws --endpoint-url http://localhost:4566 sns delete-topic --topic-arn arn:aws:sns:us-east-1:000000000000:topic
     * @param topicName
     * @return
     */
    @DeleteMapping(value = "/delete-topic/{topicName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeleteTopicResult> deleteTopic(@PathVariable String topicName) {
    	DeleteTopicResult deleteTopic = amazonSNS.deleteTopic(topicName);
        return ResponseEntity.ok().body(deleteTopic);
    }
}
