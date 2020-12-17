package com.virtue.csr.service;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.virtue.csr.constants.CONSTANTS;
import com.virtue.csr.model.Partner;
import com.virtue.csr.repository.PartnerRepository;

@Service
public class CSRCommonService {
	
	@Autowired
	private SequenceGenerator seqGenerator;
	
	@Autowired
	private PartnerRepository partnerRepository; 
	
	public String getStudentId() {
		Optional<String> seqId = Optional.of(String.valueOf(seqGenerator.getNextSequenceId("student_id")));
		return seqId.orElse("");
	}
	
	public String getCustomer(String baseUrl) {
		Partner partner=partnerRepository.findByHost(URI.create(baseUrl).getHost()).orElse(null);
		return ( partner != null ) ? partner.getCode() : CONSTANTS.DEFAULT_PARTNER_CODE;
	}
}
