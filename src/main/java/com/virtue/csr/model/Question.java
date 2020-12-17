package com.virtue.csr.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="questions")
public class Question {
	
	@Id
	private long id;
	private int custid;
	private int campid;
	private String text;
	private int textid;
	private Double awardAmount;
	
	@Field(value="display")
	private String label;
	
	private String applyURL;
	private boolean active;
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getCustid() {
		return custid;
	}
	public void setCustid(int custid) {
		this.custid = custid;
	}
	public int getCampid() {
		return campid;
	}
	public void setCampid(int campid) {
		this.campid = campid;
	}
	public Double getAwardAmount() {
		return awardAmount;
	}
	public void setAwardAmount(Double awardAmount) {
		this.awardAmount = awardAmount;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getApplyURL() {
		return applyURL;
	}
	public void setApplyURL(String applyURL) {
		this.applyURL = applyURL;
	}
	public int getTextid() {
		return textid;
	}
	public void setTextid(int textid) {
		this.textid = textid;
	}

}
