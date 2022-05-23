package xyz.fullstacks.demo.awsdev.config;

import static xyz.fullstacks.demo.awsdev.config.AWSConfigConstants.ACCESS_KEY;
import static xyz.fullstacks.demo.awsdev.config.AWSConfigConstants.ENDPOINT;
import static xyz.fullstacks.demo.awsdev.config.AWSConfigConstants.REGION_DEFAULT;
import static xyz.fullstacks.demo.awsdev.config.AWSConfigConstants.SECRET_KEY;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

@Configuration
@EnableDynamoDBRepositories(basePackages = "xyz.fullstacks.demo.awsdev.repository")
public class AWSDynamoDBConfig {

	@Bean
	public AmazonDynamoDB amazonDynamoDB() {
		return AmazonDynamoDBClientBuilder
				.standard()
//				.withCredentials(getCredentialsProvider())  //连接到Localstack 的Edge Server上不需要用户名和密码
				.withEndpointConfiguration(getEndpointConfiguration(ENDPOINT, REGION_DEFAULT))
//				.withRegion(REGION_DEFAULT)
				.build();
	}
	
//	@Bean
//    public DynamoDBMapperConfig dynamoDBMapperConfig() {
//        return DynamoDBMapperConfig.DEFAULT;
//    }

	private EndpointConfiguration getEndpointConfiguration(String endpoint, String region) {
		return new EndpointConfiguration(endpoint, region);
	}

	private AWSStaticCredentialsProvider getCredentialsProvider() {
		return new AWSStaticCredentialsProvider(getBasicAWSCredentials());
	}

	private BasicAWSCredentials getBasicAWSCredentials() {
		return new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
	}
}

//@Configuration
//@EnableDynamoDBRepositories(basePackageClasses = UserRepository.class)
//public class AWSDynamoDBConfig {
//	public AWSCredentialsProvider amazonAWSCredentialsProvider() {
//		return new AWSStaticCredentialsProvider(amazonAWSCredentials());
//	}
//
//	@Bean
//	public AWSCredentials amazonAWSCredentials() {
//		return new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
//	}
//
//	@Bean
//	public DynamoDBMapperConfig dynamoDBMapperConfig() {
//		return DynamoDBMapperConfig.DEFAULT;
//	}
//
//	@Bean
//	public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB, DynamoDBMapperConfig config) {
//		return new DynamoDBMapper(amazonDynamoDB, config);
//	}
//
//	@Bean
//	public AmazonDynamoDB amazonDynamoDB() {
//		return AmazonDynamoDBClientBuilder.standard().withCredentials(amazonAWSCredentialsProvider())
//				.withRegion(Regions.valueOf(REGION_DEFAULT)).build();
//	}
//}