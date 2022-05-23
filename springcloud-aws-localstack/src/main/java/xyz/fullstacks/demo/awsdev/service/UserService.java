package xyz.fullstacks.demo.awsdev.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectResult;

import xyz.fullstacks.demo.awsdev.model.User;

public interface UserService {
	
	User saveUser(User user);

    List<User> findAll();

    List<User> findByFirstName(String firstName);

    List<User> findByLastName(String lastName);

    List<String> getAllDocumentsFromBuckets(String bucketName);

    Bucket createBucket(String bucketName);

    PutObjectResult uploadDocument(MultipartFile file, String bucketName) throws IOException;

}