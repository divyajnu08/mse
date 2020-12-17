package com.virtue.csr.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.virtue.csr.dto.ResultDto;

@Document(collection="csr_data")
public class CSRData {

	String id;
	String baseURL;
	@Id
	String studentId;
	BasicInformation basicInfo;
	AdditionalInformation addtnInfo;
	ChoiceInformation choiceInfo;
	Date createdTs;
	Date updatedTs;
	List<ScholarshipInfo> scholarshipList;
	Map<Integer,List<ResultDto>> result;
	
	public List<ScholarshipInfo> getScholarshipList() {
		return scholarshipList;
	}
	public void setScholarshipList(List<ScholarshipInfo> scholarshipList) {
		this.scholarshipList = scholarshipList;
	}
	public Map<Integer, List<ResultDto>> getResult() {
		return result;
	}
	public void setResult(Map<Integer, List<ResultDto>> result) {
		this.result = result;
	}
	public String getBaseURL() {
		return baseURL;
	}
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}
	public ChoiceInformation getChoiceInfo() {
		return choiceInfo;
	}
	public void setChoiceInfo(ChoiceInformation choiceInfo) {
		this.choiceInfo = choiceInfo;
	}
	public Date getCreatedTs() {
		return createdTs;
	}
	public void setCreatedTs(Date createdTs) {
		this.createdTs = createdTs;
	}
	public Date getUpdatedTs() {
		return updatedTs;
	}
	public void setUpdatedTs(Date updatedTs) {
		this.updatedTs = updatedTs;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public BasicInformation getBasicInfo() {
		return basicInfo;
	}
	public void setBasicInfo(BasicInformation basicInfo) {
		this.basicInfo = basicInfo;
	}
	public AdditionalInformation getAddtnInfo() {
		return addtnInfo;
	}
	public void setAddtnInfo(AdditionalInformation addtnInfo) {
		this.addtnInfo = addtnInfo;
	}
}
