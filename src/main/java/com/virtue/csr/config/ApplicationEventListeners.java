package com.virtue.csr.config;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.virtue.csr.events.BasicInfoSubmittedEvent;
import com.virtue.csr.model.BasicInformation;
import com.virtue.csr.model.CSRData;
import com.virtue.csr.model.CustomerCsrData;
import com.virtue.csr.model.MasterConfig;
import com.virtue.csr.repository.MasterConfigRepository;
import com.virtue.csr.service.CSRCommonService;
import com.virtue.csr.service.EncryptionService;

@Component
public class ApplicationEventListeners {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationEventListeners.class);
	
	@Autowired
	@Qualifier("db1MongoTemplate")
	MongoTemplate db1MongoTemplate;
	
	@Autowired
	private MasterConfigRepository configRepository;
	
	@Autowired
	private HashMap<String, Object> configProperties;
	
	@Autowired
	private CSRCommonService commonService;
	
	@Autowired
	private EncryptionService encryptionService;
	
	@EventListener
	public void savePIIData(BasicInfoSubmittedEvent event) {
		CSRData csrData=event.getData();
		BasicInformation basicInfo=csrData.getBasicInfo();
		String customer=commonService.getCustomer(csrData.getBaseURL());
		CustomerCsrData data=new CustomerCsrData();
		data.setStudentId(customer+csrData.getStudentId());
		data.setEmailId(encryptionService.decrypt(basicInfo.getEmail()));
		data.setName(basicInfo.getName());
		data.setCity(basicInfo.getCity());
		data.setState(basicInfo.getState());
		data.setContactNumber(basicInfo.getContactno());
		data.setCustomer(customer);
		data.setCreatedTs(LocalDateTime.now());
		db1MongoTemplate.save(data);
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		List<MasterConfig> items = configRepository.findByGroupKey("csr.config");
		for (MasterConfig masterConfig : items) {
			configProperties.put(masterConfig.getKey(), masterConfig.getValue());
		}
		logger.debug("configuration properties:" + configProperties);
	}

}
