package com.virtue.csr.util;

import java.util.HashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.virtue.csr.model.BasicInformation;
import com.virtue.csr.service.EncryptionService;

@Component
public class CommonUtils {
	private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

	private ObjectWriter jsonWriter;

	@Autowired(required = true)
	public void setJsonWriter(ObjectWriter writer) {
		jsonWriter = writer;
	}
	
	@Autowired
	private EncryptionService encryptionService;

	public BasicInformation writeBasicInfo(BasicInformation data) {
		BasicInformation output=data;
		if(data.getEmail()!=null) 
			data.setEmail(encryptionService.encrypt(data.getEmail()));
		return data;
	}
	
	public BasicInformation readBasicInfo(BasicInformation data) {
		BasicInformation output=data;
		if(data.getEmail()!=null) 
			data.setEmail(encryptionService.decrypt(data.getEmail()));
		return data;
	}

	public String toJsonString(Object data) {
		try {
			return jsonWriter.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			logger.error("Error while converting object to json string");
		}
		return null;
	}
	
	public <K, V> String mapToCSVString(HashMap<K,V> map) {
		return map.values().stream().map(Object::toString).collect(Collectors.joining(","));
	}

}
