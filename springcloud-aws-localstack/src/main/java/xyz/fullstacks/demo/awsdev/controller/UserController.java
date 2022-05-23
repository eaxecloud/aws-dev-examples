package xyz.fullstacks.demo.awsdev.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import xyz.fullstacks.demo.awsdev.model.User;
import xyz.fullstacks.demo.awsdev.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	
	@PostMapping
    public ResponseEntity<User> newUser(@Valid @RequestBody User user) {
		return ResponseEntity.ok(userService.saveUser(user));
    }
	
    @GetMapping
    public ResponseEntity<List<User>> findUsers(@Nullable @RequestParam("firstName") String firstName, @Nullable @RequestParam("lastName") String lastName) {

        if (null != firstName) {
            return ResponseEntity.ok(userService.findByFirstName(firstName));
        } else if (null != lastName){
            return ResponseEntity.ok(userService.findByLastName(lastName));
        }
        return ResponseEntity.ok(userService.findAll());
    }


    @GetMapping("documents")
    public List<String> getAllDocuments(@Param("bucketName") String bucketName) {
        return userService.getAllDocumentsFromBuckets(bucketName);
    }

    @PutMapping("documents")
    public ResponseEntity saveDocument(@RequestParam(value = "file") MultipartFile file, @Param("bucketName") String bucketName) throws IOException {

    	userService.uploadDocument(file, bucketName);

        return ResponseEntity.created(URI.create("SOME-LOCATION")).build();

    }

    @PostMapping("create-bucket")
    public ResponseEntity<String> create(@Param("bucketName") String bucketName) {
    	userService.createBucket(bucketName);

        return ResponseEntity.ok(bucketName);

    }
}
