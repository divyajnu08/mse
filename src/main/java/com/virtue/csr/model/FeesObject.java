package com.virtue.csr.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.virtue.csr.constants.FeesItems;
import com.virtue.csr.constants.HousingOptions;

@Document(collection = "fees_items")
public class FeesObject {
	
	@Id
	@JsonIgnore
	private String id;
	@JsonIgnore
	private Integer custid;
	@JsonIgnore
	private Integer campid;
	private HousingOptions housingOption;
	private String descr;
	private FeesItems key;
	private Double value;
	private String year;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getCustid() {
		return custid;
	}
	public void setCustid(Integer custid) {
		this.custid = custid;
	}
	public Integer getCampid() {
		return campid;
	}
	public void setCampid(Integer campid) {
		this.campid = campid;
	}
	public HousingOptions getHousingOption() {
		return housingOption;
	}
	public void setHousingOption(HousingOptions housingOption) {
		this.housingOption = housingOption;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public FeesItems getKey() {
		return key;
	}
	public void setKey(FeesItems key) {
		this.key = key;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
}
