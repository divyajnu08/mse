package com.virtue.csr.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.virtue.csr.model.Question;

public interface QuestionsReporsitory extends MongoRepository<Question, Long>{
	List<Question> findByCustid(long l);
	List<Question> findByCustidAndActive(long l,boolean active);
	
	@Query("{'textid' : { '$in' : ?0 }}")
	List<Question> findAllByTextids(List<Long> textids);
	
	Question findByTextid(long textid);
}
