package com.virtue.csr.model.stripe;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Plan {
	
	@Autowired
	private ObjectMapper mapper = new ObjectMapper();
	
	private String id;
	private String type;
	private Long amount;
	private String interval;
	private Long intervalCount;
	private String name;
	private String currency;
	private Map<String, Boolean> details;
	private int refund;
	private boolean active;
	
	
	public Plan(String id, String type, Long amount, String interval, Long intervalCount, String name, String currency,
			boolean active, String details, int refund) {
		super();
		this.id = id;
		this.type = type;
		this.amount = amount;
		this.interval = interval;
		this.intervalCount = intervalCount;
		this.name = name;
		this.currency = currency;
		this.active = active;
		try {
			this.details = mapper.readValue(details, new TypeReference<Map<String, Boolean>>() {});
		} catch (JsonMappingException e) {
			
		} catch (JsonProcessingException e) {
			
		}
		this.refund = refund;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public String getInterval() {
		return interval;
	}
	public void setInterval(String interval) {
		this.interval = interval;
	}
	public Long getIntervalCount() {
		return intervalCount;
	}
	public void setIntervalCount(Long intervalCount) {
		this.intervalCount = intervalCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}	

	public Map<String, Boolean> getDetails() {
		return details;
	}

	public void setDetails(Map<String, Boolean> details) {
		this.details = details;
	}

	public int getRefund() {
		return refund;
	}

	public void setRefund(int refund) {
		this.refund = refund;
	}
	
}
