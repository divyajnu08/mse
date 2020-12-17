package com.virtue.csr.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.virtue.csr.config.EventPublisher;
import com.virtue.csr.config.SpringConfig;
import com.virtue.csr.constants.MaritalStatus;
import com.virtue.csr.constants.TestTypes;
import com.virtue.csr.dto.KeyValue;
import com.virtue.csr.dto.Status;
import com.virtue.csr.model.AdditionalInformation;
import com.virtue.csr.model.BasicInformation;
import com.virtue.csr.model.ChoiceInformation;
import com.virtue.csr.model.ScholarshipInfo;
import com.virtue.csr.model.VisitorInfo;
import com.virtue.csr.service.CSRCommonService;
import com.virtue.csr.service.CostCalculationService;
import com.virtue.csr.service.EncryptionService;
import com.virtue.csr.service.MeritGrantMethodProvider;
import com.virtue.csr.service.ReportGenerationService;
import com.virtue.csr.service.StateGrantMethodProvider;
import com.virtue.csr.util.CommonUtils;
import com.virtue.csr.util.ConversionUtils;
import com.virtue.csr.utils.TestUtils;

@AutoConfigureDataMongo
@WebMvcTest(controllers = MainController.class)
@EnableMongoRepositories(basePackages = { "com.virtue.csr.repository", "com.virtue.csr.customer" })
@ContextConfiguration(classes = { MainController.class, CommonUtils.class, EncryptionService.class,
		CSRCommonService.class, CostCalculationService.class, MeritGrantMethodProvider.class, ConversionUtils.class,
		StateGrantMethodProvider.class, EventPublisher.class, SpringConfig.class, ReportGenerationService.class })
@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class MainControllerTest {

	static HashMap<String, Object> sessionattr = new HashMap<String, Object>();

	@Autowired
	private MockMvc mockMvc;

	private BasicInformation createBasicInfo() {
		BasicInformation basicInfo = new BasicInformation();
		basicInfo.setName("test");
		basicInfo.setAge(12);
		basicInfo.setCity("Arkansas");
		basicInfo.setEmail("test@test.com");
		basicInfo.setFederalFinancialAid("yes");
		basicInfo.setGender("male");
		basicInfo.setMaritalstatus(MaritalStatus.MARRIED);
		return basicInfo;
	}

	private AdditionalInformation createAddtnInfo() {
		AdditionalInformation addtnInfo = new AdditionalInformation();
		addtnInfo.setEnrollmentYear("2020-2021");
		addtnInfo.setExtraCurricular(true);
		addtnInfo.setGpa(3.0);
		addtnInfo.setHighSchoolName("test");
		addtnInfo.setHighSchoolType("Public");
		addtnInfo.setScore(21.0);
		addtnInfo.setScore_type(TestTypes.ACT);
		return addtnInfo;
	}

	private ChoiceInformation createChoiceInfo() {
		ChoiceInformation choiceInfo = new ChoiceInformation();
		List<KeyValue> quesList = new ArrayList<KeyValue>();
		quesList.add(new KeyValue("50", "yes"));
		Map<Integer, List<KeyValue>> customers = new HashMap<Integer, List<KeyValue>>();
		customers.put(22, quesList);
		choiceInfo.setCustomers(customers);
		return choiceInfo;
	}

	private List<ScholarshipInfo> createScholarshipInfo() {
		List<ScholarshipInfo> result = new ArrayList<ScholarshipInfo>();
		ScholarshipInfo info = new ScholarshipInfo();
		info.setId(22);
		info.setName("Bethel University");
		info.setGrants(10900.0);
		info.setApplyURL("https://www.bethel.edu/admissions/apply/");
		info.setDeadline(null);
		List<KeyValue> list = new ArrayList<KeyValue>();
		list.add(new KeyValue("Merit Grant", "10900.0"));
		list.add(new KeyValue("Royal Merit Scholarship Qualification", "0.0"));
		info.setGrants_breakdown(list);
		result.add(info);
		return result;
	}

	private VisitorInfo createVisitorInfo() {
		VisitorInfo info = new VisitorInfo();
		info.setEmailId("test1@test.com");
		info.setIpAddress("10.10.10.10");
		info.setLatitude(10.01);
		info.setLongitude(10.01);
		info.setName("visitor1");
		return info;
	}

	@Test
	@Order(1)
	public void run_saveBasicInfo() throws Exception {
		MvcResult result = this.mockMvc
				.perform(post("/saveBasicInfo").content(TestUtils.toJsonString(createBasicInfo()))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		Status response = TestUtils.fromJsonString(result.getResponse().getContentAsString(), Status.class);
		String csrObjid = (String) response.getData();
		sessionattr.put("csrObjectId", csrObjid);
	}

	@Test
	@Order(2)
	public void run_getBasicInfo() throws Exception {
		String expected = TestUtils.toJsonString(createBasicInfo());
		MvcResult result;
		Status response = null;
		result = this.mockMvc.perform(get("/getBasicInfo").sessionAttrs(sessionattr).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		response = TestUtils.fromJsonString(result.getResponse().getContentAsString(), Status.class);
		String actual = TestUtils.toJsonString(response.getData());
		assertEquals(actual, expected);
	}

	@Test
	@Order(3)
	public void run_saveAdditionalInfo() throws Exception {
		String expected = TestUtils.toJsonString(createAddtnInfo());
		MvcResult result = this.mockMvc.perform(
				post("/saveAdditionalInfo").sessionAttrs(sessionattr).content(TestUtils.toJsonString(createAddtnInfo()))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		Status response = TestUtils.fromJsonString(result.getResponse().getContentAsString(), Status.class);
		String actual = TestUtils.toJsonString(response.getData());
		assertEquals(actual, expected);
	}

	@Test
	@Order(4)
	public void run_getAdditionalInfo() throws Exception {
		String expected = TestUtils.toJsonString(createAddtnInfo());
		MvcResult result;
		Status response = null;
		result = this.mockMvc
				.perform(get("/getAdditionalInfo").sessionAttrs(sessionattr).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		response = TestUtils.fromJsonString(result.getResponse().getContentAsString(), Status.class);
		String actual = TestUtils.toJsonString(response.getData());
		assertEquals(actual, expected);
	}

	@Test
	@Order(5)
	public void run_saveChoiceInfo() throws Exception {
		String expected = TestUtils.toJsonString(createChoiceInfo());
		MvcResult result = this.mockMvc.perform(
				post("/saveChoiceInfo").sessionAttrs(sessionattr).content(TestUtils.toJsonString(createChoiceInfo()))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		Status response = TestUtils.fromJsonString(result.getResponse().getContentAsString(), Status.class);
		String actual = TestUtils.toJsonString(response.getData());
		assertEquals(actual, expected);
	}

	@Test
	@Order(6)
	public void run_getChoiceInfo() throws Exception {
		String expected = TestUtils.toJsonString(createChoiceInfo());
		MvcResult result;
		Status response = null;
		result = this.mockMvc
				.perform(get("/getChoiceInfo").sessionAttrs(sessionattr).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		response = TestUtils.fromJsonString(result.getResponse().getContentAsString(), Status.class);
		String actual = TestUtils.toJsonString(response.getData());
		assertEquals(actual, expected);
	}

	//@Test
	//@Order(7)
	public void run_getScholarship() throws Exception {
		String expected = TestUtils.toJsonString(createScholarshipInfo());
		MvcResult result = this.mockMvc
				.perform(get("/scholarship").sessionAttrs(sessionattr).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		Status response = TestUtils.fromJsonString(result.getResponse().getContentAsString(), Status.class);
		String actual = TestUtils.toJsonString(response.getData());
		assertEquals(actual, expected);
	}

	@Test
	@Order(8)
	public void run_saveVisitorInfo() throws Exception {
		MvcResult result = this.mockMvc
				.perform(post("/visitor").content(TestUtils.toJsonString(createVisitorInfo()))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		Status response = TestUtils.fromJsonString(result.getResponse().getContentAsString(), Status.class);
		String actual = TestUtils.toJsonString(response.getData());
		assertNotNull(actual);
	}

	@Test
	@Order(9)
	public void run_getVisitorInfo() throws Exception {
		MvcResult result = this.mockMvc
				.perform(get("/visitor/{emailId}", "test1@test.com").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		Status response = TestUtils.fromJsonString(result.getResponse().getContentAsString(), Status.class);
		String actual = TestUtils.toJsonString(response.getData());
		assertNotNull(actual);
	}

}
