package com.virtue.csr.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.virtue.csr.model.School;


@Repository
public interface SchoolRepository extends MongoRepository<School, String>{
	Optional<School> findByCeeb(Integer ceeb);
	Optional<School> findByName(String name);
	List<School> findByActive(Boolean active);
	List<School> findByState(String state);
}
