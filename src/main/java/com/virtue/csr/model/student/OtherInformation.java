package com.virtue.csr.model.student;


import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.virtue.csr.constants.HousingOptions;

public class OtherInformation implements Serializable {
	private static final long serialVersionUID = 1L;
	private String studentId;
	private HousingOptions housingOption;
	private boolean churchAffliation;
	private boolean legacyAffliation;
	private boolean contactInfo;
	private String emailId;
	private String mobileNo;
	private Date ts;
	
	
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public HousingOptions getHousingOption() {
		return housingOption;
	}
	public void setHousingOption(HousingOptions housingOption) {
		this.housingOption = housingOption;
	}
	public boolean isChurchAffliation() {
		return churchAffliation;
	}
	public void setChurchAffliation(boolean churchAffliation) {
		this.churchAffliation = churchAffliation;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public boolean isLegacyAffliation() {
		return legacyAffliation;
	}
	public void setLegacyAffliation(boolean legacyAffliation) {
		this.legacyAffliation = legacyAffliation;
	}
	public boolean isContactInfo() {
		return contactInfo;
	}
	public void setContactInfo(boolean contactInfo) {
		this.contactInfo = contactInfo;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	
	public void updateObject(OtherInformation otherObj) {
		this.housingOption = otherObj.housingOption;
		this.churchAffliation = otherObj.churchAffliation;
		this.legacyAffliation = otherObj.legacyAffliation;
		this.contactInfo = otherObj.contactInfo;
		this.emailId = otherObj.emailId;
		this.mobileNo = otherObj.mobileNo;		
	}
		
}
