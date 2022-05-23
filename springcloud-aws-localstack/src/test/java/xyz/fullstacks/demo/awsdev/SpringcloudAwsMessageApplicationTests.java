package xyz.fullstacks.demo.awsdev;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@SpringBootTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS) // @TestInstance(TestInstance.Lifecycle.PER_METHOD)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@TestPropertySource("classpath:test.properties")
@DisplayName("Test SpringcloudAwsMessageApplicationTests")
class SpringcloudAwsMessageApplicationTests {

	@Test
	@Order(1)
	@DisplayName("Testing .contextLoads()")
	void contextLoads() {
		log.info("Testing contextLoads");
	}

}
