package com.virtue.csr.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.virtue.csr.dto.KeyValue;

public class ChoiceInformation {
	
	Map<Integer,List<KeyValue>> customers;
	
	public Map<Integer,List<KeyValue>> getCustomers() {
		return customers;
	}
	public void setCustomers(Map<Integer,List<KeyValue>> customers) {
		this.customers = customers;
	}
	
	public void updateObject(ChoiceInformation otherObj) {
		this.customers=otherObj.customers;
	}

}
