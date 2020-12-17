package com.virtue.csr.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.virtue.csr.config.EventPublisher;
import com.virtue.csr.constants.AdmissionTypes;
import com.virtue.csr.constants.HousingOptions;
import com.virtue.csr.constants.MESSAGE_TYPES;
import com.virtue.csr.constants.MaritalStatus;
import com.virtue.csr.constants.Scenario;
import com.virtue.csr.constants.SchoolTypes;
import com.virtue.csr.dto.ResultDto;
import com.virtue.csr.dto.Status;
import com.virtue.csr.exception.APIException;
import com.virtue.csr.model.AdditionalInformation;
import com.virtue.csr.model.BasicInformation;
import com.virtue.csr.model.CSRData;
import com.virtue.csr.model.ChoiceInformation;
import com.virtue.csr.model.FeedBack;
import com.virtue.csr.model.NPCObject;
import com.virtue.csr.model.ScholarshipInfo;
import com.virtue.csr.model.VisitorInfo;
import com.virtue.csr.model.student.Academics;
import com.virtue.csr.model.student.DemoGraphics;
import com.virtue.csr.model.student.Financials;
import com.virtue.csr.model.student.OtherInformation;
import com.virtue.csr.repository.CSRObjectRepository;
import com.virtue.csr.repository.UserSubscriptionRepository;
import com.virtue.csr.repository.VisitorInfoRepository;
import com.virtue.csr.service.CSRCommonService;
import com.virtue.csr.service.CostCalculationService;
import com.virtue.csr.service.ReportGenerationService;
import com.virtue.csr.service.ReportGenerationService.ReportTypes;
import com.virtue.csr.util.CommonUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Main Controller")
@Controller
public class MainController {

	private static final Logger logger = LoggerFactory.getLogger(MainController.class);

	@Autowired
	private CommonUtils utils;

	@Autowired
	private CSRCommonService service;

	@Autowired
	private CSRObjectRepository csrRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private CostCalculationService costCalcService;

	@Autowired
	private EventPublisher publisher;

	@Autowired
	private VisitorInfoRepository visitorRepository;

	@Autowired
	private UserSubscriptionRepository userSubscrRepository;

	@Autowired
	private ReportGenerationService reportService;

	private NPCObject createDummyObject() {
		NPCObject npcdto = new NPCObject();
		DemoGraphics demo = new DemoGraphics();
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
		Academics acad = new Academics();
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

	@ApiOperation(value = "saveBasicInformation", response = Status.class, produces = "application/json")
	@RequestMapping(path = "saveBasicInfo", method = RequestMethod.POST)
	public ResponseEntity<Status> saveBasicInfo(HttpServletRequest request, @RequestBody BasicInformation data,
			HttpSession httpSession) {
		CSRData csrdto = new CSRData();
		CSRData dbObj = null;
		data = utils.writeBasicInfo(data);
		String baseURL = request.getHeader("Origin");
		// String baseURL = request.getScheme() + "://" + request.getServerName() + ":"
		// + request.getServerPort() + request.getContextPath();
		try {
			if (ObjectUtils.isEmpty(httpSession.getAttribute("csrObjectId"))) {
				String studentId = service.getStudentId();
				csrdto = new CSRData();
				csrdto.setStudentId(studentId);
				csrdto.setBasicInfo(data);
				csrdto.setCreatedTs(new Date());
				csrdto.setBaseURL(baseURL);
				dbObj = csrRepository.save(csrdto);
				httpSession.setAttribute("csrObjectId", dbObj.getStudentId());
			} else {
				String csrid = (String) httpSession.getAttribute("csrObjectId");
				if (csrid != null) {
					Optional<CSRData> csrObj = csrRepository.findOneByStudentId(csrid);
					if (csrObj.isPresent()) {
						dbObj = csrObj.get();
					} else {
						dbObj = new CSRData();
					}
					if (ObjectUtils.isEmpty(dbObj.getBasicInfo())) {
						dbObj.setBasicInfo(data);
					} else {
						dbObj.getBasicInfo().updateObject(data);
					}
					dbObj.setUpdatedTs(new Date());
					dbObj.setBaseURL(baseURL);
					dbObj = csrRepository.save(dbObj);
					httpSession.setAttribute("csrObjectId", dbObj.getStudentId());
				}
			}
			publisher.publishEvent(dbObj);
			return ResponseEntity.ok(new Status(true, dbObj.getStudentId()));
		} catch (Exception e) {
			logger.error("error while saving basic information");
			throw new APIException(e);
		}
	}

	@ApiOperation(value = "Get basicInfo Page", response = Status.class, produces = "application/json")
	@RequestMapping(path = "getBasicInfo", method = RequestMethod.GET)
	public ResponseEntity<Status> getBasicInfo(HttpSession httpSession) {
		try {
			if (ObjectUtils.isEmpty(httpSession.getAttribute("csrObjectId"))) {
				BasicInformation pageObj = new BasicInformation();
				return ResponseEntity.ok(new Status(true, pageObj));
			} else {
				String csrid = (String) httpSession.getAttribute("csrObjectId");
				CSRData csrObj = csrRepository.findOneByStudentId(csrid).get();
				logger.debug("basic data from session:" + utils.toJsonString(csrid));
				BasicInformation data = utils.readBasicInfo(csrObj.getBasicInfo());
				return ResponseEntity.ok(new Status(true, data));
			}
		} catch (Exception e) {
			logger.error("error while fetching basic information");
			throw new APIException(e);
		}
	}

	@ApiOperation(value = "saveAdditionalInformation", response = Status.class, produces = "application/json")
	@RequestMapping(path = "saveAdditionalInfo", method = RequestMethod.POST)
	public ResponseEntity<Status> saveAdditionalInfo(@RequestBody AdditionalInformation data, HttpSession httpSession) {
		String studentId = null;
		CSRData csrdto = new CSRData();
		try {
			if (!ObjectUtils.isEmpty(httpSession.getAttribute("csrObjectId"))) {
				studentId = (String) httpSession.getAttribute("csrObjectId");
				csrdto = csrRepository.findOneByStudentId(studentId).get();
				if (csrdto != null) {
					if (ObjectUtils.isEmpty(csrdto.getAddtnInfo())) {
						csrdto.setAddtnInfo(data);
					} else {
						csrdto.getAddtnInfo().updateObject(data);
					}
					csrdto.setUpdatedTs(new Date());
					csrRepository.save(csrdto);
					httpSession.setAttribute("csrObjectId", studentId);
				}
			}
			return ResponseEntity.ok(
					new Status(true, Optional.ofNullable(csrdto.getAddtnInfo()).orElse(new AdditionalInformation())));
		} catch (Exception e) {
			logger.error("error while saving basic information");
			throw new APIException(e);
		}
	}

	@ApiOperation(value = "Get additonalInfo Page", response = Status.class, produces = "application/json")
	@RequestMapping(path = "getAdditionalInfo", method = RequestMethod.GET)
	public ResponseEntity<Status> getAdditionalInfo(HttpSession httpSession) {
		try {
			if (ObjectUtils.isEmpty(httpSession.getAttribute("csrObjectId"))) {
				AdditionalInformation pageObj = new AdditionalInformation();
				return ResponseEntity.ok(new Status(true, pageObj));
			} else {
				String studentId = (String) httpSession.getAttribute("csrObjectId");
				CSRData csrObj = csrRepository.findOneByStudentId(studentId).get();
				logger.debug("additional data from session:" + utils.toJsonString(csrObj.getAddtnInfo()));
				return ResponseEntity.ok(new Status(true, csrObj.getAddtnInfo()));
			}
		} catch (Exception e) {
			logger.error("error while fetching additional information");
			throw new APIException(e);
		}
	}

	@ApiOperation(value = "saveChoiceInformation", response = Status.class, produces = "application/json")
	@RequestMapping(path = "saveChoiceInfo", method = RequestMethod.POST)
	public ResponseEntity<Status> saveChoiceInfo(@RequestBody ChoiceInformation data, HttpSession httpSession) {
		CSRData csrdto = new CSRData();
		try {
			if (!ObjectUtils.isEmpty(httpSession.getAttribute("csrObjectId"))) {
				String studentId = (String) httpSession.getAttribute("csrObjectId");
				if (studentId != null) {
					csrdto = csrRepository.findOneByStudentId(studentId).get();
					if (ObjectUtils.isEmpty(csrdto.getChoiceInfo())) {
						csrdto.setChoiceInfo(data);
					} else {
						csrdto.getChoiceInfo().updateObject(data);
					}
					csrdto.setUpdatedTs(new Date());
					csrdto = csrRepository.save(csrdto);
					httpSession.setAttribute("csrObjectId", csrdto.getStudentId());
					List<List<Integer>> selectedIds = new ArrayList<List<Integer>>();
					for (Integer key : data.getCustomers().keySet()) {
						ArrayList<Integer> keys = new ArrayList<Integer>();
						keys.add(key);
						keys.add(key);
						selectedIds.add(keys);
					}
				}
			}
			return ResponseEntity
					.ok(new Status(true, Optional.ofNullable(csrdto.getChoiceInfo()).orElse(new ChoiceInformation())));
		} catch (Exception e) {
			logger.error("error while saving basic information");
			throw new APIException(e);
		}
	}

	@ApiOperation(value = "Get choiceInfo Page", response = Status.class, produces = "application/json")
	@RequestMapping(path = "getChoiceInfo", method = RequestMethod.GET)
	public ResponseEntity<Status> getChoiceInfo(HttpSession httpSession) {
		try {
			if (ObjectUtils.isEmpty(httpSession.getAttribute("csrObjectId"))) {
				ChoiceInformation pageObj = new ChoiceInformation();
				return ResponseEntity.ok(new Status(true, pageObj));
			} else {
				String studentId = (String) httpSession.getAttribute("csrObjectId");
				CSRData csrdto = csrRepository.findOneByStudentId(studentId).get();
				logger.debug("additional data from session:" + utils.toJsonString(csrdto.getChoiceInfo()));
				return ResponseEntity.ok(new Status(true, csrdto.getChoiceInfo()));
			}
		} catch (Exception e) {
			logger.error("error while fetching the choice info page");
			throw new APIException(e);
		}
	}

	@ApiOperation(value = "Get results Page", response = Status.class, produces = "application/json")
	@RequestMapping(path = "result", method = RequestMethod.GET)
	public ResponseEntity<Status> getResult(HttpSession httpSession) {
		try {
			Map<String, List<ResultDto>> result = null;
			if (ObjectUtils.isEmpty(httpSession.getAttribute("csrObjectId"))) {
				result = new HashMap<String, List<ResultDto>>();
				return ResponseEntity.ok(new Status(true, result));
			} else {
				String studentId = (String) httpSession.getAttribute("csrObjectId");
				CSRData csrdto = csrRepository.findOneByStudentId(studentId).get();
				logger.debug("result data from session:" + utils.toJsonString(csrdto.getResult()));
				return ResponseEntity.ok(new Status(true, csrdto.getResult()));
			}
		} catch (Exception e) {
			logger.error("Error while getting result");
			throw new APIException(e);
		}
	}

	@ApiOperation(value = "Save result", response = Status.class, produces = "application/json")
	@RequestMapping(path = "result", method = RequestMethod.POST)
	public ResponseEntity<Status> saveResult(HttpSession httpSession) {
		CSRData csrdto = null;
		try {
			String studentId = (String) httpSession.getAttribute("csrObjectId");
			if (studentId != null) {
				csrdto = csrRepository.findOneByStudentId(studentId).get();
				Map<Integer, List<ResultDto>> result = costCalcService.generateOutput(csrdto, createDummyObject());
				csrdto.setResult(result);
				csrdto = csrRepository.save(csrdto);
			}
			return ResponseEntity.ok(new Status(true, csrdto.getResult()));
		} catch (Exception e) {
			logger.error("error while saving result");
			throw new APIException(e);
		}
	}

	@ApiOperation(value = "get scholarship Info", response = Status.class, produces = "application/json")
	@RequestMapping(path = "scholarship", method = RequestMethod.GET)
	public ResponseEntity<Status> getScholarship(HttpSession httpSession) {
		String studentId = null;
		CSRData csrdto = null;
		List<ScholarshipInfo> resultList = null;
		try {
			studentId = (String) httpSession.getAttribute("csrObjectId");
			if (studentId != null) {
				csrdto = csrRepository.findOneByStudentId(studentId).orElse(null);
			}
			if (!ObjectUtils.isEmpty(csrdto)) {
				resultList = costCalcService.processScholarshipBreakdown(csrdto, createDummyObject());
				csrdto.setScholarshipList(resultList);
				csrRepository.save(csrdto);
			} else {
				return ResponseEntity.ok(new Status(false, "Data not found in database"));
			}
			return ResponseEntity.ok(new Status(true, resultList));
		} catch (Exception e) {
			logger.error("error while getting scholarship information information for student : {}", studentId);
			throw new APIException(e);
		}
	}

	@ApiOperation(value = "save visitor info", response = Status.class, produces = "application/json")
	@CrossOrigin(origins = "*")
	@RequestMapping(path = "visitor", method = RequestMethod.POST)
	public ResponseEntity<Status> saveVisitorInfo(HttpSession httpSession, @RequestBody VisitorInfo data) {
		try {
			VisitorInfo visitorDto = visitorRepository.findByEmailId(data.getEmailId()).orElse(null);
			if (visitorDto != null) {
				visitorDto.updateObject(data);
			} else {
				visitorDto = data;
				visitorDto.setCreatedTs(new Date());
			}
			visitorRepository.save(visitorDto);
			return ResponseEntity.ok(new Status(true, visitorDto));
		} catch (Exception e) {
			logger.error("error while saving visitor information: {}", data);
			throw new APIException(e);
		}
	}

	@ApiOperation(value = "get visitor info", response = Status.class, produces = "application/json")
	@RequestMapping(path = "visitor/{emailId}", method = RequestMethod.GET)
	public ResponseEntity<Status> getVisitorInfo(HttpSession httpSession, @PathVariable String emailId) {
		try {
			VisitorInfo visitorDto = visitorRepository.findByEmailId(emailId).orElse(new VisitorInfo());
			return ResponseEntity.ok(new Status(true, visitorDto));
		} catch (Exception e) {
			logger.error("error while getting visitor information for user {}", emailId);
			throw new APIException(e);
		}
	}

	@ApiOperation(value = "kill session", response = Status.class, produces = "application/json")
	@RequestMapping(path = "killSession", method = RequestMethod.GET)
	public ResponseEntity<Status> killSession(HttpSession httpSession) {
		try {
			String sessionId = httpSession.getId();
			LocalDateTime createdTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(httpSession.getCreationTime()),
					ZoneId.systemDefault());
			httpSession.invalidate();
			logger.info("Session with session id {} killed. Created on {}", sessionId, createdTime);
			return ResponseEntity.ok(new Status(true, "session killed"));
		} catch (Exception e) {
			logger.error("Error in killing session", e);
			throw new APIException(e);
		}
	}

	@ApiOperation(value = "saveFeedback", response = Status.class, produces = "application/json")
	@RequestMapping(path = "saveFeedback", method = RequestMethod.POST)
	public ResponseEntity<Status> saveFeedback(@RequestBody FeedBack feedBack, HttpSession httpSession) {
		if (feedBack.isValid()) {
			String studentId = (String) httpSession.getAttribute("csrObjectId");
			CSRData csrdto = csrRepository.findOneByStudentId(studentId).get();
			feedBack.setStudentId(studentId);
			if (!ObjectUtils.isEmpty(csrdto) && StringUtils.hasText(csrdto.getBasicInfo().getEmail()))
				feedBack.setEmail(csrdto.getBasicInfo().getEmail());
			if (!ObjectUtils.isEmpty(csrdto) && StringUtils.hasText(csrdto.getBasicInfo().getContactno()))
				feedBack.setMobile(csrdto.getBasicInfo().getContactno());
			mongoTemplate.save(feedBack);
			return ResponseEntity.ok(new Status(true, MESSAGE_TYPES.INFO, "Feedback has been saved.", null));
		} else {
			return ResponseEntity.ok(new Status("Please provide feedback message and category.", MESSAGE_TYPES.WARN));
		}

	}

	@ApiOperation(value = "Validate Subscription", response = Status.class)
	@RequestMapping(path = "validateSubscription", method = RequestMethod.GET)
	public ResponseEntity<Status> validateSubscription(@RequestParam String token, HttpSession httpSession) {
		return ResponseEntity
				.ok(new Status(true, userSubscrRepository.findValidToken(token, LocalDateTime.now()) != null));
	}

	@ApiOperation(value = "Extract counsellor data", response = Status.class)
	@RequestMapping(path = "getCounsellorData", method = RequestMethod.GET)
	public ResponseEntity<Status> getCounsellorData(@RequestParam(required=false) String instName,HttpSession httpSession) {
		return ResponseEntity.ok(new Status(true, MESSAGE_TYPES.INFO, "Counsellor Data extracted.",
				reportService.extractCounsellorData(instName)));
	}
	
	@ApiOperation(value = "Extract counsellor data", response = Status.class)
	@RequestMapping(path = "getCounsellorDataByType", method = RequestMethod.GET)
	public ResponseEntity<Status> getCounsellorDataByType(@RequestParam(required=false) String instName,
			@RequestParam ReportTypes reportType,@RequestParam(defaultValue="0") int pageno, HttpSession httpSession) {
		return ResponseEntity.ok(new Status(true, MESSAGE_TYPES.INFO, "Counsellor Data extracted.",
				reportService.extractCounsellorData(instName,reportType,pageno)));
	}

	@ApiOperation(value = "Generate counsellor reports", response = Status.class)
	@RequestMapping(path = "generateCounsellorReport", method = RequestMethod.GET)
	public ResponseEntity<Status> generateCounsellorReport(@RequestParam(required = false) String instName,
			@RequestParam ReportTypes reportType, HttpSession httpSession) {
		try {
			return ResponseEntity.ok(new Status(true, MESSAGE_TYPES.INFO, "Reports has been generated successfully.",
					reportService.exportReportToCSV(instName, reportType)));
		} catch (Exception e) {
			logger.error("Error in report generation", e);
			throw new APIException(e);
		}
	}
	
	@ApiOperation(value = "Get student elligible for finances", response = Status.class)
	@RequestMapping(path = "getStudentsEligibleForFinances", method = RequestMethod.GET)
	public ResponseEntity<Status> getStudentsEligibleForFinances(@RequestParam(required = false) String instName) {
		return ResponseEntity.ok(new Status(true, MESSAGE_TYPES.INFO, "Data fetched successfully.",
				reportService.extractStudentsEligibleForFinances(instName)));
	}
	
	@ApiOperation(value = "Get completed applications by school", response = Status.class)
	@RequestMapping(path = "getCompletedApplicationsBySchool", method = RequestMethod.GET)
	public ResponseEntity<Status> getCompletedApplicationsBySchool() {
		return ResponseEntity.ok(new Status(true, MESSAGE_TYPES.INFO, "Data fetched successfully.",
				reportService.countCompletedApplicationsBySchool()));
	}

	@ApiOperation(value = "Get student counts by ethnicity", response = Status.class)
	@RequestMapping(path = "getStudentsCountByEthnicity", method = RequestMethod.GET)
	public ResponseEntity<Status> getStudentsCountByEthnicity(@RequestParam(required = false) String instName) {
		return ResponseEntity.ok(new Status(true, MESSAGE_TYPES.INFO, "Data fetched successfully.",
				reportService.countStudentsByEthnicity(instName)));
	}
	
	@ApiOperation(value = "Get student counts by gender", response = Status.class)
	@RequestMapping(path = "getStudentsCountByGender", method = RequestMethod.GET)
	public ResponseEntity<Status> getStudentsCountByGender(@RequestParam(required = false) String instName) {
		return ResponseEntity.ok(new Status(true, MESSAGE_TYPES.INFO, "Data fetched successfully.",
				reportService.countStudentsByGender(instName)));
	}

	@ApiOperation(value = "Get student counts by gender", response = Status.class)
	@RequestMapping(path = "getApplicationsCountByMonth", method = RequestMethod.GET)
	public ResponseEntity<Status> getApplicationCountByMonth(@RequestParam(required = false) String instName) {
		return ResponseEntity.ok(new Status(true, MESSAGE_TYPES.INFO, "Data fetched successfully.",
				reportService.countApplicationsByMonth(instName)));
	}
	
	@ApiOperation(value = "Get total applications count",response = Status.class)
	@RequestMapping(path = "getTotalApplicationsCount", method = RequestMethod.GET)
	public ResponseEntity<Status> getTotalApplicationsCount(@RequestParam(required = false) String instName) {
		return ResponseEntity.ok(new Status(true, MESSAGE_TYPES.INFO, "Data fetched successfully.",
				reportService.countTotalApplications(instName)));
	}
	
	@ApiOperation(value = "Get completed applications by school", response = Status.class)
	@RequestMapping(path = "getCompletedApplicationsBySchoolPerRegion", method = RequestMethod.GET)
	public ResponseEntity<Status> getCompletedApplicationsBySchoolPerRegion(@RequestParam(required = false) String region) {
		return ResponseEntity.ok(new Status(true, MESSAGE_TYPES.INFO, "Data fetched successfully.",
				reportService.countCompletedApplicationsBySchoolPerRegion(region)));
	}
	
	@ApiOperation(value = "Get student counts by ethnicity", response = Status.class)
	@RequestMapping(path = "getStudentsCountByEthnicityPerRegion", method = RequestMethod.GET)
	public ResponseEntity<Status> getStudentsCountByEthnicityPerRegion(@RequestParam(required = false) String region) {
		return ResponseEntity.ok(new Status(true, MESSAGE_TYPES.INFO, "Data fetched successfully.",
				reportService.countStudentsByEthnicityPerRegion(region)));
	}
	
	@ApiOperation(value = "Get student counts by gender", response = Status.class)
	@RequestMapping(path = "getStudentsCountByGenderPerRegion", method = RequestMethod.GET)
	public ResponseEntity<Status> getStudentsCountByGenderPerRegion(@RequestParam(required = false) String region) {
		return ResponseEntity.ok(new Status(true, MESSAGE_TYPES.INFO, "Data fetched successfully.",
				reportService.countStudentsByGenderPerRegion(region)));
	}
	
	@ApiOperation(value = "Get applications count by school",response = Status.class)
	@RequestMapping(path = "getApplicationsCountBySchool", method = RequestMethod.GET)
	public ResponseEntity<Status> getApplicationsCountBySchool(@RequestParam(required = false) String region) {
		return ResponseEntity.ok(new Status(true, MESSAGE_TYPES.INFO, "Data fetched successfully.",
				reportService.countApplicationsBySchool()));
	}
	
	
}

