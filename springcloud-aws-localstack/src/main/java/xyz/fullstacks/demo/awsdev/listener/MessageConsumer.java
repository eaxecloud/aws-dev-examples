package xyz.fullstacks.demo.awsdev.listener;

import io.awspring.cloud.messaging.config.annotation.NotificationMessage;
import xyz.fullstacks.demo.awsdev.model.Event;

public interface MessageConsumer {

    /**
     * 消费队列中收到的消息。
     * 为了保证正确的反序列化，需要加@NotificationMessage，从SNS消息中提取Event对象。
     * {@link io.awspring.cloud.messaging.config.annotation.NotificationMessage}
     *
     * @param event
     */
    public void consume(@NotificationMessage Event event);
}
