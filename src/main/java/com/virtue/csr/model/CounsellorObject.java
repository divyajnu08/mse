/**
 * 
 */
package com.virtue.csr.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 *
 */
@Document(collection="csr_counselor_data")
public class CounsellorObject {

	Integer clientId;
    String applicationCode;
    String firstName;
    String lastName;
    String gender;
    Double actScore;
    Double gpa;
    String applicationCompleteness;
    Integer submissionStatus;
    Integer transcripts;
    Integer submittedApplicantRecommendations;
    Integer completedSteps;
    Integer totalSteps;
    String status;
    String institutionName;
    String ethnicity;
    String readerDecision1;
    Integer readerScore1;
    String readerDecision2;
    Integer readerScore2;
    String colorCode;
    @Field("agi")
    Double adjustedGrossIncome;
    @Field("hSize")
    Integer householdSize;
    Integer ceeb;
    String category;
    
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	public String getApplicationCode() {
		return applicationCode;
	}
	public void setApplicationCode(String applicationCode) {
		this.applicationCode = applicationCode;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Double getActScore() {
		return actScore;
	}
	public void setActScore(Double actScore) {
		this.actScore = actScore;
	}
	public Double getGpa() {
		return gpa;
	}
	public void setGpa(Double gpa) {
		this.gpa = gpa;
	}
	public String getApplicationCompleteness() {
		return applicationCompleteness;
	}
	public void setApplicationCompleteness(String applicationCompleteness) {
		this.applicationCompleteness = applicationCompleteness;
	}
	public Integer getSubmissionStatus() {
		return submissionStatus;
	}
	public void setSubmissionStatus(Integer submissionStatus) {
		this.submissionStatus = submissionStatus;
	}
	public Integer getTranscripts() {
		return transcripts;
	}
	public void setTranscripts(Integer transcripts) {
		this.transcripts = transcripts;
	}
	public Integer getSubmittedApplicantRecommendations() {
		return submittedApplicantRecommendations;
	}
	public void setSubmittedApplicantRecommendations(Integer submittedApplicantRecommendations) {
		this.submittedApplicantRecommendations = submittedApplicantRecommendations;
	}
	public Integer getCompletedSteps() {
		return completedSteps;
	}
	public void setCompletedSteps(Integer completedSteps) {
		this.completedSteps = completedSteps;
	}
	public Integer getTotalSteps() {
		return totalSteps;
	}
	public void setTotalSteps(Integer totalSteps) {
		this.totalSteps = totalSteps;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInstitutionName() {
		return institutionName;
	}
	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}
	public String getEthnicity() {
		return ethnicity;
	}
	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}
	public String getReaderDecision1() {
		return readerDecision1;
	}
	public void setReaderDecision1(String readerDecision1) {
		this.readerDecision1 = readerDecision1;
	}
	public Integer getReaderScore1() {
		return readerScore1;
	}
	public void setReaderScore1(Integer readerScore1) {
		this.readerScore1 = readerScore1;
	}
	public String getReaderDecision2() {
		return readerDecision2;
	}
	public void setReaderDecision2(String readerDecision2) {
		this.readerDecision2 = readerDecision2;
	}
	public Integer getReaderScore2() {
		return readerScore2;
	}
	public void setReaderScore2(Integer readerScore2) {
		this.readerScore2 = readerScore2;
	}
	public String getColorCode() {
		return colorCode;
	}
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	public Double getAdjustedGrossIncome() {
		return adjustedGrossIncome;
	}
	public void setAdjustedGrossIncome(Double adjustedGrossIncome) {
		this.adjustedGrossIncome = adjustedGrossIncome;
	}
	public Integer getHouseholdSize() {
		return householdSize;
	}
	public void setHouseholdSize(Integer householdSize) {
		this.householdSize = householdSize;
	}
	public void setCeeb(Integer ceeb) {
		this.ceeb = ceeb;
	}
	public Integer getCeeb() {
		return ceeb;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

}
