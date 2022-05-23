package xyz.fullstacks.demo.awsdev.repository;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import xyz.fullstacks.demo.awsdev.model.User;

public interface PagingUserRepository extends PagingAndSortingRepository<User, String> {
	Page<User> findByLastName(String lastName, Pageable pageable);
	Page<User> findByFirstName(String firstName, Pageable pageable);

	@EnableScan
	@EnableScanCount
	Page<User> findAll(Pageable pageable);
}