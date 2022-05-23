package xyz.fullstacks.demo.awsdev;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import xyz.fullstacks.demo.awsdev.config.AWSConfigConstants;

@ContextConfiguration(initializers = BaseIntegrationTest.Initializer.class)
public abstract class BaseIntegrationTest {

	private final static DockerImageName localstackImage = DockerImageName.parse("localstack/localstack:0.14.2");

	private final static LocalStackContainer.Service[] services = { LocalStackContainer.Service.S3,
			LocalStackContainer.Service.DYNAMODB, LocalStackContainer.Service.SNS, LocalStackContainer.Service.SQS };

	public final static LocalStackContainer localStackContainer = new LocalStackContainer(localstackImage)
			.withServices(services)
			.withEnv(AWSConfigConstants.HOSTNAME_EXTERNAL_KEY, AWSConfigConstants.DOCKER_HOST_IP)	
			.withEnv(AWSConfigConstants.DOCKER_HOST, AWSConfigConstants.DOCKER_HOST_URL);

	static {

		localStackContainer.start();

	}

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

			TestPropertyValues values = TestPropertyValues.of(
					"aws.dynamodb.endpoint=" + localStackContainer
							.getEndpointConfiguration(LocalStackContainer.Service.DYNAMODB).getServiceEndpoint(),
					"aws.s3.endpoint=" + localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.S3)
							.getServiceEndpoint(),
					"aws.sns.endpoint=" + localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.SNS)
							.getServiceEndpoint(),
					"aws.sqs.endpoint=" + localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.SQS)
							.getServiceEndpoint());

			values.applyTo(configurableApplicationContext);
		}
	}
}