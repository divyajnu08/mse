package com.virtue.csr.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.virtue.csr.constants.AdmissionTypes;
import com.virtue.csr.constants.EmploymentStatus;
import com.virtue.csr.constants.EnrollmentStatus;
import com.virtue.csr.constants.HousingOptions;
import com.virtue.csr.constants.MaritalStatus;
import com.virtue.csr.constants.Scenario;
import com.virtue.csr.constants.SchoolTypes;
import com.virtue.csr.dto.Status;
import com.virtue.csr.model.NPCObject;
import com.virtue.csr.model.School;
import com.virtue.csr.model.student.Academics;
import com.virtue.csr.model.student.DemoGraphics;
import com.virtue.csr.model.student.Financials;
import com.virtue.csr.model.student.OtherInformation;
import com.virtue.csr.repository.SchoolRepository;
import com.virtue.csr.service.LookupDataService;
import com.virtue.csr.service.NpcCommonService;
import com.virtue.csr.service.NpcCostCalculationService;
import com.virtue.csr.util.ConversionUtils;


@Controller
public class DefaultController {

	@Autowired
	private LookupDataService lookupDataService;
	
	@Autowired
	private NpcCostCalculationService service;
	
	@Autowired
	private NpcCommonService cmnService;

	@Autowired
	private ConversionUtils conversionUtils;
	
	@Autowired
	private SchoolRepository repo;
	
	@RequestMapping(path="testSchoolRepo",method=RequestMethod.GET)
	public ResponseEntity<Status> testSchoolRepo(HttpSession httpSession){
		School sch = new School();
		sch.setId(12);
		sch.setAcademic_year("2020-2021");
		sch.setActive(true);
		sch.setAddress_1("address 1");
		sch.setAddress_2("address 2");
		sch.setCeeb(2334);
		sch.setCity("arkansas");
		sch.setLatitude(10.01);
		sch.setLongitude(10.01);
		sch.setName("test school1");
		sch.setState("MN");
		sch.setState_name("Minnesota");
		sch.setZip("208002");
		repo.save(sch);
		sch.setId(13);
		sch.setCeeb(2335);
		sch.setName("test school2");
		repo.save(sch);
		sch.setId(14);
		sch.setCeeb(2336);
		sch.setActive(false);
		sch.setName("test school3");
		repo.save(sch);
		Optional<School> optn=repo.findByCeeb(2334);
		if(optn.isPresent()) {
			System.out.println(optn.get());
		}
		List<School> schools=repo.findByState("MN");
		schools=repo.findByActive(true);
		optn=repo.findByName("test school1");
		return ResponseEntity.ok(new Status(true,schools));
	}
	
	@RequestMapping(path="test",method=RequestMethod.GET)
	public ResponseEntity<Status> getTest(HttpSession httpSession){
		String academicYear = "2020-2021";
		double data = lookupDataService.getB3AdjustedBusinessNetworth(12000.0,academicYear);
		return ResponseEntity.ok(new Status(true,data));		
	}

	//scenario A
	@RequestMapping(path="testCase1" ,method=RequestMethod.POST )
	public ResponseEntity<Status> getTestResult(HttpSession httpSession) throws JsonProcessingException {
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
		service.processFinalResult(npcdto);
		//return ResponseEntity.ok(new Status(true, npcdto.getResult()));
		return null;
	}
	
	//scenario B
	@RequestMapping(path="testCase2" ,method=RequestMethod.POST )
	public ResponseEntity<Status> getTestCase2(HttpSession httpSession) throws JsonProcessingException {
		NPCObject npcdto = new NPCObject();
		DemoGraphics demo =new DemoGraphics();
		demo.setAcademicYear("2020-2021");
		demo.setAdmissionType(AdmissionTypes.FR);
		demo.setBirthYear(2005);
		demo.setCountry("US");
		demo.setState("AK");
		demo.setZip(32323);
		demo.setUsCitizen(true);
		demo.setDependent(false);
		demo.setDependentCount(0);
		demo.setFirstName("divya");
		demo.setLastName("srivastava");
		demo.setMaritalStatus(MaritalStatus.SINGLE);
		demo.setVeteran(true);
		demo.setSpouseVeteran(false);
		cmnService.setScenario(demo);
		npcdto.setDemoGraphics(demo);
		Academics acad=new Academics();
		acad.setAct(30);
		acad.setHsGpa4(3.0);
		acad.setHsType(SchoolTypes.PUBLIC);
		acad.setHsYear(2020);
		npcdto.setAcademics(acad);
		Financials fin = new Financials();
		fin.setParentsMritalStatus(MaritalStatus.MARRIED);
		fin.setParentResidencyState("AK");
		fin.setParent1Age(45);
		fin.setParent1Income(30000.0);
		fin.setParent2Age(40);
		fin.setParent2Income(20000.0);
		fin.setHouseholdAllCount(4);
		fin.setHouseholdAllStudCount(1);
		fin.setHouseholdStudcountExclParents(1);
		fin.setParentsTotalAssests(10000.0);
		fin.setNetworthParentsBusiness(50000.0);
		fin.setStudentIncome(10000.0);
		fin.setStudentTotalAssests(15000.0);
		fin.setStudentNetworth(20000.0);
		fin.setSpouseIncome(10000.0);
		fin.setParentsTax(100.0);
		fin.setStudentTax(100.0);
		npcdto.setFinancials(fin);
		OtherInformation othr = new OtherInformation();
		othr.setHousingOption(HousingOptions.ONCAMPUS);
		npcdto.setOthers(othr);
		service.processFinalResult(npcdto);
		//return ResponseEntity.ok(new Status(true, npcdto.getResult()));
		return null;
	}
	
	//scenario c
	@RequestMapping(path="testCase3" ,method=RequestMethod.POST )
	public ResponseEntity<Status> getTestCase3(HttpSession httpSession) throws JsonProcessingException {
		NPCObject npcdto = new NPCObject();
		DemoGraphics demo =new DemoGraphics();
		demo.setAcademicYear("2020-2021");
		demo.setAdmissionType(AdmissionTypes.FR);
		demo.setCountry("US");
		demo.setState("AK");
		demo.setZip(32323);
		demo.setUsCitizen(true);
		demo.setDependent(true);
		demo.setDependentCount(2);
		demo.setFirstName("divya");
		demo.setLastName("srivastava");
		demo.setMaritalStatus(MaritalStatus.SINGLE);
		demo.setVeteran(true);
		demo.setDependent(true);
		demo.setDependentCount(2);
		cmnService.setScenario(demo);
		npcdto.setDemoGraphics(demo);
		Academics acad=new Academics();
		acad.setAct(30);
		acad.setHsGpa4(3.0);
		acad.setHsType(SchoolTypes.PUBLIC);
		acad.setHsYear(2020);
		npcdto.setAcademics(acad);
		Financials fin = new Financials();
		fin.setStudentIncome(10000.0);
		fin.setStudentTotalAssests(15000.0);
		fin.setStudentNetworth(20000.0);
		fin.setStudentTax(100.0);
		fin.setEmploymentStatus(EmploymentStatus.ONLY_STUDENT);
		fin.setHouseholdAllStudCount(2);
		fin.setHouseholdAllCount(4);
		npcdto.setFinancials(fin);
		OtherInformation othr = new OtherInformation();
		othr.setHousingOption(HousingOptions.ONCAMPUS);
		npcdto.setOthers(othr);
		service.processFinalResult(npcdto);
		//return ResponseEntity.ok(new Status(true, npcdto.getResult()));
		return null;
	}
}
