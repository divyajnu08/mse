package com.virtue.csr.controller;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.virtue.csr.constants.CONSTANTS;
import com.virtue.csr.constants.FeesItems;
import com.virtue.csr.dto.KeyValue;
import com.virtue.csr.dto.Status;
import com.virtue.csr.exception.APIException;
import com.virtue.csr.model.Country;
import com.virtue.csr.model.FeesObject;
import com.virtue.csr.model.MasterConfig;
import com.virtue.csr.model.NPCObject;
import com.virtue.csr.repository.CountryReporsitory;
import com.virtue.csr.repository.MasterConfigRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Master Config Controller")
@RestController
@RequestMapping("/config")
public class MasterConfigController {
	private static final Logger logger = LoggerFactory.getLogger(MasterConfigController.class);
	
	@Autowired
	private MasterConfigRepository configRepository;
	
	@Autowired
	private CountryReporsitory countryReporsitory;
	
	@Value("${npc.config.custid}")
	private Integer custid;
	
	@Value("${npc.config.campid}")
	private Integer campid;

	@GetMapping("/getSessions")
	public ResponseEntity<Status> getSession() {
		logger.info(CONSTANTS.LOGGER_MSG,"getSession");
		HashMap<String, String> data = null;
		List<MasterConfig> items = configRepository.findByGroupKeyAndCustidAndCampid("config.session", custid, campid);
		data = new HashMap<String, String>();
		for (MasterConfig item : items) {
			if(!ObjectUtils.isEmpty(item))
				data.put(item.getKey(), item.getValue());
		}
		logger.debug("Session info :" + data);
		return ResponseEntity.ok(new Status(true, data));
	}
	
	@ApiOperation(value = "Get page labels", response = Status.class, produces = "application/json")
	@GetMapping("/getPageLabels")
	public ResponseEntity<Status> getPageLabels(@RequestParam(value = "pageid") Integer pageid) {
		HashMap<String, String> data = null;
		List<MasterConfig> items = configRepository.findByGroupKeyAndCustidAndCampid("labels.page."+pageid, custid, campid);
		data = new HashMap<String, String>();
		for (MasterConfig item : items) {
			if(!ObjectUtils.isEmpty(item))
				data.put(item.getKey(), item.getValue());
		}
		logger.debug("Page labels :" + data);
		return ResponseEntity.ok(new Status(true, data));
	}

	@ApiOperation(value = "Get all states", response = Status.class, produces = "application/json")
	@GetMapping("/states")
	public ResponseEntity<Status> getStates(@RequestParam(value = "country") String country) {
		Query query = new Query();
		query.addCriteria(Criteria.where("iso2").is(country));
		Optional<Country> option = countryReporsitory.findOneByIso2(country);
		if(option.isPresent()) {
			List<KeyValue> states = option.get().getStates();
			states.sort(Comparator.comparing(state -> state.getValue()));
			logger.debug("States :" + states);
			return ResponseEntity.ok(new Status(true, states));
		}		
		return ResponseEntity.ok(new Status());		
	}

	@ApiOperation(value = "Get drop down values", response = Status.class, produces = "application/json")
	@GetMapping("/getdropdownValue")
	public ResponseEntity<Status> getdropdownValue(@RequestParam(value = "dropdownName") String dropdownName) {
		LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
		List<MasterConfig> items = configRepository.findByGroupKeyAndCustidAndCampid(dropdownName, custid, campid);
		for (MasterConfig item : items) {
			if(!ObjectUtils.isEmpty(item))
				data.put(item.getKey(), item.getValue());
		}
		if(dropdownName.equals("dob.year")) {
			result=data.entrySet()
			.stream()
			.sorted((e1,e2)->Integer.valueOf(e2.getValue()).compareTo(Integer.valueOf(e1.getValue())))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1,LinkedHashMap::new));
		}else {
			result=data;
		}
		logger.debug("Drop down values" + result);
		return ResponseEntity.ok(new Status(true, result));
	}
	
	@ApiOperation(value = "Get High school year values", response = Status.class, produces = "application/json")
	@GetMapping("/getHSYearValues")
	public ResponseEntity<Status> getHSYearValues(@RequestParam(value = "dropdownName") String dropdownName, HttpSession httpSession) {
		LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
		NPCObject npcdto = (NPCObject) httpSession.getAttribute("npcDTO");
		if(!ObjectUtils.isEmpty(npcdto)) {
			//String birth_date = npcdto.getDemoGraphics().getBirthDate();
			//int year = Integer.parseInt(birth_date.substring(3, 6));
			int year = npcdto.getDemoGraphics().getBirthYear();
			for (int i = 0; i < 15; i++) {				
				data.put(Integer.toString(year+i), Integer.toString(year+i));				
			}
		}else {
			List<MasterConfig> items = configRepository.findByGroupKeyAndCustidAndCampid(dropdownName, custid, campid);
			for (MasterConfig item : items) {
				if(!ObjectUtils.isEmpty(item))
					data.put(item.getKey(), item.getValue());
			}
		}
		result=data.entrySet()
		.stream()
		.sorted((e1,e2)->Integer.valueOf(e2.getValue()).compareTo(Integer.valueOf(e1.getValue())))
		.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1,LinkedHashMap::new));
		logger.debug("HS year values" + result);
		return ResponseEntity.ok(new Status(true, result));
	}

}
