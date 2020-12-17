package com.virtue.csr.repository;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.virtue.csr.model.UserSubscription;


public interface UserSubscriptionRepository extends MongoRepository<UserSubscription,Integer>{
	
	@Query(value="{ $and : [{ 'token' : ?0 , 'status' : 'ACTIVE' , 'startDate' : { $lte : ?1 } , 'endDate' : { $gte : ?1 }  }]}")
	UserSubscription findValidToken(String token,LocalDateTime accessTime);
	
	UserSubscription findByEmailIdAndStatus(String emailId, String status);
	
	UserSubscription findByEmailIdAndTokenAndStatus(String emailId, String token, String status);

}
