package xyz.fullstacks.demo.awsdev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DeleteTableResult;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;

@RestController
@RequestMapping("/dynamodb")
public class DynamoDBController {

	@Autowired
	AmazonDynamoDB amazonDynamoDB;

	@GetMapping(value = "/list-tables", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ListTablesResult> listTables() {
		ListTablesResult listTables = amazonDynamoDB.listTables();
		return ResponseEntity.ok().body(listTables);
	}

	@PostMapping(value = "/create-table/{tableName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreateTableResult> createTable(@PathVariable String tableName) {
		AttributeDefinition[] ads = { new AttributeDefinition("id", ScalarAttributeType.S),
				new AttributeDefinition("firstName", ScalarAttributeType.S) };
		KeySchemaElement[] kses = { new KeySchemaElement("id", KeyType.HASH),
				new KeySchemaElement("firstName", KeyType.RANGE) };

		CreateTableRequest createTableRequest = new CreateTableRequest().withAttributeDefinitions(ads)
				.withKeySchema(kses).withProvisionedThroughput(new ProvisionedThroughput(10L, 10L))
				.withTableName(tableName);

		CreateTableResult createTable = amazonDynamoDB.createTable(createTableRequest);
		return ResponseEntity.ok().body(createTable);
	}

	@GetMapping(value = "/desc-table/{tableName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DescribeTableResult> inspectTable(@PathVariable String tableName) {
		DescribeTableResult describeTable = amazonDynamoDB.describeTable(tableName);
		return ResponseEntity.ok().body(describeTable);
	}
	
	@DeleteMapping(value = "/delete-table/{tableName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DeleteTableResult> deleteTable(@PathVariable String tableName) {
		DeleteTableResult deleteTable = amazonDynamoDB.deleteTable(tableName);
		return ResponseEntity.ok().body(deleteTable);
	}
}
