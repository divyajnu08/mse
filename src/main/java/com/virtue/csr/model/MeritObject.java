package com.virtue.csr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "merit_grid")
public class MeritObject {

	@Id
	@JsonIgnore
	private String id;
	@JsonIgnore
	private Integer custid;
	@JsonIgnore
	private Integer campid;
	private String custName;
	private String groupKey;
	private String key;
	@JsonIgnore
	@Field(value ="field1_min")
	private Double field1Min; //for GPA
	@JsonIgnore
	@Field(value ="field1_max")
	private Double field1Max;
	@JsonIgnore
	@Field(value ="field2_min")
	private Double field2Min; //for ACT
	@JsonIgnore
	@Field(value ="field2_max")
	private Double field2Max;
	@Field(value = "outstateValue")
	private Double value;
	
	public String getId() {
		return id;
	}
	public Integer getCustid() {
		return custid;
	}
	public Integer getCampid() {
		return campid;
	}
	public String getCustName() {
		return custName;
	}
	public String getGroupKey() {
		return groupKey;
	}
	public String getKey() {
		return key;
	}
	public Double getField1Min() {
		return field1Min;
	}
	public Double getField1Max() {
		return field1Max;
	}
	public Double getField2Min() {
		return field2Min;
	}
	public Double getField2Max() {
		return field2Max;
	}
	public Double getValue() {
		return value;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCustid(Integer custid) {
		this.custid = custid;
	}
	public void setCampid(Integer campid) {
		this.campid = campid;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public void setField1Min(Double field1Min) {
		this.field1Min = field1Min;
	}
	public void setField1Max(Double field1Max) {
		this.field1Max = field1Max;
	}
	public void setField2Min(Double field2Min) {
		this.field2Min = field2Min;
	}
	public void setField2Max(Double field2Max) {
		this.field2Max = field2Max;
	}
	public void setValue(Double value) {
		this.value = value;
	}
}
