package xyz.fullstacks.demo.awsdev.config;

import static xyz.fullstacks.demo.awsdev.config.AWSConfigConstants.ACCESS_KEY;
import static xyz.fullstacks.demo.awsdev.config.AWSConfigConstants.ENDPOINT;
import static xyz.fullstacks.demo.awsdev.config.AWSConfigConstants.REGION_DEFAULT;
import static xyz.fullstacks.demo.awsdev.config.AWSConfigConstants.SECRET_KEY;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

@Configuration
//@ConfigurationProperties(prefix = "cloud.aws.s3")
public class AWSS3Config {
	@Bean
	public AmazonS3 amazonS3() {
		return AmazonS3Client.builder()
				// 为AWS S3客户端，提供endpoint url 和 the region, 
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(ENDPOINT, REGION_DEFAULT)) 
				// 通过aws configure配置的ACCESS_KEY 和 SECRET_KEY
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY)))
				.withPathStyleAccessEnabled(true) 
				.build();
	}
}
