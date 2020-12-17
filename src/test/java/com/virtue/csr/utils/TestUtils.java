package com.virtue.csr.utils;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.json.JsonParser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtue.csr.constants.AdmissionTypes;
import com.virtue.csr.constants.HousingOptions;
import com.virtue.csr.constants.MaritalStatus;
import com.virtue.csr.constants.Scenario;
import com.virtue.csr.constants.SchoolTypes;
import com.virtue.csr.model.CSRData;
import com.virtue.csr.model.NPCObject;
import com.virtue.csr.model.ScholarshipInfo;
import com.virtue.csr.model.student.Academics;
import com.virtue.csr.model.student.DemoGraphics;
import com.virtue.csr.model.student.Financials;
import com.virtue.csr.model.student.OtherInformation;


public class TestUtils {
	
	public static String toJsonString(Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
	public static <T> T fromJsonString(final String json,Class<T> clazz) {
		try {
			ObjectMapper mapper=new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			return mapper.readValue(json, clazz);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<CSRData> readInputJsonFileToList(String path){
		List<CSRData> inputList=null;
		ObjectMapper mapper = new ObjectMapper();
		JsonParser parser=new JacksonJsonParser();
		try {
			inputList=mapper.readValue(FileUtils.readFileToString(new File(path)),new TypeReference<List<CSRData>>() {});
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return inputList;
	}
	
	public static List<List<ScholarshipInfo>> readOutputJsonFileToList(String path){
		List<List<ScholarshipInfo>> inputList=null;
		ObjectMapper mapper = new ObjectMapper();
		JsonParser parser=new JacksonJsonParser();
		try {
			inputList=mapper.readValue(FileUtils.readFileToString(new File(path)),new TypeReference<List<List<ScholarshipInfo>>>() {});
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return inputList;
	}

	
	public static NPCObject getDummyNPCObject() {
		NPCObject npcdto = new NPCObject();
		DemoGraphics demo =new DemoGraphics();
		demo.setAcademicYear("2020-2021");
		demo.setAdmissionType(AdmissionTypes.FR);
		demo.setBirthYear(2002);
		demo.setCountry("US");
		demo.setState("AK");
		demo.setZip(32323);
		demo.setUsCitizen(false);
		demo.setDependent(false);
		demo.setDependentCount(0);
		demo.setFirstName("divya");
		demo.setLastName("srivastava");
		demo.setMaritalStatus(MaritalStatus.SINGLE);
		demo.setVeteran(false);
		demo.setSpouseVeteran(false);
		demo.setScenario(Scenario.A);
		npcdto.setDemoGraphics(demo);
		Academics acad=new Academics();
		acad.setAct(30);
		acad.setHsGpa4(3.0);
		acad.setHsType(SchoolTypes.PUBLIC);
		acad.setHsYear(2020);
		npcdto.setAcademics(acad);
		Financials fin = new Financials();
		fin.setParentResidencyState("AK");
		fin.setHouseholdAllCount(4);
		fin.setHouseholdStudcountExclParents(1);
		fin.setHouseholdAllStudCount(1);
		fin.setStudentIncome(1000d);
		fin.setSpouseIncome(1000d);
		fin.setParentsMritalStatus(MaritalStatus.MARRIED);
		fin.setParent1Age(45);
		fin.setParent1Income(30000.0);
		fin.setParent2Age(40);
		fin.setParent2Income(20000.0);
		fin.setNetworthParentsBusiness(50000.0);
		fin.setStudentTotalAssests(15000.0);
		npcdto.setFinancials(fin);
		OtherInformation othr = new OtherInformation();
		othr.setHousingOption(HousingOptions.ONCAMPUS);
		npcdto.setOthers(othr);
		npcdto.setParentsContribution(0.0);
		return npcdto;
	}


}
