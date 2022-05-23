package xyz.fullstacks.demo.awsdev.repository;

import java.util.List;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import xyz.fullstacks.demo.awsdev.model.User;

@EnableScan
public interface UserRepository extends CrudRepository<User, String> {
	List<User> findByLastName(String lastName);

	List<User> findByFirstName(String firstName);
}
