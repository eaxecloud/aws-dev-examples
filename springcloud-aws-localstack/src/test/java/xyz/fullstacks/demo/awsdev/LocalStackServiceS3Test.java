package xyz.fullstacks.demo.awsdev;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amazonaws.services.s3.AmazonS3;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalStackServiceS3Test extends BaseIntegrationTest{

    @Autowired
    private AmazonS3 amazonS3;

    private final static String BUCKET_NAME = UUID.randomUUID().toString();
    
    @Test
    public void createBucket() {
    	amazonS3.createBucket(BUCKET_NAME);
    	
//    	assertThat(amazonS3.listBuckets().stream().filter(bucket -> bucket.getName().equals(BUCKET_NAME)).count() == 1);
    	
        assertThat(amazonS3.listBuckets().stream().anyMatch(bucket -> bucket.getName().equals(BUCKET_NAME)));

    }
    
    @Test
    public void deleteBucket() {
    	amazonS3.deleteBucket(BUCKET_NAME);
    	
//    	assertThat(amazonS3.listBuckets().stream().filter(bucket -> bucket.getName().equals(BUCKET_NAME)).count() == 0);
    	
        assertThat(!amazonS3.listBuckets().stream().anyMatch(bucket -> bucket.getName().equals(BUCKET_NAME)));

    }
    
    @BeforeAll
    public static void init() {
        System.out.println("初始化数据");
    }

    @AfterAll
    public static void cleanup() {
        System.out.println("清理数据");
    }

}
