package com.virtue.csr.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.virtue.csr.model.MasterConfig;

@Repository
public interface MasterConfigRepository extends MongoRepository<MasterConfig, String>{
	 
	public List<MasterConfig> findByGroupKeyAndCustidAndCampid(String groupKey, Integer univId, Integer campId);
	public List<MasterConfig> findByGroupKeyAndCustid(String groupKey, Integer univId);
	public List<MasterConfig> findByGroupKey(String groupKey);
	public List<MasterConfig> findByKey(String key);
	
	//TitleLikeOrDescriptionLike
	
}
