package xyz.fullstacks.demo.awsdev.controller;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;

import xyz.fullstacks.demo.awsdev.model.Event;
import xyz.fullstacks.demo.awsdev.model.EventData;
import xyz.fullstacks.demo.awsdev.model.EventType;
import xyz.fullstacks.demo.awsdev.producer.SimpleMessageProducer;

@Controller
public class OrderController {

    private final AtomicInteger atomicInteger = new AtomicInteger();
    @Autowired
    SimpleMessageProducer simpleMessageProducer;
    
    @Autowired
    private AmazonS3 amazonS3;

    @GetMapping(value = "/create-order", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> createOrder() {
        Event event = createOrderEvent();
        simpleMessageProducer.publish(event);
        return ResponseEntity.ok().body("Published message on SNS");
    }
    
    @GetMapping(value = "/list-buckets", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Bucket>> listBuckets() {
    	List<Bucket> buckets = amazonS3.listBuckets();
        return ResponseEntity.ok().body(buckets);
    }
    
    @PostMapping(value = "/create-bucket/{bucketName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Bucket> createBucket(@PathVariable String bucketName) {
    	Bucket createdBucket = amazonS3.createBucket(bucketName);
        return ResponseEntity.ok().body(createdBucket);
    }

    private Event createOrderEvent() {
        return Event.builder()
                .eventId(UUID.randomUUID().toString())
                .occurredAt(Instant.now().toString())
                .version(String.valueOf(atomicInteger.getAndIncrement()))
                .data(EventData
                        .builder()
                        .eventType(EventType.ORDER_CREATED)
                        .orderId(UUID.randomUUID().toString())
                        .owner("SampleProducer")
                        .build())
                .build();
    }
}