package xyz.fullstacks.demo.awsdev.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.ListVersionsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.VersionListing;

@RestController
@RequestMapping("/s3")
public class S3Controller {

    @Autowired
    private AmazonS3 amazonS3;
    
    @GetMapping(value = "/list-buckets", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Bucket>> listBuckets() {
    	List<Bucket> buckets = amazonS3.listBuckets();
        return ResponseEntity.ok().body(buckets);
    }
    
    @GetMapping(value = "/list-document-from-bucket/{bucketName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> listAllDocumentsFromBucket(String bucketName) {
    	if (!amazonS3.doesBucketExistV2(bucketName)) {
    		throw new NoSuchElementException ("Bucket name is not available") ; 
    	}
    	
    	List<String> documents = amazonS3.listObjectsV2(bucketName).getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
        return ResponseEntity.ok().body(documents);
    }
    
    @PostMapping(value = "/create-bucket/{bucketName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Bucket> createBucket(@PathVariable String bucketName) {
    	Bucket createdBucket = amazonS3.createBucket(bucketName);
        return ResponseEntity.ok().body(createdBucket);
    }
    
    @PostMapping(value = "/create-bucket-expire/{bucketName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Bucket> createBucketWithExpiration(@PathVariable String bucketName) {
    	final BucketLifecycleConfiguration.Rule expirationRule = new BucketLifecycleConfiguration.Rule();
        expirationRule.withExpirationInDays(14).withStatus("Enabled");
        final BucketLifecycleConfiguration lifecycleConfig = new BucketLifecycleConfiguration().withRules(expirationRule);
    	
    	Bucket createdBucket = amazonS3.createBucket(bucketName);
    	amazonS3.setBucketLifecycleConfiguration(bucketName, lifecycleConfig);
    	
        return ResponseEntity.ok().body(createdBucket);
    }
    
    @PostMapping(value = "/upload-document", produces = MediaType.APPLICATION_JSON_VALUE, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<PutObjectResult> uploadDocument(@RequestPart MultipartFile file, String bucketName) throws IllegalStateException, IOException {
    	
    	String tempFileName = UUID.randomUUID() + file.getName();
        File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + tempFileName);
        file.transferTo(tempFile);
		PutObjectResult putObject = amazonS3.putObject(bucketName, UUID.randomUUID() + file.getName(), tempFile);
        tempFile.deleteOnExit();
        
        return ResponseEntity.ok().body(putObject);
    }
    
    /**
     * 删除bucket和期内所有的内容
     * @param bucketName
     * @return
     */
    @DeleteMapping(value = "/delete-bucket/{bucketName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteBucketAndAllContent(@PathVariable String bucketName) {
        ObjectListing objectListing = amazonS3.listObjects(bucketName);

        while (true) {
            for (S3ObjectSummary objectSummary : objectListing
                    .getObjectSummaries()) {
            	amazonS3.deleteObject(bucketName, objectSummary.getKey());
            }

            if (objectListing.isTruncated()) {
                objectListing = amazonS3.listNextBatchOfObjects(objectListing);
            } else {
                break;
            }
        }

        final VersionListing list = amazonS3.listVersions(
                new ListVersionsRequest().withBucketName(bucketName));

        for (S3VersionSummary s : list.getVersionSummaries()) {
        	amazonS3.deleteVersion(bucketName, s.getKey(), s.getVersionId());
        }

        amazonS3.deleteBucket(bucketName);
        
        return ResponseEntity.ok().body(String.format("Deleted Bucket: %s and all content", bucketName));
    }

}