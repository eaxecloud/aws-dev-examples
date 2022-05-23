package xyz.fullstacks.demo.awsdev.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.fullstacks.demo.awsdev.model.User;
import xyz.fullstacks.demo.awsdev.repository.UserRepository;
import xyz.fullstacks.demo.awsdev.service.UserService;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final AmazonS3 amazonS3;

    private final UserRepository userRepository;
	
	
	@Override
	public User saveUser(User user) {
		log.info("Saving user.");
		return userRepository.save(user);
	}

	@Override
	public List<User> findAll() {
		log.info("Finding all users.");
		return StreamSupport.stream(userRepository.findAll().spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public List<User> findByFirstName(String firstName) {
		log.info("Finding user by first name [{}].", firstName);
		return userRepository.findByFirstName(firstName);
	}

	@Override
	public List<User> findByLastName(String lastName) {
		log.info("Finding user by last name [{}].", lastName);
		return userRepository.findByLastName(lastName);
	}

	@Override
	public List<String> getAllDocumentsFromBuckets(String bucketName) {
		log.info("Get all documents from bucket {}.", bucketName);
    	if (!amazonS3.doesBucketExistV2(bucketName)) {
    		throw new NoSuchElementException ("Bucket name is not available") ; 
    	}
    	
    	return amazonS3.listObjectsV2(bucketName).getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
	}

	@Override
	public Bucket createBucket(String bucketName) {
		log.info("Creating bucket [{}].", bucketName);
		return amazonS3.createBucket(bucketName);
	}

	@Override
	public PutObjectResult uploadDocument(MultipartFile file, String bucketName) throws IOException {
		log.info("Uploading document to bucket [{}].", bucketName);
    	String tempFileName = UUID.randomUUID() + file.getName();
        File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + tempFileName);
        file.transferTo(tempFile);
		PutObjectResult putObject = amazonS3.putObject(bucketName, UUID.randomUUID() + file.getName(), tempFile);
        tempFile.deleteOnExit();
        
        return putObject;
	}

}
