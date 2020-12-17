package com.virtue.csr.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.virtue.csr.events.BasicInfoSubmittedEvent;
import com.virtue.csr.model.CSRData;

@Component
public class EventPublisher {
	
	@Autowired
	private final ApplicationEventPublisher publisher;
	
	EventPublisher(ApplicationEventPublisher publisher) {
	      this.publisher = publisher;
	}
	
	public void publishEvent(CSRData data) {
		 publisher.publishEvent(new BasicInfoSubmittedEvent(this, data));
	}

}
