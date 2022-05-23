package xyz.fullstacks.demo.awsdev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class SpringcloudAwsMessageApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringcloudAwsMessageApplication.class, args);
	}
}
