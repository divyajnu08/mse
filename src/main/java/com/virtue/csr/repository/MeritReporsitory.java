package com.virtue.csr.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.virtue.csr.constants.MeritKeys;
import com.virtue.csr.model.MeritObject;

@Repository
public interface MeritReporsitory extends MongoRepository<MeritObject, String> {

	List<MeritObject> findByGroupKey(MeritKeys groupKey);
	List<MeritObject> findByGroupKeyAndCustidAndCampid(MeritKeys groupKey,int custid,int campid);
	List<MeritObject> findByGroupKeyAndCustid(MeritKeys groupKey,int custid);
	
}
