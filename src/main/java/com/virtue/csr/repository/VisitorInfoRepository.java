package com.virtue.csr.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.virtue.csr.model.VisitorInfo;

@Repository
public interface VisitorInfoRepository extends MongoRepository<VisitorInfo, String>{
	
	Optional<VisitorInfo> findByEmailId(String emailid);
}
