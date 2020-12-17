package com.virtue.csr.model;

import java.util.List;

import com.virtue.csr.constants.TestTypes;

public class AdditionalInformation {

	private String highSchoolName;
	private String highSchoolType;
	private Double gpa;
	private Double score;
	private TestTypes score_type;
	private String enrollmentYear;
	private List<String> fieldOfStudy;
	private Boolean sportingInterests;
	private Boolean nationalMarit;
	private Boolean extraCurricular;

	public String getHighSchoolName() {
		return highSchoolName;
	}

	public void setHighSchoolName(String highSchoolName) {
		this.highSchoolName = highSchoolName;
	}

	public String getHighSchoolType() {
		return highSchoolType;
	}

	public void setHighSchoolType(String highSchoolType) {
		this.highSchoolType = highSchoolType;
	}

	public Double getGpa() {
		return gpa;
	}

	public void setGpa(Double gpa) {
		this.gpa = gpa;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public TestTypes getScore_type() {
		return score_type;
	}

	public void setScore_type(TestTypes score_type) {
		this.score_type = score_type;
	}

	public String getEnrollmentYear() {
		return enrollmentYear;
	}

	public void setEnrollmentYear(String enrollmentYear) {
		this.enrollmentYear = enrollmentYear;
	}

	public List<String> getFieldOfStudy() {
		return fieldOfStudy;
	}

	public void setFieldOfStudy(List<String> fieldOfStudy) {
		this.fieldOfStudy = fieldOfStudy;
	}

	public Boolean getSportingInterests() {
		return sportingInterests;
	}

	public void setSportingInterests(Boolean sportingInterests) {
		this.sportingInterests = sportingInterests;
	}

	public Boolean getNationalMarit() {
		return nationalMarit;
	}

	public void setNationalMarit(Boolean nationalMarit) {
		this.nationalMarit = nationalMarit;
	}

	public Boolean getExtraCurricular() {
		return extraCurricular;
	}

	public void setExtraCurricular(Boolean extraCurricular) {
		this.extraCurricular = extraCurricular;
	}

	public void updateObject(AdditionalInformation additinalInfo) {
		this.highSchoolName = additinalInfo.highSchoolName;
		this.highSchoolType = additinalInfo.highSchoolType;
		this.gpa = additinalInfo.gpa;
		this.score_type = additinalInfo.score_type;
		this.score = additinalInfo.score;
		this.enrollmentYear = additinalInfo.enrollmentYear;
		this.fieldOfStudy = additinalInfo.fieldOfStudy;
		this.nationalMarit = additinalInfo.nationalMarit;
		this.sportingInterests = additinalInfo.sportingInterests;
		this.extraCurricular = additinalInfo.extraCurricular;
	}

}
