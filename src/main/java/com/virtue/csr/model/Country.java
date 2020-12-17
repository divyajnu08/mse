package com.virtue.csr.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.virtue.csr.dto.KeyValue;

@Document(collection = "countries")
public class Country {

	@Id
	private Integer id;
	private String name;
	private String iso3;
	private String iso2;
	private String phone_code;
	private String capital;
	private String currency;
	private List<KeyValue> states = new ArrayList<>();
	
	public Integer getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getIso3() {
		return iso3;
	}
	public String getIso2() {
		return iso2;
	}
	public String getPhone_code() {
		return phone_code;
	}
	public String getCapital() {
		return capital;
	}
	public String getCurrency() {
		return currency;
	}
	public List<KeyValue> getStates() {
		return states;
	}

}

