package com.virtue.csr.dto;

import java.io.Serializable;

import org.springframework.util.ObjectUtils;

public class KeyValue implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String key;
	private String value;
	
	public KeyValue() {}
	
	public KeyValue(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	} 	
	
	@Override
	public String toString() {
		return "KeyValue [key=" + key + ", value=" + value + "]";
	}
	
	@Override
    public boolean equals(Object o) {
		if(o instanceof KeyValue) {
			KeyValue obj=(KeyValue)o;
			return ObjectUtils.nullSafeEquals(this.getKey(), obj.getKey())
					&& ObjectUtils.nullSafeEquals(this.getValue(),obj.getValue());
		}
		return false;
	}
}
