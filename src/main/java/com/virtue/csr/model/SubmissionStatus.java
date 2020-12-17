/**
 * 
 */
package com.virtue.csr.model;

/**
 *
 */
public enum SubmissionStatus {
	
	SUBMITTED(1),
	NOT_SUBMITTED(0);
	
	int value;
	
	/**
	 * @param i
	 */
	SubmissionStatus(int i) {
		this.value=i;
	}

}
