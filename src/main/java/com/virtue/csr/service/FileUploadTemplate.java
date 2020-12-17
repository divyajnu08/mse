/**
 * 
 */
package com.virtue.csr.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.ClientSessionOptions;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.TransactionBody;

/**
 *
 */
public abstract class FileUploadTemplate<T> {
	
	private static final Logger logger = LoggerFactory.getLogger(FileUploadTemplate.class);
	
	private MongoTemplate mongoTemplate;
	private String collection;
	private File inputFile;
	
	protected FileUploadTemplate(MongoTemplate mongoTemplate2, String coll, File file) {
		mongoTemplate=mongoTemplate2;
		collection=coll;
    	inputFile=file;
	}

	long process(boolean append) throws IOException {
		if (isValid(inputFile))
			return upload(append);
		logger.error("Unable to load , Input file not valid.");
		return 0;
	}

	long upload(boolean append) throws FileNotFoundException, IOException {
		List<T> objList = parse(inputFile);
		TransactionOptions txnOptions = TransactionOptions.builder().readPreference(ReadPreference.primary())
				.readConcern(ReadConcern.LOCAL).writeConcern(WriteConcern.MAJORITY).build();
		ClientSessionOptions options = ClientSessionOptions.builder().causallyConsistent(true)
				.defaultTransactionOptions(txnOptions).build();
		ClientSession clientSession = mongoTemplate.getMongoDbFactory().getSession(options);
		TransactionBody<Integer> txnBody = new TransactionBody<Integer>() {
			public Integer execute() {
				if(!append)mongoTemplate.dropCollection(collection);
				Collection<T> savedObjects=mongoTemplate.insert(objList,collection);
				return savedObjects.size();
			}
		};
		try {
			return clientSession.withTransaction(txnBody, txnOptions);
		}finally {
			clientSession.close();
		}
	}
	
	abstract boolean isValid(File file) throws FileNotFoundException, IOException;

	abstract List<T> parse(File file) throws FileNotFoundException, IOException;

}
