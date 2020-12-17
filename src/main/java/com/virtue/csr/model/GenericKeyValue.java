/**
 * 
 */
package com.virtue.csr.model;

/**
 *
 */
public class GenericKeyValue<A,B> {
	private A key;
	private B value;
	
	public A getKey() {
		return key;
	}
	public void setKey(A key) {
		this.key = key;
	}
	public B getValue() {
		return value;
	}
	public void setValue(B value) {
		this.value = value;
	}
}
