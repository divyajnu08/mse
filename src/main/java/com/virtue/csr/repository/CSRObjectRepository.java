package com.virtue.csr.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.virtue.csr.model.CSRData;

@Repository
public interface CSRObjectRepository extends MongoRepository<CSRData, String> {
	Optional<CSRData> findOneByStudentId(String student_id);
}
