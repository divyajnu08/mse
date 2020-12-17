package com.virtue.csr.model;

import java.util.List;

import com.virtue.csr.constants.Countries;
import com.virtue.csr.constants.MaritalStatus;

public class BasicInformation {

	private String name;
	private String email;
	private String lastName;
	private String contactno;
	private Integer age;
	private MaritalStatus maritalstatus;
	private String gender;
	private List<String> ethnicity;
	private String city;
	private String state;
	private String federalFinancialAid;
	private Double efc;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getContactno() {
		return contactno;
	}

	public void setContactno(String contactno) {
		this.contactno = contactno;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public MaritalStatus getMaritalstatus() {
		return maritalstatus;
	}

	public void setMaritalstatus(MaritalStatus maritalstatus) {
		this.maritalstatus = maritalstatus;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Double getEfc() {
		return efc;
	}

	public void setEfc(Double efc) {
		this.efc = efc;
	}
	
	public String getFederalFinancialAid() {
		return federalFinancialAid;
	}

	public void setFederalFinancialAid(String federalFinancialAid) {
		this.federalFinancialAid = federalFinancialAid;
	}
	
	public List<String> getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(List<String> ethnicity) {
		this.ethnicity = ethnicity;
	}


	public void updateObject(BasicInformation otherObj) {
		this.name = otherObj.name;
		this.email = otherObj.email;
		this.city = otherObj.city;
		this.age = otherObj.age;
		this.lastName = otherObj.lastName;
		this.gender = otherObj.gender;
		this.ethnicity = otherObj.ethnicity;
		this.efc = otherObj.efc;
		this.state = otherObj.state;
		this.contactno = otherObj.contactno;
		this.maritalstatus = otherObj.maritalstatus;
		this.federalFinancialAid = otherObj.federalFinancialAid;
	}

}
