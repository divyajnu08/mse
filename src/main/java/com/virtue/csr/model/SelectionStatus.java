/**
 * 
 */
package com.virtue.csr.model;

/**
 *
 */
public enum SelectionStatus {
	
	SELECTED("Selected"),
	NOT_SELECTED("Not Selected");
	
	String value;

	/**
	 * @param string
	 */
	SelectionStatus(String string) {
		this.value=string;
	}
	
	public String toString() {
		return this.value;
	}

}
