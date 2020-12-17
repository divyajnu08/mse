package com.virtue.csr.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="cust_grant_rule")
public class CustGrantRule {
	
	private int custid;
	private List<Long> ques_maxRule;
	private List<Long> ques_minRule;
	private List<Long> ques_otherRule;
	private boolean meritMandatory;
	
	public int getCustid() {
		return custid;
	}
	public void setCustid(int custid) {
		this.custid = custid;
	}
	public List<Long> getQues_maxRule() {
		return ques_maxRule;
	}
	public void setQues_maxRule(List<Long> ques_maxRule) {
		this.ques_maxRule = ques_maxRule;
	}
	public List<Long> getQues_minRule() {
		return ques_minRule;
	}
	public void setQues_minRule(List<Long> ques_minRule) {
		this.ques_minRule = ques_minRule;
	}
	public boolean isMeritMandatory() {
		return meritMandatory;
	}
	public void setMeritMandatory(boolean meritMandatory) {
		this.meritMandatory = meritMandatory;
	}
	public List<Long> getQues_otherRule() {
		return ques_otherRule;
	}
	public void setQues_otherRule(List<Long> ques_otherRule) {
		this.ques_otherRule = ques_otherRule;
	}

}
