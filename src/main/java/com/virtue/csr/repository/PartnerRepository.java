/**
 * 
 */
package com.virtue.csr.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.virtue.csr.model.Partner;

/**
 *
 */
@Repository
public interface PartnerRepository extends MongoRepository<Partner,Object>{
	
	Optional<Partner> findByHost(String host);

}
