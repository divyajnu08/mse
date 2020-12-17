package com.virtue.csr.customer;

import java.util.List;

import com.virtue.csr.model.Question;

public class CustomerResponse {
	private long id;
	private String name;
	private long netPrice; 
	List<Question> questions;
	private long coa;
	private long graduation_rate;

	
	public long getGraduation_rate() {
		return graduation_rate;
	}
	public void setGraduation_rate(long graduation_rate) {
		this.graduation_rate = graduation_rate;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCoa() {
		return coa;
	}
	public void setCoa(long coa) {
		this.coa = coa;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getNetPrice() {
		return netPrice;
	}
	public void setNetPrice(long netPrice) {
		this.netPrice = netPrice;
	}
	public List<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
}
