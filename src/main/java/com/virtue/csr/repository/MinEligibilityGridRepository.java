/**
 * 
 */
package com.virtue.csr.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.virtue.csr.model.MinEligibilityGrid;

@Repository
public interface MinEligibilityGridRepository extends MongoRepository<MinEligibilityGrid,Object> {
	
	Optional<MinEligibilityGrid> findByHouseholdSizeAndMaxAGIGreaterThanEqual(int hSize,double income);
	Optional<MinEligibilityGrid> findTopByOrderByHouseholdSizeDesc();
	
}
