package xyz.fullstacks.demo.awsdev.config;

import static xyz.fullstacks.demo.awsdev.config.AWSConfigConstants.ACCESS_KEY;
import static xyz.fullstacks.demo.awsdev.config.AWSConfigConstants.SECRET_KEY;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;

import io.awspring.cloud.messaging.core.NotificationMessagingTemplate;

/**
 *  
 * Spring Cloud会自动配置AmazonSNSClient，但是配置AmazonSNSClient时会需要AWS credentials和实际资源的endpoint
 * 
 * @author Frank
 *
 */
@Configuration
public class AWSSnsConfig {

    @Bean
    public NotificationMessagingTemplate notificationMessagingTemplate(AmazonSNS amazonSNS) {
        return new NotificationMessagingTemplate(amazonSNS);
    }

    @Bean
    @Primary
    public AmazonSNS amazonSNS(final AwsClientBuilder.EndpointConfiguration endpointConfiguration) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        return AmazonSNSClientBuilder
                .standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}