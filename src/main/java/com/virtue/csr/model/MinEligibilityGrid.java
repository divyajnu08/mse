/**
 * 
 */
package com.virtue.csr.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 */
@Document(collection="min_eligibillity_grid")
public class MinEligibilityGrid {
	
	Integer householdSize;
	Double maxAGI;
	
	public Integer getHouseholdSize() {
		return householdSize;
	}
	public void setHoseholdSize(Integer hoseholdSize) {
		this.householdSize = hoseholdSize;
	}
	public Double getMaxAmaxAGIdjustedGrossIncome() {
		return maxAGI;
	}
	public void setMaxAdjustedGrossIncome(Double maxAGI) {
		this.maxAGI = maxAGI;
	}
	
}
