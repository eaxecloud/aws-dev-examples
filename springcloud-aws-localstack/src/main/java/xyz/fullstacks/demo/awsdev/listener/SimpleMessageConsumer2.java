package xyz.fullstacks.demo.awsdev.listener;

import static xyz.fullstacks.demo.awsdev.config.AWSConfigConstants.ORDER_QUEUE_2;

import org.springframework.stereotype.Controller;

import io.awspring.cloud.messaging.config.annotation.NotificationMessage;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import xyz.fullstacks.demo.awsdev.model.Event;

@Slf4j
@Controller
public class SimpleMessageConsumer2 implements MessageConsumer {

    @Override
    @SqsListener(value = ORDER_QUEUE_2, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void consume(@NotificationMessage Event event) {
        if (event != null) {
            log.info("Received order event for consumer 2: " + event);
        }
    }
}