package xyz.fullstacks.demo.awsdev;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalStackServiceDynamoDbTest extends BaseIntegrationTest{

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @BeforeAll
    public static void init() {
        System.out.println("初始化数据");
    }

    @AfterAll
    public static void cleanup() {
        System.out.println("清理数据");
    }

    @Test
    public void verifyTableName() {

        assertThat(amazonDynamoDB.listTables().getTableNames().contains("car"));

    }

    
}
