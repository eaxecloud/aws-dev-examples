package xyz.fullstacks.demo.awsdev.config;

import static xyz.fullstacks.demo.awsdev.config.AWSConfigConstants.ACCESS_KEY;
import static xyz.fullstacks.demo.awsdev.config.AWSConfigConstants.ENDPOINT;
import static xyz.fullstacks.demo.awsdev.config.AWSConfigConstants.REGION_DEFAULT;
import static xyz.fullstacks.demo.awsdev.config.AWSConfigConstants.SECRET_KEY;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.awspring.cloud.messaging.config.QueueMessageHandlerFactory;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import io.awspring.cloud.messaging.support.NotificationMessageArgumentResolver;

/**
 * Spring Cloud会自动配置AmazonSNSClient，但是配置AmazonSNSClient时会需要AWS
 * credentials和实际资源的endpoint
 * 
 * @author Frank
 *
 */
@Configuration
public class AWSSqsConfig {

	@Bean
	public AwsClientBuilder.EndpointConfiguration endpointConfiguration() {
		return new AwsClientBuilder.EndpointConfiguration(ENDPOINT, REGION_DEFAULT);
	}

	@Bean
	@Primary
	public AmazonSQSAsync amazonSQSAsync(final AwsClientBuilder.EndpointConfiguration endpointConfiguration) {
		BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
		return AmazonSQSAsyncClientBuilder.standard().withEndpointConfiguration(endpointConfiguration)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
	}

	/**
	 * QueueMessageHandlerFactory用于将从SQS中获取的字符串通过MessageConverter转换成实际需要的Object
	 * 
	 * @param messageConverter
	 * @return
	 */
	@Bean
	public QueueMessageHandlerFactory queueMessageHandlerFactory(MessageConverter messageConverter) {
		var factory = new QueueMessageHandlerFactory();
		factory.setArgumentResolvers(List.of(new NotificationMessageArgumentResolver(messageConverter)));
		return factory;
	}

	@Bean
	protected MessageConverter messageConverter(ObjectMapper objectMapper) {
		var converter = new MappingJackson2MessageConverter();
		converter.setObjectMapper(objectMapper);
		converter.setSerializedPayloadClass(String.class);
		converter.setStrictContentTypeMatch(false);
		return converter;
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean
	public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQS) {
		return new QueueMessagingTemplate(amazonSQS);
	}
}