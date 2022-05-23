package xyz.fullstacks.demo.awsdev.config;

import org.springframework.beans.factory.annotation.Value;

public class AWSConfigConstants {
	public static final String DOCKER_HOST = "DOCKER_HOST";
	public static final String DOCKER_HOST_URL = "http://192.168.146.179:2375";
	
	public static final String HOSTNAME_EXTERNAL_KEY = "HOSTNAME_EXTERNAL";
	public static final String DOCKER_HOST_IP = "192.168.146.179";
	
	public static final String ACCESS_KEY = "foo";
	public static final String SECRET_KEY = "bar";

	@Value("${aws.service-endpoint}")
	public static final String ENDPOINT = "http://192.168.146.179:4566";

	public static final String REGION_TOKYO = "ap-northeast-1";

	@Value("${cloud.aws.region.static}")
	public static final String REGION_VIRGINIA = "us-east-1";

	public static final String REGION_DEFAULT = REGION_VIRGINIA;

	@Value("${cloud.aws.sns.order-created.arn}")
	public static final String ORDER_CREATED_TOPIC = "order-created-topic";

	@Value("${cloud.aws.sqs.order-queue.url}")
	public static final String ORDER_QUEUE = "order-queue";

	@Value("${cloud.aws.sqs.order-queue-2.url}")
	public static final String ORDER_QUEUE_2 = "order-queue-2";
	
	@Value("${cloud.aws.sns.dlq-default.arn}")
	public static final String DLQ_DEFAULT = "dead-letter-quque-default";
	
}